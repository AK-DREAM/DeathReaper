package DeathReaper.actions;

import DeathReaper.DeathReaperCore;
import DeathReaper.util.BuryCardEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class ShowCardAndBuryEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.5F;
    private AbstractCard card;
    private static final float PADDING;

    public ShowCardAndBuryEffect(AbstractCard card) {
        this.card = card;
        this.duration = 0.5F;
        this.identifySpawnLocation((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F);
        AbstractDungeon.effectsQueue.add(new CardPoofEffect(card.target_x, card.target_y));
        card.drawScale = 0.01F;
        card.targetDrawScale = 1.0F;
        if (card.type != CardType.CURSE && card.type != CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            card.upgrade();
        }
        DeathReaperCore.graveyardPile.addToTop(card);
    }

    private void identifySpawnLocation(float x, float y) {
        int effectCount = 0;

        for (AbstractGameEffect e : AbstractDungeon.effectList) {
            if (e instanceof ShowCardAndBuryEffect) {
                ++effectCount;
            }
        }
        System.out.println(effectCount);
        this.card.target_y = (float)Settings.HEIGHT * 0.5F;
        switch(effectCount) {
            case 0:
                this.card.target_x = (float)Settings.WIDTH * 0.5F;
                break;
            case 1:
                this.card.target_x = (float)Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH;
                break;
            case 2:
                this.card.target_x = (float)Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH;
                break;
            case 3:
                this.card.target_x = (float)Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            case 4:
                this.card.target_x = (float)Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
                break;
            default:
                this.card.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
                this.card.target_y = MathUtils.random((float)Settings.HEIGHT * 0.2F, (float)Settings.HEIGHT * 0.8F);
        }

    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
            AbstractDungeon.effectsQueue.add(new BuryCardEffect(this.card));
        }
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.card.render(sb);
        }
    }

    public void dispose() {
    }

    static {
        PADDING = 30.0F * Settings.scale;
    }
}