package piles;

import deck.Card;
import deck.Rank;
import deck.Suit;

public class SuitPile extends Pile{
    private Suit suit;
    public SuitPile(Suit suit){
        this.suit=suit;
    }
    public boolean isCardValid(Card card) {
        boolean valid;
        if(getTopCard() != null){
            valid = card.getSuit() == this.suit &&
                    getTopCard().isPrevious(card);
        }else{
            valid = card.getRank() == Rank.ACE &&
                    card.getSuit() == this.suit;
        }
        return valid;
    }
}
