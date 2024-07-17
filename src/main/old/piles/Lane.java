package piles;

import java.util.List;

public class Lane extends Pile implements Iterable<Card>{
    public Lane(List<Card> cards){
        super(cards);
    }

    public boolean isCardValid(Card card) {
        boolean valid = false;
        if(getTopCard() != null){
            valid = getTopCard().getColor() != card.getColor() &&
                    getTopCard().isNext(card);
        }else if(card.getRank()== Rank.KING){
            valid=true;
        }
        return valid;
    }
}
