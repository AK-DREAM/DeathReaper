package DeathReaper.actions;

import DeathReaper.DeathReaperCore;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.powers.AbstractDeathReaperPower;
import DeathReaper.powers.ReaperFormPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

public class ModifyMaxPressureAction extends AbstractGameAction {
    public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(DeathReaperCore.makeID("PressurePower")).DESCRIPTIONS[2];
    private boolean manual;
    public ModifyMaxPressureAction(int amount, boolean manual) {
        this.actionType = ActionType.POWER;
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
        this.amount = amount;
        this.manual = manual;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof TheDeathReaper && this.startDuration == this.duration) {
            if (this.manual) {
                if (this.amount == 0) { this.isDone = true; return; }
            } else {
                int amt = TheDeathReaper.getPressure(), maxAmt = TheDeathReaper.getMaxPressure();
                int addOne = p.hasPower(ReaperFormPower.POWER_ID)?2:3;
                this.amount = Math.max((amt-maxAmt)/addOne, 0);
                if (this.amount == 0) { this.isDone = true; return; }
                TheDeathReaper.addPressure(-(addOne-1)*this.amount);
            }
            AbstractDungeon.player.powers.stream()
                    .filter(pw -> pw instanceof AbstractDeathReaperPower)
                    .forEach(pw -> {
                        ((AbstractDeathReaperPower)pw).onMaxPressureIncrease(this.amount);
                    });
            TheDeathReaper.addMaxPressure(amount);
            AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, "+" + amount + " " + NAME));
        }
        this.tickDuration();
    }
}
