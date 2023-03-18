package DeathReaper.patches;

import DeathReaper.DeathReaperCore;
import DeathReaper.cards.AbstractDeathReaperCard;
import DeathReaper.cards.Tombstone;
import DeathReaper.powers.GodOfDeathPower;
import DeathReaper.powers.TombstonePower;
import basemod.ReflectionHacks;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = UseCardAction.class,
        method = "update"
)
public class UseCardActionBuryPatch {
    @SpireInsertPatch(rloc = 25)
    public static SpireReturn<Void> Insert(UseCardAction u) {
        AbstractCard card = ReflectionHacks.getPrivate(u, UseCardAction.class, "targetCard");
        AbstractPlayer p = AbstractDungeon.player;
        boolean toBury = false;
        if (card.type == AbstractCard.CardType.POWER && !card.cardID.equals(Tombstone.ID) && p.hasPower(TombstonePower.POWER_ID)) {
            p.getPower(TombstonePower.POWER_ID).flash();
            toBury = true;
        }
        if (card instanceof AbstractDeathReaperCard && ((AbstractDeathReaperCard) card).bury)
            toBury = true;
        if (card.exhaust || u.exhaustCard || CardModifierManager.hasModifier(card, ExhaustMod.ID))
            toBury = false;
        if (p.hasPower(GodOfDeathPower.POWER_ID)) {
            card.modifyCostForCombat(-99);
            toBury = false;
        }
        if (toBury) {
            DeathReaperCore.moveToGraveyard(AbstractDungeon.player.hand, card);
            card.exhaustOnUseOnce = false;
            card.dontTriggerOnUseCard = false;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
            ReflectionHacks.privateMethod(AbstractGameAction.class, "tickDuration", new Class[0]).invoke(u, new Object[0]);
            return SpireReturn.Return();
        } else return SpireReturn.Continue();
    }
}
