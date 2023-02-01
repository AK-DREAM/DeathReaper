package DeathReaper.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MySelectCardsInHandAction extends AbstractGameAction {
    private Predicate<AbstractCard> predicate;
    private Consumer<List<AbstractCard>> callback;
    private String text;
    private boolean anyNumber;
    private boolean canPickZero;
    private boolean returnToHand;
    private boolean isRandom;
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;

    public MySelectCardsInHandAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, boolean returnToHand, boolean isRandom, Predicate<AbstractCard> cardFilter, Consumer<List<AbstractCard>> callback) {
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.text = textForSelect;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.returnToHand = returnToHand;
        this.isRandom = isRandom;
        this.predicate = cardFilter;
        this.callback = callback;
        this.hand = AbstractDungeon.player.hand.group;
        this.tempHand = new ArrayList<AbstractCard>();
        this.tempHand.addAll(this.hand);
    }

    public MySelectCardsInHandAction(int amount, String textForSelect, boolean anyNumber, boolean canPickZero, boolean returnToHand, boolean isRandom, Consumer<List<AbstractCard>> callback) {
        this(amount, textForSelect, anyNumber, canPickZero, returnToHand, isRandom, card->true, callback);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.hand.stream().noneMatch(this.predicate) || this.callback == null) {
                this.isDone = true;
                return;
            }
            if (this.isRandom) {
                List<AbstractCard> list = this.hand.stream().filter(this.predicate).collect(Collectors.toList());
                Collections.shuffle(list);
                this.amount = Math.min(this.amount, list.size());
                this.callback.accept(list.stream().limit(this.amount).collect(Collectors.toList()));
                this.isDone = true;
            } else if (this.hand.stream().filter(this.predicate).count() <= (long)this.amount && !this.anyNumber && !this.canPickZero) {
                this.callback.accept(this.hand.stream().filter(this.predicate).collect(Collectors.toList()));
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
                this.isDone = true;
            } else {
                this.tempHand.removeIf(this.predicate);
                if (this.tempHand.size() > 0) {
                    this.hand.removeIf(c -> this.tempHand.contains(c));
                }
                AbstractDungeon.handCardSelectScreen.open(this.text, this.amount, this.anyNumber, this.canPickZero);
                this.tickDuration();
            }
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            this.callback.accept(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            if (this.returnToHand) {
                this.hand.addAll(AbstractDungeon.handCardSelectScreen.selectedCards.group);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            if (this.tempHand.size() > 0) {
                this.hand.addAll(this.tempHand);
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            this.isDone = true;
            return;
        }
        this.tickDuration();
    }
}