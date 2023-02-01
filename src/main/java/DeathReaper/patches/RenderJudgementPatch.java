package DeathReaper.patches;

import DeathReaper.characters.TheDeathReaper;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "renderHealth"
)
public class RenderJudgementPatch {
    private static float HEALTH_BAR_HEIGHT = -1.0F;

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"x", "y"}
    )
    public static void Insert(AbstractCreature mo, SpriteBatch sb, float x, float y) {
        if (HEALTH_BAR_HEIGHT == -1.0F) {
            HEALTH_BAR_HEIGHT = 20.0F * Settings.scale;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof TheDeathReaper &&
                ((TheDeathReaper) p).calcJudgementDamage(mo) > 0 && mo.hbAlpha > 0.0F) {
            renderTempHPIconAndValue(mo, sb, x, y);
        }
    }

    private static void renderTempHPIconAndValue(AbstractCreature mo, SpriteBatch sb, float x, float y) {
        float X = ReflectionHacks.getPrivate(mo, AbstractCreature.class, "BLOCK_ICON_X");
        float Y = ReflectionHacks.getPrivate(mo, AbstractCreature.class, "BLOCK_ICON_Y");
        Texture JudgementIcon = ImageMaster.loadImage("DeathReaperResources/images/ui/JudgementIcon.png");
        sb.setColor(Color.WHITE.cpy());
        sb.draw(JudgementIcon, x + X - 16.0F + mo.hb.width, y + Y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont,
                Integer.toString(((TheDeathReaper)AbstractDungeon.player).calcJudgementDamage(mo)),
                x + X + 16.0F + mo.hb.width, y - 16.0F * Settings.scale, Settings.CREAM_COLOR, 1.0F);
    }

    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new FieldAccessMatcher(AbstractCreature.class, "currentBlock");
            ArrayList<Matcher> matchers = new ArrayList();
            matchers.add(finalMatcher);
            return LineFinder.findInOrder(ctBehavior, matchers, finalMatcher);
        }
    }
}
