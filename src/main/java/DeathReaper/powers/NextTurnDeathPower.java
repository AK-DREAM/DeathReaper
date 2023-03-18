package DeathReaper.powers;

import DeathReaper.DeathReaperCore;
import DeathReaper.util.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import static DeathReaper.DeathReaperCore.makePowerPath;

public class NextTurnDeathPower extends AbstractDeathReaperPower {
    public static final String POWER_ID = DeathReaperCore.makeID("NextTurnDeathPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

//    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
//    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public NextTurnDeathPower() {}
    public NextTurnDeathPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("end_turn_death");

        this.updateDescription();
    }

    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new VFXAction(new WeightyImpactEffect(this.owner.hb.cX, this.owner.hb.cY, Color.RED.cpy())));
        this.addToBot(new WaitAction(0.5F));
        this.addToBot(new LoseHPAction(this.owner, this.owner, 99999));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

}