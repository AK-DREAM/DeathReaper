package DeathReaper.patches;

import DeathReaper.powers.JudgementPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "damage",
        paramtypez = {
                DamageInfo.class
        }
)
public class DamageToJudgementPatch {
    public static void Prefix(AbstractMonster mo, DamageInfo info) {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower("DeathReaper:DeathReaperPower")
        && info.owner == p && info.type == DamageInfo.DamageType.NORMAL) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(mo, p,
                    new JudgementPower(mo, p, info.output)));
            info.output = 0;
            info.type = DamageInfo.DamageType.THORNS;
        }
    }
}
