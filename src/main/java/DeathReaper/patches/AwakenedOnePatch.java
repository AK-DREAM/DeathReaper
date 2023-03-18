package DeathReaper.patches;

import DeathReaper.powers.JudgementPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

@SpirePatch(clz = AwakenedOne.class, method = "damage")
public class AwakenedOnePatch {
    @SpireInsertPatch(rloc = 343-312)
    public static void Insert(AwakenedOne m) {
        m.powers.removeIf(pw -> JudgementPower.POWER_ID.equals(pw.ID));
    }
}
