//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package DeathReaper.util;

import DeathReaper.characters.TheDeathReaper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PressureMeterEffect2 extends AbstractGameEffect {
    private AtlasRegion img;
    private float x, y, vX, vY, vY2, vS;
    private float startingDuration, targetScale;
    private float scaleJitter = 1.0F;

    public PressureMeterEffect2(float x, float y) {
        this.img = ImageMaster.GLOW_SPARK;
        this.startingDuration = MathUtils.random(0.6F, 0.8F);
        this.duration = this.startingDuration;
        this.color = new Color(0.75F, 0.25F, 0.25F, 0.0F);
        this.targetScale = MathUtils.random(0.8F, 1.0F) * Settings.scale;
        this.scaleJitter = MathUtils.random(this.scaleJitter - 0.1F, this.scaleJitter + 0.1F);
        this.x = x - (float)img.packedWidth / 2.0F;
        this.y = y - (float)img.packedHeight / 2.0F;
    }

    public void update() {
        this.scale = Interpolation.bounceIn.apply(this.targetScale, 0.1F, this.duration / this.startingDuration);
        this.rotation += this.vX * this.startingDuration * Gdx.graphics.getDeltaTime();
        this.color.a = this.duration / this.startingDuration * 0.5F;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (TheDeathReaper.pressureMeter.isMoving() || this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(img, this.x, this.y, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, this.scale*this.scaleJitter, this.scale*this.scaleJitter, this.rotation);
        sb.setBlendFunction(770, 771);
    }
    public void dispose() {
    }
}
