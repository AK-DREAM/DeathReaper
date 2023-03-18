package DeathReaper.cards;

import DeathReaper.actions.ZoomDrawAction;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static DeathReaper.DeathReaperCore.makeCardPath;

public class PunishZone extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(PunishZone.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = -1;
    private static final int UPGRADED_COST = -1;
    // /STAT DECLARATION/


    public PunishZone() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int effect = EnergyPanel.totalCount;
                if (p.hasRelic("Chemical X")) {
                    effect += 2;
                    p.getRelic("Chemical X").flash();
                }
                for (int i = 0; i < effect; i++) {
                    AbstractCard c = DeathReaperCore.generateRandomPrefixCard();
                    if (PunishZone.this.upgraded) c.upgrade();
                    DeathReaperCore.makeTemporary(c);
                    this.addToBot(new MakeTempCardInHandAction(c, 1));
                }
                p.energy.use(EnergyPanel.totalCount);
                this.isDone = true;
            }
        });
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
}
