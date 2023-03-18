package DeathReaper.cards;

import DeathReaper.actions.ModifyPressureAction;
import basemod.AutoAdd;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.DeathReaperCore;

import java.util.ArrayList;
import java.util.List;

import static DeathReaper.DeathReaperCore.makeCardPath;

public class DarkMagicCircle extends AbstractDynamicCard {
    // TEXT DECLARATION

    public static final String ID = DeathReaperCore.makeID(DarkMagicCircle.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDeathReaper.Enums.DEATH_REAPER;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 2;

    private List<AbstractCard> myCardsToPreview = new ArrayList<>();
    private float rotationTimer;
    private int previewIndex;
    // /STAT DECLARATION/


    public DarkMagicCircle() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = DeathReaperCore.generateRandomPrefixCard();
        if (this.upgraded) c.upgrade();
        DeathReaperCore.makeTemporary(c);
        this.addToBot(new MakeTempCardInHandAction(c, 1));
        this.addToBot(new ModifyPressureAction(magicNumber, true));
    }

    public static List<AbstractCard> getList() {
        return DeathReaperCore.generatePrefixCards();
    }

    @Override
    public void update() {
        super.update();
        if (this.myCardsToPreview.isEmpty()) {
            this.myCardsToPreview.addAll(getList());
        }
        if (this.hb.hovered) {
            if (this.rotationTimer <= 0.0F) {
                this.rotationTimer = 2.0F;
                if (this.myCardsToPreview.size() == 0) this.cardsToPreview = CardLibrary.cards.get("Madness");
                else this.cardsToPreview = this.myCardsToPreview.get(this.previewIndex);
                if (this.previewIndex == this.myCardsToPreview.size()-1) this.previewIndex = 0;
                else ++this.previewIndex;
            } else {
                this.rotationTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
