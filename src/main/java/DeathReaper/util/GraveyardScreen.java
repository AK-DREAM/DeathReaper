package DeathReaper.util;

import DeathReaper.DeathReaperCore;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.overlayMenu;

public class GraveyardScreen extends CustomScreen implements ScrollBarListener {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private CardGroup graveyardPileCopy;
    public boolean isHovered;
    private static final int CARDS_PER_LINE = 5;
    private boolean grabbedScreen;
    private float grabStartY;
    private float currentDiffY;
    private static float drawStartX;
    private static float drawStartY;
    private static float padX;
    private static float padY;
    private float scrollLowerBound;
    private float scrollUpperBound;
    private static final String DESC;
    private AbstractCard hoveredCard;
    private int prevDeckSize;
    private static final float SCROLL_BAR_THRESHOLD;
    private ScrollBar scrollBar;
    private AbstractCard controllerCard;

    public GraveyardScreen() {
        this.graveyardPileCopy = new CardGroup(CardGroupType.UNSPECIFIED);
        this.isHovered = false;
        this.grabbedScreen = false;
        this.grabStartY = 0.0F;
        this.currentDiffY = 0.0F;
        this.scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        this.hoveredCard = null;
        this.prevDeckSize = 0;
        this.controllerCard = null;
        drawStartX = (float)Settings.WIDTH;
        drawStartX -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartX -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartX /= 2.0F;
        drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.scrollBar = new ScrollBar(this);
        this.scrollBar.move(0.0F, -30.0F * Settings.scale);
    }

    public static class Enum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen GRAVEYARD_SCREEN;
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() { return Enum.GRAVEYARD_SCREEN; }

    public void update() {
        boolean isDraggingScrollBar = false;
        if (this.shouldShowScrollBar()) {
            isDraggingScrollBar = this.scrollBar.update();
        }

        if (!isDraggingScrollBar) {
            this.updateScrolling();
        }

        if (Settings.isControllerMode && this.controllerCard != null && !CardCrawlGame.isPopupOpen && !CInputHelper.isTopPanelActive()) {
            if ((float)Gdx.input.getY() > (float)Settings.HEIGHT * 0.7F) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if ((float)Gdx.input.getY() < (float)Settings.HEIGHT * 0.3F) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }
        }

        this.updatePositions();
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        if (!this.grabbedScreen) {
            if (InputHelper.scrolledDown) {
                this.currentDiffY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                this.currentDiffY -= Settings.SCROLL_SPEED;
            }

            if (InputHelper.justClickedLeft) {
                this.grabbedScreen = true;
                this.grabStartY = (float)y - this.currentDiffY;
            }
        } else if (InputHelper.isMouseDown) {
            this.currentDiffY = (float)y - this.grabStartY;
        } else {
            this.grabbedScreen = false;
        }

        if (this.prevDeckSize != this.graveyardPileCopy.size()) {
            this.calculateScrollBounds();
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void calculateScrollBounds() {
        if (this.graveyardPileCopy.size() > 10) {
            int scrollTmp = this.graveyardPileCopy.size() / 5 - 2;
            if (this.graveyardPileCopy.size() % 5 != 0) {
                ++scrollTmp;
            }

            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;
        } else {
            this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;
        }

        this.prevDeckSize = this.graveyardPileCopy.size();
    }

    private void resetScrolling() {
        if (this.currentDiffY < this.scrollLowerBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);
        } else if (this.currentDiffY > this.scrollUpperBound) {
            this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);
        }

    }

    private void updatePositions() {
        this.hoveredCard = null;
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.graveyardPileCopy.group;

        for(int i = 0; i < cards.size(); ++i) {
            int mod = i % 5;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }

            (cards.get(i)).target_x = drawStartX + (float)mod * padX;
            (cards.get(i)).target_y = drawStartY + this.currentDiffY - (float)lineNum * padY;
            (cards.get(i)).update();
            if (AbstractDungeon.topPanel.potionUi.isHidden) {
                (cards.get(i)).updateHoverLogic();
                if ((cards.get(i)).hb.hovered) {
                    this.hoveredCard = cards.get(i);
                }
            }
        }
    }

    @Override
    public void reopen() {
        overlayMenu.cancelButton.show(TEXT[3]);
    }

    @Override
    public void open(Object... args) {
        CardCrawlGame.sound.play("DECK_OPEN");
        overlayMenu.showBlackScreen();
        overlayMenu.cancelButton.show(TEXT[3]);
        this.currentDiffY = 0.0F;
        this.grabStartY = 0.0F;
        this.grabbedScreen = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = Enum.GRAVEYARD_SCREEN;
        this.graveyardPileCopy.clear();

        for (AbstractCard c : DeathReaperCore.graveyardPile.group) {
            AbstractCard toAdd = c.makeStatEquivalentCopy();
            toAdd.setAngle(0.0F, true);
            toAdd.targetDrawScale = 0.75F;
            toAdd.drawScale = 0.75F;
            toAdd.lighten(true);
            this.graveyardPileCopy.addToBottom(toAdd);
        }
        this.graveyardPileCopy.sortAlphabetically(true);
        this.graveyardPileCopy.sortByRarityPlusStatusCardType(true);
        this.hideCards();
        if (this.graveyardPileCopy.group.size() <= 5) {
            drawStartY = (float)Settings.HEIGHT * 0.5F;
        } else {
            drawStartY = (float)Settings.HEIGHT * 0.66F;
        }
        this.calculateScrollBounds();
    }

    private void hideCards() {
        int lineNum = 0;
        ArrayList<AbstractCard> cards = this.graveyardPileCopy.group;
        for(int i = 0; i < cards.size(); ++i) {
            int mod = i % 5;
            if (mod == 0 && i != 0) {
                ++lineNum;
            }
            (cards.get(i)).current_x = drawStartX + (float)mod * padX;
            (cards.get(i)).current_y = drawStartY + this.currentDiffY - (float)lineNum * padY - MathUtils.random(100.0F * Settings.scale, 200.0F * Settings.scale);
            (cards.get(i)).targetDrawScale = 0.75F;
            (cards.get(i)).drawScale = 0.75F;
        }

    }

    public void render(SpriteBatch sb) {
        if (this.shouldShowScrollBar()) {
            this.scrollBar.render(sb);
        }

        if (this.hoveredCard == null) {
            this.graveyardPileCopy.render(sb);
        } else {
            this.graveyardPileCopy.renderExceptOneCard(sb, this.hoveredCard);
            this.hoveredCard.renderHoverShadow(sb);
            this.hoveredCard.render(sb);
            this.hoveredCard.renderCardTip(sb);
        }

        FontHelper.renderDeckViewTip(sb, DESC, 96.0F * Settings.scale, Settings.CREAM_COLOR);
    }

    public void scrolledUsingBar(float newPercent) {
        this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);
        this.updateBarPosition();
    }

    private void updateBarPosition() {
        float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);
        this.scrollBar.parentScrolledToPercent(percent);
    }

    private boolean shouldShowScrollBar() {
        return this.scrollUpperBound > SCROLL_BAR_THRESHOLD;
    }

    public void openingSettings() {}
    public void close() {
        overlayMenu.cancelButton.hide();
        genericScreenOverlayReset();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(DeathReaperCore.makeID("Graveyard"));
        TEXT = uiStrings.TEXT;
        DESC = TEXT[2];
        SCROLL_BAR_THRESHOLD = 500.0F * Settings.scale;
    }
}
