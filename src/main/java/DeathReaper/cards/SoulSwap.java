package DeathReaper.cards;

import DeathReaper.actions.MyExhumeAction;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;

import java.util.List;
import java.util.function.Consumer;

import static DeathReaper.DeathReaperCore.makeCardPath;

public class SoulSwap extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(SoulSwap.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;
    // /STAT DECLARATION/


    public SoulSwap() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardAction(p, p, magicNumber, false));
        this.addToBot(new MyExhumeAction(magicNumber, false));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeMagicNumber(UPGRADE_MAGIC);
//            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}