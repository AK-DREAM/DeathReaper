package DeathReaper.actions;

import DeathReaper.DefaultMod;
import DeathReaper.characters.TheDeathReaper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

public class MyWaitAction extends AbstractGameAction {

    public MyWaitAction(float duration) {
        this.startDuration = this.duration = duration;
    }

    public void update() {
        this.tickDuration();
        DefaultMod.logger.info(this.startDuration + " " + this.duration);
    }
}
