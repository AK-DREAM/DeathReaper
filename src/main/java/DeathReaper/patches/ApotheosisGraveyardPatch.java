package DeathReaper.patches;

import DeathReaper.DeathReaperCore;
import DeathReaper.powers.JudgementPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(clz = ApotheosisAction.class, method = "update")
public class ApotheosisGraveyardPatch {
    @SpireInsertPatch(rloc = 1)
    public static void Insert(ApotheosisAction action) {
       for (AbstractCard c : DeathReaperCore.graveyardPile.group) {
           if (c.canUpgrade()) {
               c.upgrade(); c.applyPowers();
           }
       }
    }
}
