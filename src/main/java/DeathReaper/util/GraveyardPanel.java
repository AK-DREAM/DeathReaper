package DeathReaper.util;

import DeathReaper.DeathReaperCore;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustPileParticle;

import static basemod.BaseMod.openCustomScreen;

public class GraveyardPanel extends AbstractPanel {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static float fontScale;
    private static final float COUNT_CIRCLE_W;
    public static int totalCount;
    private Hitbox hb;
    public static float energyVfxTimer;

    public GraveyardPanel() {
        super((float)Settings.WIDTH - 70.0F * Settings.scale, 284.0F * Settings.scale, (float)Settings.WIDTH + 100.0F * Settings.scale, 284.0F * Settings.scale, 0.0F, 0.0F, (Texture)null, false);
        this.hb = new Hitbox(0.0F, 0.0F, 100.0F * Settings.scale, 100.0F * Settings.scale);
    }

    public boolean notEmpty() {
        return !DeathReaperCore.graveyardPile.isEmpty();
    }

    public void updatePositions() {
        super.updatePositions();
        if (!this.isHidden && notEmpty()) {
            this.hb.update();
            this.updateVfx();
        }

        if (this.hb.hovered && !AbstractDungeon.isScreenUp) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }

        if (this.hb.clicked && AbstractDungeon.overlayMenu.combatPanelsShown && AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.player.isDead && notEmpty()) {
            this.hb.clicked = false;
            this.hb.hovered = false;
            if (AbstractDungeon.isScreenUp) {
                if (AbstractDungeon.previousScreen == null) {
                    AbstractDungeon.previousScreen = AbstractDungeon.screen;
                }
            } else {
                AbstractDungeon.previousScreen = null;
            }
            this.openGraveyardPile();
        }
    }

    private void openGraveyardPile() {
        if (AbstractDungeon.player.hoveredCard != null) {
            AbstractDungeon.player.releaseCard();
        }
        AbstractDungeon.dynamicBanner.hide();
        openCustomScreen(GraveyardScreen.Enum.GRAVEYARD_SCREEN);
        this.hb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    private void updateVfx() {
        energyVfxTimer -= Gdx.graphics.getDeltaTime();
        if (energyVfxTimer < 0.0F && !Settings.hideLowerElements) {
            ExhaustPileParticle i = new ExhaustPileParticle(this.current_x, this.current_y);
            Color c = ReflectionHacks.getPrivate(i, AbstractGameEffect.class, "color");
            float tmp = c.r; c.r = c.g; c.g = tmp;
            AbstractDungeon.effectList.add(i);
            energyVfxTimer = 0.05F;
        }
    }

    public void render(SpriteBatch sb) {
        if (notEmpty()) {
            this.hb.move(this.current_x, this.current_y);
            String msg = Integer.toString(DeathReaperCore.graveyardPile.size());
            sb.setColor(Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x - COUNT_CIRCLE_W / 2.0F, this.current_y - COUNT_CIRCLE_W / 2.0F, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, this.current_x, this.current_y + 2.0F * Settings.scale, new Color(130/255.0F,185/255.0F,238/255.0F,1.0F));
            this.hb.render(sb);
            if (this.hb.hovered && AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                TipHelper.renderGenericTip(1550.0F * Settings.scale, 550.0F * Settings.scale, TEXT[0], TEXT[1]);
            }
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(DeathReaperCore.makeID("Graveyard"));
        TEXT = uiStrings.TEXT;
        fontScale = 1.0F;
        COUNT_CIRCLE_W = 128.0F * Settings.scale;
        totalCount = 0;
        energyVfxTimer = 0.0F;
    }
}
