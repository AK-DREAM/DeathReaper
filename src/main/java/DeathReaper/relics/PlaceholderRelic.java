package DeathReaper.relics;

import DeathReaper.actions.ModifyMaxPressureAction;
import DeathReaper.actions.ModifyPressureAction;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import DeathReaper.DeathReaperCore;
import DeathReaper.util.TextureLoader;

import static DeathReaper.DeathReaperCore.makeRelicOutlinePath;
import static DeathReaper.DeathReaperCore.makeRelicPath;

public class PlaceholderRelic extends CustomRelic {

    public static final String ID = DeathReaperCore.makeID("PlaceholderRelic");

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    public PlaceholderRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        this.addToBot(new ModifyPressureAction(4, true));
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
