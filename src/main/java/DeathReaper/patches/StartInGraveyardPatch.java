package DeathReaper.patches;

import DeathReaper.DeathReaperCore;
import DeathReaper.cards.AbstractDeathReaperCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

@SpirePatch(clz = CardGroup.class, method = "initializeDeck")
public class StartInGraveyardPatch {
    @SpireInsertPatch(rloc = 4, localvars = {"copy"})
    public static void Insert(CardGroup __instance, CardGroup masterDeck, CardGroup copy) {
        ArrayList<AbstractCard> moveToGraveyard = new ArrayList();
        for (AbstractCard c : copy.group) {
            if (c instanceof AbstractDeathReaperCard && ((AbstractDeathReaperCard) c).startInGraveyard) {
                moveToGraveyard.add(c);
            }
        }
        for (AbstractCard c : moveToGraveyard) {
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    DeathReaperCore.moveToGraveyard(AbstractDungeon.player.drawPile, c);
                    this.isDone = true;
                }
            });
        }
    }
}
