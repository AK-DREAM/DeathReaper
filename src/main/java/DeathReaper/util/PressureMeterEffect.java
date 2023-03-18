package DeathReaper.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.Set;

public class PressureMeterEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 0.2F;

    private float effectDuration, x, y, vY, rotator;
    private float scaleJitter = 1.0F;
    private static final float SCALE_JITTER_AMT = 0.1F;
    private Color shadowColor = Color.BLACK.cpy();
    private TextureAtlas.AtlasRegion img;
    private boolean isAdditive;

    public PressureMeterEffect(boolean moveToMiddle) {
        this.img = getImg();
        setPosition();
        this.x -= (this.img.packedWidth / 2.0F);
        this.y -= (this.img.packedHeight / 2.0F);
        this.x += MathUtils.random(-5.0F*Settings.scale, 5.0F*Settings.scale);
        this.y += MathUtils.random(-5.0F*Settings.scale, 5.0F*Settings.scale);

        if (moveToMiddle) this.x += 469.0F * Settings.scale;

        this.effectDuration = MathUtils.random(0.8F, 1.8F);
        this.duration = this.effectDuration;
        this.startingDuration = this.effectDuration;
        this.scaleJitter = MathUtils.random(this.scaleJitter - 0.1F, this.scaleJitter + 0.1F);
        this.scale = 0.3F*Settings.scale;
        this.vY = MathUtils.random(30.0F * Settings.scale, 60.0F * Settings.scale);
        this.color = new Color(1.0F, 1.0F, 1.0F, 0.0F);
        this.color.g -= MathUtils.random(0.7F);
        this.color.b -= this.color.g - MathUtils.random(0.7F);
        if (this.color.b < 0) this.color.b = 0;
        this.color.a = 0.5F;
        this.isAdditive = MathUtils.randomBoolean();
        this.rotator = 0.0F;
    }

    private TextureAtlas.AtlasRegion getImg() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            return ImageMaster.FLAME_1;
        } else if (roll == 1) {
            return ImageMaster.FLAME_2;
        } else {
            return ImageMaster.FLAME_3;
        }
    }

    private void setPosition() {
        int roll = MathUtils.random(0, 10);
        switch (roll) {
            case 0:
                this.x = 371.0F * Settings.scale;
                this.y = 804.0F * Settings.scale;
                return;
            case 1:
                this.x = 353.0F * Settings.scale;
                this.y = 807.0F * Settings.scale;
                return;
            case 2:
                this.x = 334.0F * Settings.scale;
                this.y = 800.0F * Settings.scale;
                return;
            case 3:
                this.x = 314.0F * Settings.scale;
                this.y = 801.0F * Settings.scale;
                return;
            case 4:
                this.x = 308.0F * Settings.scale;
                this.y = 784.0F * Settings.scale;
                return;
            case 5:
                this.x = 291.0F * Settings.scale;
                this.y = 786.0F * Settings.scale;
                return;
            case 6:
                this.x = 301.0F * Settings.scale;
                this.y = 765.0F * Settings.scale;
                return;
            case 7:
                this.x = 312.0F * Settings.scale;
                this.y = 753.0F * Settings.scale;
                return;
            case 8:
                this.x = 320.0F * Settings.scale;
                this.y = 734.0F * Settings.scale;
                return;
            case 9:
                this.x = 333.0F * Settings.scale;
                this.y = 721.0F * Settings.scale;
                return;
            default:
                this.x = 340.0F * Settings.scale;
                this.y = 704.0F * Settings.scale;
        }
    }

    public void update() {
        this.rotation += this.rotator * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.2F)
            this.scale = (Settings.scale * this.duration / this.effectDuration * 2.0F + Settings.scale / 2.0F) * 0.1F;
        if (this.duration < this.startingDuration / 2.0F) {
            this.color.a = this.duration / (this.startingDuration / 2.0F) * 0.5F;
        }
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        }
    }

    public void render(SpriteBatch sb) {
        if (this.isAdditive)
            sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw((TextureRegion)this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * this.scaleJitter, this.scale * this.scaleJitter * 0.7F, this.rotation);
        if (this.isAdditive)
            sb.setBlendFunction(770, 771);
    }

    public void dispose() {}

}
