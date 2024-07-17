package piles;

import core.InvalidMoveException;

public class UncoverdPile extends Pile{

    @Override
    public void addCard(Card card) throws InvalidMoveException {
        super.addCard(card);
        card.flip();
    }

    public boolean isCardValid(Card card) {
        return card.isCovered();
    }
}
