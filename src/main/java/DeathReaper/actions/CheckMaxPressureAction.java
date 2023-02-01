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

import java.util.Iterator;

public class CheckMaxPressureAction extends AbstractGameAction {
    public static final String NAME = CardCrawlGame.languagePack.getPowerStrings(DefaultMod.makeID("PressurePower")).DESCRIPTIONS[2];

    public CheckMaxPressureAction() {
        this.actionType = ActionType.POWER;
        this.startDuration = this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof TheDeathReaper && this.startDuration == this.duration) {
            int amt = TheDeathReaper.getPressure(), maxAmt = TheDeathReaper.getMaxPressure();
            if (amt < maxAmt+4) { this.isDone = true; return; }
            int tmp = (amt-maxAmt)/4;
            TheDeathReaper.addPressure(-3*tmp);
            TheDeathReaper.addMaxPressure(tmp);
            AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, "+" + tmp + " " + NAME));
        }
        this.tickDuration();
    }
}
