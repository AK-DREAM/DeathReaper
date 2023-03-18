package DeathReaper.actions;

import DeathReaper.DeathReaperCore;
import DeathReaper.cards.AbstractDeathReaperCard;
import DeathReaper.powers.AbstractDeathReaperPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyExhumeAction extends MyMoveCardsAction {
    public static Consumer<List<AbstractCard>> triggerExhume = new Consumer<List<AbstractCard>>() {
        @Override
        public void accept(List<AbstractCard> list) {
            for (AbstractCard c : list) {
                if (c instanceof AbstractDeathReaperCard)
                    ((AbstractDeathReaperCard) c).onExhume();
                AbstractDungeon.player.powers.stream()
                        .filter(pw -> pw instanceof AbstractDeathReaperPower)
                        .forEach(pw -> {
                            ((AbstractDeathReaperPower)pw).onExhume(c);
                        });
            }
        }
    };
    public MyExhumeAction(int amount, boolean isRandom, Predicate<AbstractCard> predicate, Consumer<List<AbstractCard> > callback) {
        super(AbstractDungeon.player.hand, DeathReaperCore.graveyardPile, amount, isRandom,
                predicate, callback==null?triggerExhume:triggerExhume.andThen(callback)
        );
    }
    public MyExhumeAction(int amount, boolean isRandom, Predicate<AbstractCard> predicate) {
        this(amount, isRandom, predicate, null);
    }
    public MyExhumeAction(int amount, boolean isRandom) {
        this(amount, isRandom, card->true);
    }
    public MyExhumeAction(List<AbstractCard> toMove) {
        super(AbstractDungeon.player.hand, DeathReaperCore.graveyardPile, toMove, triggerExhume);
    }
}
