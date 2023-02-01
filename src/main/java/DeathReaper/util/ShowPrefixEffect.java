package DeathReaper.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlyingSpikeEffect;

import java.util.Random;
import java.util.Set;

public class ShowPrefixEffect extends AbstractGameEffect {
    private static final float STARTING_OFFSET_Y;
    private static final float TARGET_OFFSET_Y;
    private float x, y, offsetY;
    private String msg;
    private Color targetColor;
    public boolean over;
    public float timer;
    public float MSG_VANISH_X = Settings.WIDTH * 1.25F;

    public ShowPrefixEffect(float x, float y, String msg, float duration) {
        this.msg = msg;
        this.x = x;
        this.y = y-TARGET_OFFSET_Y;
        this.targetColor = Settings.BLUE_TEXT_COLOR;
        this.color = Color.WHITE.cpy();
        this.offsetY = STARTING_OFFSET_Y;
        this.startingDuration = this.duration = duration;
        this.over = false; this.timer = 1.0F;
    }

    public void update() {
        this.offsetY = Interpolation.exp10In.apply(TARGET_OFFSET_Y, STARTING_OFFSET_Y, this.timer);
        this.color.r = Interpolation.pow2In.apply(this.targetColor.r, 1.0F, this.timer);
        this.color.g = Interpolation.pow2In.apply(this.targetColor.g, 1.0F, this.timer);
        this.color.b = Interpolation.pow2In.apply(this.targetColor.b, 1.0F, this.timer);
        timer = Math.max(0.0F, timer - Gdx.graphics.getDeltaTime());
        if (over) {
            this.duration -= Gdx.graphics.getDeltaTime();
            if (this.duration < 0.5F) {
                this.color.a = MathUtils.lerp(this.color.a, 0.0F, Gdx.graphics.getDeltaTime() * 5.0F);
            }
            if (this.duration < 1.0F) {
                this.x = Interpolation.pow2In.apply(this.x, MSG_VANISH_X, 1.0F - this.duration);
            }
//            this.color.a = Interpolation.exp10Out.apply(0.0F, 1.0F, this.duration / this.startingDuration);

            if (this.duration < 0.0F) this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, this.msg, this.x, this.y + this.offsetY, this.color, 1.25F);
    }

    public void dispose() {
    }

    static {
        STARTING_OFFSET_Y = 60.0F * Settings.scale;
        TARGET_OFFSET_Y = 100.0F * Settings.scale;
    }
}
