package DeathReaper.powers;

import DeathReaper.actions.ReckoningAction;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractDeathReaperPower extends AbstractPower {
    public boolean isPrefix = false;
    public String adjective = "";
    public int prefixPriority = 0;
    public Color textColor;

    public void onTrigger(ReckoningAction action) {}

    public void onExhume(AbstractCard c) {}

    public void onBury(AbstractCard c) {}

    public void onMaxPressureIncrease(int amount) {}
}
