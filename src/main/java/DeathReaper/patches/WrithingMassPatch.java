package DeathReaper.patches;

import DeathReaper.characters.TheDeathReaper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ReactivePower;

@SpirePatch(clz = ReactivePower.class, method = "onAttacked")
public class WrithingMassPatch {
    public static void Prefix(ReactivePower pw, DamageInfo info, int amount) {
        if (info.owner != null) System.out.println(info.owner.name + ' ' + info.type);
        if (info.owner instanceof TheDeathReaper && info.type == DamageInfo.DamageType.NORMAL && amount == 0) {
            pw.flash();
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction((AbstractMonster)pw.owner));
        }
    }
}
