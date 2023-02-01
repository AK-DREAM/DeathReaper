package DeathReaper.patches;

import DeathReaper.DefaultMod;
import DeathReaper.cards.AbstractDeathReaperCard;
import DeathReaper.cards.Whisper;
import DeathReaper.characters.TheDeathReaper;
import DeathReaper.powers.AbstractDeathReaperPower;
import DeathReaper.powers.BuryNextCardPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.sql.Ref;

public class GraveyardPatches {
    @SpirePatch(clz = OverlayMenu.class, method = "update")
    public static class GraveyardPanelPatch1 {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(OverlayMenu __) { DefaultMod.graveyardPanel.updatePositions(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class GraveyardPanelPatch2 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(OverlayMenu __) { DefaultMod.graveyardPanel.show(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class GraveyardPanelPatch3 {
        @SpireInsertPatch(rloc = 5)
        public static void Insert(OverlayMenu __) { DefaultMod.graveyardPanel.hide(); }
    }
    @SpirePatch(clz = OverlayMenu.class, method = "render")
    public static class GraveyardPanelPatch4 {
        @SpireInsertPatch(rloc = 7)
        public static void Insert(OverlayMenu __, SpriteBatch sb) {
            DefaultMod.graveyardPanel.render(sb);
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = "update"
    )
    public static class UseCardActionBuryPatch {
        @SpireInsertPatch(rloc = 25)
        public static SpireReturn<Void> Insert(UseCardAction u) {
            AbstractCard card = ReflectionHacks.getPrivate(u, UseCardAction.class, "targetCard");
            AbstractPlayer p = AbstractDungeon.player;
            boolean toBury = false;
            if (card.type == AbstractCard.CardType.POWER || card.exhaust) {

            } else {
                if (p.hasPower(BuryNextCardPower.POWER_ID)) {
                    if (!(card instanceof Whisper) || p.getPower(BuryNextCardPower.POWER_ID).amount >= 2) {
                        toBury = true;
                        p.getPower(BuryNextCardPower.POWER_ID).flash();
                        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(p, p, BuryNextCardPower.POWER_ID, 1));
                    }
                }
            }
            if (card instanceof AbstractDeathReaperCard && ((AbstractDeathReaperCard) card).bury)
                toBury = true;
            if (toBury) {
                DefaultMod.moveToGraveyard(card);
                card.exhaustOnUseOnce = false;
                card.dontTriggerOnUseCard = false;
                AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
                ReflectionHacks.privateMethod(AbstractGameAction.class, "tickDuration", new Class[0]).invoke(u, new Object[0]);
                return SpireReturn.Return();
            } else return SpireReturn.Continue();
        }
    }
}
