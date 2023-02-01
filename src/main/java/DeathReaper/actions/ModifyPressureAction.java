package DeathReaper.actions;

import DeathReaper.DefaultMod;
import DeathReaper.characters.TheDeathReaper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;

public class ModifyPressureAction extends AbstractGameAction {
    public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(DefaultMod.makeID("PressurePower")).DESCRIPTIONS[1];
    private boolean add;
    public ModifyPressureAction(int amount, boolean add) {
        this.amount = amount;
        this.add = add;
        this.actionType = ActionType.POWER;
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof TheDeathReaper && this.startDuration == this.duration) {
            if (add) {
                TheDeathReaper.addPressure(this.amount);
                if (p.hasPower("DeathReaper:DeathReaperPower")) {
                    p.getPower("DeathReaper:DeathReaperPower").flash();
                }
                AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, "+" + this.amount + " " + NAME));
            } else TheDeathReaper.setPressure(this.amount);

            if (TheDeathReaper.getPressure() >= TheDeathReaper.getMaxPressure()+4) {
                this.addToBot(new CheckMaxPressureAction());
            }
        }
        this.tickDuration();
    }
}
