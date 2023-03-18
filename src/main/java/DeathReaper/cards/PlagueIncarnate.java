package DeathReaper.cards;

import DeathReaper.powers.JudgementPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static DeathReaper.DeathReaperCore.makeCardPath;

public class PlagueIncarnate extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(PlagueIncarnate.class.getSimpleName());
    public static final String IMG = makeCardPath("Attack.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/


    public PlagueIncarnate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.hasPower(JudgementPower.POWER_ID)) {
                    this.addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, DeathReaperCore.getPowerAmount(m, JudgementPower.POWER_ID))));
                }
                this.isDone = true;
            }
        });
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
