package DeathReaper.patches;

import DeathReaper.DeathReaperCore;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.OverlayMenu;

public class GraveyardPanelPatch {
    @SpirePatch(clz = OverlayMenu.class, method = "update")
    public static class GraveyardPanelPatch1 {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(OverlayMenu __) { DeathReaperCore.graveyardPanel.updatePositions(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class GraveyardPanelPatch2 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(OverlayMenu __) { DeathReaperCore.graveyardPanel.show(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class GraveyardPanelPatch3 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(OverlayMenu __) { DeathReaperCore.graveyardPanel.hide(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class GraveyardPanelPatch4 {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(OverlayMenu __, SpriteBatch sb) {
            DeathReaperCore.graveyardPanel.render(sb);
        }
    }
}
