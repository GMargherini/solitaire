package deck;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private final Random random;

    public Deck(){
        random = new Random();
        cards=new LinkedList<>();
        for(Suit suit : Suit.values()){
            for(Rank rank : Rank.values()){
                Card newCard = new Card(rank, suit);
                cards.add(newCard);
            }
        }
    }
    public Card pickRandomCard(){
        Card card = cards.get(random.nextInt(cards.size()));
        cards.remove(card);
        return card;
    }
    public List<Card> getCards(int numberOfCards) {
        LinkedList<Card> cards = new LinkedList<>();
        for(int i=0;i<numberOfCards;i++){
            cards.add(pickRandomCard());
        }
        return cards;
    }

    public int getDeckSize() {
        return cards.size();
    }
}
