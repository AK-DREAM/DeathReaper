package DeathReaper.cards;
import basemod.abstracts.CustomCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDeathReaperCard extends CustomCard {

    // Custom Abstract Cards can be a bit confusing. While this is a simple base for simply adding a second magic number,
    // if you're new to modding I suggest you skip this file until you know what unique things that aren't provided
    // by default, that you need in your own cards.

    // In this example, we use a custom Abstract Card in order to define a new magic number. From here on out, we can
    // simply use that in our cards, so long as we put "extends AbstractDynamicCard" instead of "extends CustomCard" at the start.
    // In simple terms, it's for things that we don't want to define again and again in every single card we make.

    public boolean bury;
    public boolean startInGraveyard;

    public AbstractDeathReaperCard(final String id,
                                   final String name,
                                   final String img,
                                   final int cost,
                                   final String rawDescription,
                                   final CardType type,
                                   final CardColor color,
                                   final CardRarity rarity,
                                   final CardTarget target) {

        super(id, name, img, cost, rawDescription, type, color, rarity, target);

        // Set all the things to their default values.
        isCostModified = false;
        isCostModifiedForTurn = false;
        isDamageModified = false;
        isBlockModified = false;
        isMagicNumberModified = false;
    }

    public void onBury() {}

    public void onExhume() {}

    public void onOtherCardsBury(AbstractCard c) {}

    public static class Enums {
        @SpireEnum
        public static CardTags PREFIX;
    }
}