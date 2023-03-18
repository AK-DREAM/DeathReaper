package DeathReaper.cards;

import DeathReaper.powers.ShockingReckoningPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;

import static DeathReaper.DeathReaperCore.makeCardPath;

public class ShockingReckoning extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(ShockingReckoning.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 2;
    // /STAT DECLARATION/


    public ShockingReckoning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        this.bury = true;
        this.tags.add(Enums.PREFIX);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new ShockingReckoningPower(p, p, magicNumber)));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
