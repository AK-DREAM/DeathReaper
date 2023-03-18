package DeathReaper.powers;

import DeathReaper.DeathReaperCore;
import DeathReaper.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static DeathReaper.DeathReaperCore.makePowerPath;

public class DeathLinkPower extends AbstractDeathReaperPower {
    public AbstractCreature source;

    public static final String POWER_ID = DeathReaperCore.makeID("DeathLinkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public DeathLinkPower() {}

    public DeathLinkPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DeathLinkPowerPatch {
        public static void Prefix(AbstractPlayer p, DamageInfo info) {
            int amt = DeathReaperCore.getPowerAmount(info.owner, DeathLinkPower.POWER_ID);
            if (info.owner instanceof AbstractMonster && info.type == DamageInfo.DamageType.NORMAL
                    && info.output > 0 && amt > 0) {
                AbstractCreature mo = info.owner;
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(mo, p, new JudgementPower(mo, p, info.output*amt)));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }
}
