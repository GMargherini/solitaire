package piles;

import java.util.List;

public class DrawPile extends Pile{

    public DrawPile(List<Card> cards){
        super(cards);
    }

    public boolean isCardValid(Card card) {
        return true;
    }

}
