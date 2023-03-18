package DeathReaper.actions;

import DeathReaper.characters.TheDeathReaper;
import DeathReaper.powers.AbstractDeathReaperPower;
import DeathReaper.powers.JudgementPower;
import DeathReaper.powers.UnfinishedReckoningPower;
import DeathReaper.util.ReckoningEffect;
import DeathReaper.util.ShowPrefixEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReckoningAction extends AbstractGameAction {
    public ArrayList<AbstractDeathReaperPower> prefix = new ArrayList<>();
    public ArrayList<AbstractGameAction> actionQueue = new ArrayList<>();
    public AbstractPlayer p;
    public float nowX, nowY;
    public boolean DoNotReducePressure = false;
    public boolean SkipJudgement = false;
    public int DamageTimes = 1;

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
        if (!prefix.isEmpty()) {
            Collections.sort(prefix, new Comparator<AbstractDeathReaperPower>() {
                @Override
                public int compare(AbstractDeathReaperPower o1, AbstractDeathReaperPower o2) {
                    return o1.prefixPriority-o2.prefixPriority;
                }
            });
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

                actionQueue.add(new MyWaitAction(0.2F));
                pw.onTrigger(this);
                actionQueue.add(new MyWaitAction(0.4F));
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
            actionQueue.add(new MyWaitAction(0.75F));
            if (!SkipJudgement) {
                actionQueue.add(new AbstractGameAction() {
                    @Override
                    public void update() {
                        int tot = AbstractDungeon.getMonsters().monsters.size();
                        int damage[] = new int[tot];
                        for (int i = 0; i < tot; i++) {
                            AbstractMonster mo = AbstractDungeon.getMonsters().monsters.get(i);
                            this.addToTop(new RemoveSpecificPowerAction(mo, p, JudgementPower.POWER_ID));
                            damage[i] = TheDeathReaper.calcJudgementDamage(mo);
                        }
                        boolean isFast = ReckoningAction.this.DamageTimes > 1;
                        for (int i = 0; i < ReckoningAction.this.DamageTimes; i++) {
                            this.addToTop(new DamageAllEnemiesAction(p, damage, DamageInfo.DamageType.THORNS, AttackEffect.SLASH_HEAVY, isFast));
                        }
                        this.isDone = true;
                    }
                });
            }
            actionQueue.add(new AbstractGameAction() {
                @Override
                public void update() {
                    int blk = TheDeathReaper.getRealPressure();
                    if (blk > 0) this.addToTop(new GainBlockAction(p, blk));
                    this.isDone = true;
                }
            });
            actionQueue.add(new MyWaitAction(0.5F));
            if (!DoNotReducePressure) {
                actionQueue.add(new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.addToTop(new ModifyPressureAction(Math.max(0, TheDeathReaper.getPressure()/2), false));
                        this.isDone = true;
                    }
                });
            }
            for (int i = actionQueue.size()-1; i >= 0; i--) {
                this.addToTop(actionQueue.get(i));
            }
        }
        this.tickDuration();
    }
}
