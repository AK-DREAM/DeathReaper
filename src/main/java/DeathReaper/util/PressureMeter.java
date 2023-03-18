//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package DeathReaper.util;

import DeathReaper.DeathReaperCore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import java.util.Set;

public class PressureMeter {
    private Texture body;
    private Texture meter;
    private Texture scytheLight, scytheDark;
    public int amount = 0;
    public int maxAmount = 10;
    private Hitbox hb;
    private float cX, cY;
    private float curProgress = 0.0F, tarProgress = 0.0F;
    private float timer = 0.0F, timer2 = 0.0F;
    private Color color;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public String name;
    public String description;
    public boolean moveToMiddle = false;


    public PressureMeter() {
        this.color = Color.WHITE.cpy();
        this.name = TEXT[0];
        this.description = TEXT[1];
        this.body = ImageMaster.loadImage("DeathReaperResources/images/ui/PressureMeter_Body.png");
        this.meter = ImageMaster.loadImage("DeathReaperResources/images/ui/PressureMeter_Meter.png");
        this.scytheLight = ImageMaster.loadImage("DeathReaperResources/images/ui/PressureMeter_Scythe_Light.png");
        this.scytheDark = ImageMaster.loadImage("DeathReaperResources/images/ui/PressureMeter_Scythe_Dark.png");
        this.hb = new Hitbox(346.0F * Settings.scale, 52.0F * Settings.scale);
        this.cX = 318.0F * Settings.scale;
        this.cY = 726.0F * Settings.scale;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            if (this.moveToMiddle) this.cX = 787.0F * Settings.scale;
            else this.cX = 318.0F * Settings.scale;
            if (AbstractDungeon.player != null) {
                this.hb.move(this.cX + this.hb.width / 2, this.cY + this.hb.height / 2);
            }
            this.hb.update();

            float dur = this.curProgress >= 0.99F ? 0.3F : 0.8F;
            float delta = Gdx.graphics.getDeltaTime();

            this.tarProgress = Math.min(1.0F, 1.0F*this.amount/this.maxAmount);
            this.curProgress = MathUtils.lerp(this.curProgress, this.tarProgress, Math.min(9.0F*delta, 1.0F));

            timer += delta;
            if (timer > dur) {
                timer = 0;
                AbstractDungeon.effectsQueue.add(new PressureMeterEffect(moveToMiddle));
            }
//            timer2 += delta;
//            if (timer2 > 0.1F) {
//                timer2 = 0;
//                if (this.curProgress > 0.01F && this.curProgress < 0.99F && !this.isMoving()) {
//                    float t = 346.0F * this.curProgress;
//                    AbstractDungeon.effectsQueue.add(new PressureMeterEffect2(this.cX + t * Settings.scale, this.hb.cY));
//                }
//            }
        }
    }

    public void render(SpriteBatch sb) {
        Float t = Settings.scale;
        if (AbstractDungeon.getCurrRoom().phase == RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            sb.setColor(this.color);
            int meterW = Math.round(346.0F*this.curProgress);
            sb.draw(this.meter, this.cX, this.cY, 0.0F, 0.0F, meterW, 52.0F, t, t, 0.0F, 107, 228, meterW, 52, false, false);
            String energyMsg = Math.min(this.amount, this.maxAmount) + "/" + this.maxAmount;
            if (this.amount > this.maxAmount) {
                energyMsg = energyMsg + " (+" + (this.amount-this.maxAmount) + ")";
            }
            AbstractDungeon.player.getEnergyNumFont().getData().setScale(0.7F);
            FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), energyMsg, this.cX+173.0F*t, this.cY+26.0F*t, this.color);
            sb.draw(this.body, this.cX, this.cY, 0.0F, 0.0F, 346.0F, 52.0F, t, t, 0.0F, 107, 228, 346, 52, false, false);
            sb.draw(this.curProgress>=0.99F?this.scytheLight:this.scytheDark, this.cX - 58.0F*t, this.cY - 35.0F*t, 0, 0, 107.0F, 122.0F, t, t, 0.0F, 59, 195, 107, 122, false, false);
            this.hb.render(sb);
            if (this.hb.hovered) {
                TipHelper.renderGenericTip(this.hb.cX + this.hb.width * 0.5F, this.hb.cY + this.hb.height * 0.3F, this.name, this.description);
            }
        }
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setMaxAmount(int amount) {
        this.maxAmount = amount;
    }
    public void addAmount(int amount) {
        this.amount += amount;
    }
    public void addMaxAmount(int amount) {
        this.maxAmount += amount;
    }

    public boolean isMoving() {
        return Math.abs(curProgress-tarProgress) > 0.01F;
    }

    public void reset(int maxAmount) {
        this.amount = 0;
        this.maxAmount = maxAmount;
    }


    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(DeathReaperCore.makeID("PressureMeter"));
        TEXT = uiStrings.TEXT;
    }
}
