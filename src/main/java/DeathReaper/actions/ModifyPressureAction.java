package DeathReaper.actions;

import DeathReaper.DeathReaperCore;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.powers.DeathReaperPower;
import DeathReaper.powers.NoMorePressurePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

public class ModifyPressureAction extends AbstractGameAction {
    public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(DeathReaperCore.makeID("PressurePower")).DESCRIPTIONS[1];
    private boolean add, toMaxAmount;
    public ModifyPressureAction(int amount, boolean add, boolean toMaxAmount) {
        this.amount = amount;
        this.add = add;
        this.toMaxAmount = toMaxAmount;
        this.actionType = ActionType.POWER;
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
    }
    public ModifyPressureAction(int amount, boolean add) {
        this(amount, add, false);
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof TheDeathReaper && this.startDuration == this.duration) {
            if (this.toMaxAmount) {
                this.add = false; this.amount = TheDeathReaper.getMaxPressure();
            }
            if (p.hasPower(NoMorePressurePower.POWER_ID)) {
                if ((this.add && this.amount > 0) || !this.add && this.amount > TheDeathReaper.getPressure()) {
                    p.getPower(NoMorePressurePower.POWER_ID).flash();
                    this.isDone = true; return;
                }
            }
            if (add) {
                TheDeathReaper.addPressure(this.amount);
                if (p.hasPower(DeathReaperPower.POWER_ID)) {
                    p.getPower(DeathReaperPower.POWER_ID).flash();
                }
                AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, "+" + this.amount + " " + NAME));
            } else {
                TheDeathReaper.setPressure(this.amount);
            }
            this.addToTop(new ModifyMaxPressureAction(0, false));
        }
        this.tickDuration();
    }
}
