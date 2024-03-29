package DeathReaper.powers;

import DeathReaper.DeathReaperCore;
import DeathReaper.actions.ReckoningAction;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static DeathReaper.DeathReaperCore.makePowerPath;


public class AbsorbingReckoningPower extends AbstractDeathReaperPower {
    public AbstractCreature source;

    public static final String POWER_ID = DeathReaperCore.makeID("AbsorbingReckoningPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public AbsorbingReckoningPower() {}

    public AbsorbingReckoningPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.source = source;
        this.amount = amount;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.isPrefix = true;
        this.adjective = DESCRIPTIONS[2];

        updateDescription();
    }

    @Override
    public void onTrigger(ReckoningAction action) {
        action.actionQueue.add(new AbstractGameAction() {
            @Override
            public void update() {
                this.addToTop(new HealAction(AbsorbingReckoningPower.this.owner, AbsorbingReckoningPower.this.owner,
                        AbsorbingReckoningPower.this.amount*TheDeathReaper.getRealPressure()));
                this.isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
