package DeathReaper.actions;

import DeathReaper.DefaultMod;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.powers.AbstractDeathReaperPower;
import DeathReaper.util.ReckoningEffect;
import DeathReaper.util.ShowPrefixEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ReckoningAction extends AbstractGameAction {
    public ArrayList<AbstractDeathReaperPower> prefix = new ArrayList<>();
    public ArrayList<AbstractGameAction> actionQueue = new ArrayList<>();
    public AbstractPlayer p;
    public float nowX, nowY;

    public ReckoningAction() {
        p = AbstractDungeon.player;
        this.startDuration = this.duration = 0.1F;
        nowX = 820.0F*Settings.scale;
        nowY = 640.0F*Settings.scale;

        for (AbstractPower pw : p.powers) {
            if (pw instanceof AbstractDeathReaperPower && ((AbstractDeathReaperPower) pw).isPrefix) {
                prefix.add((AbstractDeathReaperPower) pw);
            }
        }
    }

    public void update() {
        if (this.duration == this.startDuration) {
            for (int prefixCnt = 0; prefixCnt < prefix.size(); ++prefixCnt) {
                AbstractDeathReaperPower pw = prefix.get(prefixCnt);
                if (prefixCnt == 6) {
                    nowX = 1070.0F * Settings.scale;
                    nowY = 640.0F * Settings.scale;
                }
                if (prefixCnt > 10) {
                    nowX = MathUtils.random(820.0F, 1070.0F);
                    nowY = MathUtils.random(630.0F, 930.0F);
                }
                actionQueue.add(new VFXAction(new ShowPrefixEffect(nowX, nowY, pw.adjective, 1.5F)));
                actionQueue.add(new RemoveSpecificPowerAction(p, p, pw.ID));
                pw.onTrigger(this);
                actionQueue.add(new MyWaitAction(0.5F));
                nowY += 75.0F * Settings.scale;
            }
            actionQueue.add(new VFXAction(p, new ReckoningEffect(1.5F), 0.0F, true));
            actionQueue.add(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractGameEffect i : AbstractDungeon.effectList) {
                        if (i instanceof ShowPrefixEffect) ((ShowPrefixEffect) i).over = true;
                    }
                    this.isDone = true;
                }
            });
            actionQueue.add(new MyWaitAction(1.0F));
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                if (mo.hasPower("DeathReaper:JudgementPower")) {
                    int dmg = ((TheDeathReaper)p).calcJudgementDamage(mo);
                    actionQueue.add(new DamageAction(mo, new DamageInfo(null, dmg, DamageInfo.DamageType.THORNS), AttackEffect.SLASH_HEAVY));
                    actionQueue.add(new RemoveSpecificPowerAction(mo, p, "DeathReaper:JudgementPower"));
                }
            }
            int blk = ((TheDeathReaper)p).calcReckoningBlock();
            if (blk > 0) actionQueue.add(new GainBlockAction(p, blk));
            actionQueue.add(new MyWaitAction(0.5F));
            actionQueue.add(new ModifyPressureAction(TheDeathReaper.getRealPressure()/2, false));
            for (int i = actionQueue.size()-1; i >= 0; i--) {
                this.addToTop(actionQueue.get(i));
            }
        }
        this.tickDuration();
    }
}
