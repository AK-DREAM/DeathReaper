package DeathReaper.util;

import DeathReaper.DeathReaperCore;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class BuryCardEffect extends AbstractGameEffect {
    private AbstractCard c;

    public BuryCardEffect(AbstractCard c) {
        this.duration = 1.0F;
        this.c = c;
    }

    public void update() {
        if (this.duration == 1.0F) {
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);
            int i;
            for(i = 0; i < 90; ++i) {
                ExhaustBlurEffect tmp = new ExhaustBlurEffect(this.c.current_x, this.c.current_y+200.0F);
                Color newColor = Color.BLACK.cpy();
                newColor.r = MathUtils.random(0.1F, 0.4F);
                newColor.g = newColor.r;
                newColor.b = newColor.r + 0.25F;
                ReflectionHacks.setPrivate(tmp, AbstractGameEffect.class, "color", newColor);
                ReflectionHacks.setPrivate(tmp, ExhaustBlurEffect.class, "vY", MathUtils.random(-5.0F, -1.0F));
                AbstractDungeon.effectsQueue.add(tmp);
            }
            for(i = 0; i < 50; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.c.current_x, this.c.current_y));
            }
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (!this.c.fadingOut && this.duration < 0.7F && !AbstractDungeon.player.hand.contains(this.c)) {
            DeathReaperCore.logger.info(c.cardID + "!!!!!!!!!");
            this.c.fadingOut = true;
        }
        this.c.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.c.resetAttributes();
        }

    }

    public void render(SpriteBatch sb) {
        this.c.render(sb);
    }

    public void dispose() {
    }
}
