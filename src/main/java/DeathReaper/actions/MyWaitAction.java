package DeathReaper.actions;

import DeathReaper.DeathReaperCore;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class MyWaitAction extends AbstractGameAction {

    public MyWaitAction(float duration) {
        this.startDuration = this.duration = duration;
    }

    public void update() {
        this.tickDuration();
    }
}
