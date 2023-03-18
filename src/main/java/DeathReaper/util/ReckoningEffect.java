package DeathReaper.util;

import DeathReaper.DeathReaperCore;
import DeathReaper.characters.TheDeathReaper;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

public class ReckoningEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final float HEIGHT_DIV_2;
    private Color bgColor;
    private static final float TARGET_HEIGHT;
    private float currentHeight = 0.0F;
    private static final String BATTLE_START_MSG;
    private float timer = 0.0F;
    private static final float MSG_VANISH_X;
    private float firstMessageX;
    private float totalTime;
    private int fireCount;

    public ReckoningEffect(float totalTime) {
        this.totalTime = totalTime;
        this.firstMessageX = (float)Settings.WIDTH / 2.0F;
        this.duration = this.startingDuration = totalTime;
        this.bgColor = new Color(AbstractDungeon.fadeColor.r / 2.0F, AbstractDungeon.fadeColor.g / 2.0F, AbstractDungeon.fadeColor.b / 2.0F, 0.0F);
        this.color = Settings.GOLD_COLOR.cpy();
        this.color.a = 0.0F;
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.play("BATTLE_START_1");
        } else {
            CardCrawlGame.sound.play("BATTLE_START_2");
        }
        this.fireCount = Math.min(TheDeathReaper.getRealPressure()/2+2, 12);
    }

    public void update() {
        if (this.duration == totalTime) {
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.FIREBRICK));
        }
        if (this.duration > totalTime-0.5F) {
            this.currentHeight = MathUtils.lerp(this.currentHeight, TARGET_HEIGHT, Gdx.graphics.getDeltaTime() * 3.0F);
            this.color.a = MathUtils.lerp(this.color.a, 1.0F, Gdx.graphics.getDeltaTime() * 5.0F);
        } else if (this.duration < 0.5F) {
            this.currentHeight = MathUtils.lerp(this.currentHeight, 0.0F, Gdx.graphics.getDeltaTime() * 3.0F);
            this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
        }
        if (this.duration < 1.0F) {
            this.firstMessageX = Interpolation.pow2In.apply(this.firstMessageX, MSG_VANISH_X, 1.0F - this.duration);
        }
        this.bgColor.a = this.color.a * 0.75F;

        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F && this.duration > 1.0F) {
            for (int i = 0; i < this.fireCount; i++) {
                AbstractDungeon.effectsQueue.add(MyGiantFireEffect());
            }
            this.timer = 0.05F;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.bgColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, HEIGHT_DIV_2 - this.currentHeight / 2.0F, (float)Settings.WIDTH, this.currentHeight);
        sb.setBlendFunction(770, 1);
        FontHelper.renderFontCentered(sb, FontHelper.bannerNameFont, BATTLE_START_MSG, this.firstMessageX, HEIGHT_DIV_2, this.color, 1.0F);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }

    public AbstractGameEffect MyGiantFireEffect() {
        GiantFireEffect i = new GiantFireEffect();
        ReflectionHacks.setPrivate(i, GiantFireEffect.class, "vY", MathUtils.random(200.0F, 600.0F) * Settings.scale);
        return i;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(DeathReaperCore.makeID("ReckoningEffect"));
        TEXT = uiStrings.TEXT;
        HEIGHT_DIV_2 = (float)Settings.HEIGHT / 2.0F;
        TARGET_HEIGHT = 150.0F * Settings.scale;
        BATTLE_START_MSG = TEXT[0];
        MSG_VANISH_X = (float)(Settings.WIDTH) * 1.25F;
    }
}
