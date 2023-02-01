package DeathReaper.cards;

import DeathReaper.powers.JudgementPower;
import basemod.AutoAdd;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DefaultMod;

import static DeathReaper.DefaultMod.makeCardPath;

public class WipeOut extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(WipeOut.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] TEXT = cardStrings.EXTENDED_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private boolean moreDesc = false;


    // /STAT DECLARATION/


    public WipeOut() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.bury = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(JudgementPower.POWER_ID)) {
            this.addToBot(
                    new DamageAction(m, new DamageInfo(p, DefaultMod.getPowerAmount(m, JudgementPower.POWER_ID), DamageInfo.DamageType.HP_LOSS),
                            AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderHand")
    public static class RenderDamagePatch {
        @SpireInsertPatch(rloc = 2268-2229)
        public static void Insert(AbstractPlayer p, SpriteBatch sb) {
            if (p.hoveredCard instanceof WipeOut) {
                WipeOut card = (WipeOut)p.hoveredCard;
                AbstractMonster mo = ReflectionHacks.getPrivate(p, AbstractPlayer.class, "hoveredMonster");
                if (mo != null) {
                    if (!card.moreDesc) {
                        card.moreDesc = true;
                        p.hoveredCard.rawDescription =
                                DESCRIPTION + TEXT[0] + DefaultMod.getPowerAmount(mo, JudgementPower.POWER_ID) + TEXT[1];
                        card.initializeDescription();
                    }
                } else {
                    if (card.moreDesc) {
                        card.moreDesc = false;
                        p.hoveredCard.rawDescription = DESCRIPTION;
                        card.initializeDescription();
                    }
                }
            }
        }
    }
}
