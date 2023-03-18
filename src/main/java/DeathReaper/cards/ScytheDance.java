package DeathReaper.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;

import static DeathReaper.DeathReaperCore.makeCardPath;
import static DeathReaper.DeathReaperCore.getPowerAmount;

public class ScytheDance extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(ScytheDance.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int MAGIC = 4;

    // /STAT DECLARATION/


    public ScytheDance() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 1; i <= magicNumber; i++) {
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster tar = null;
                    String powerID = "DeathReaper:JudgementPower";
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                        if (mo.isDeadOrEscaped()) continue;
                        if (tar == null || getPowerAmount(tar, powerID) > getPowerAmount(mo, powerID)) {
                            tar = mo;
                        }
                    }
                    if (tar != null) this.addToTop(new DamageAction(tar, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.FIRE));
                    this.isDone = true;
                }
            });
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
