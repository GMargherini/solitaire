package piles;

import deck.Card;
import core.InvalidMoveException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static core.Constants.WHITE_TEXT;

abstract public class Pile implements Iterable<Card>{
    protected List<Card> cards;
    public Pile(){
        cards = new LinkedList<>();
    }
    public Pile(List<Card> cards){
        this.cards=cards;
    }

    public Card getTopCard(){
        if(!cards.isEmpty()){
            return cards.get(cards.size()-1);
        }
        else{
            return null;
        }
    }
    public void addCard(Card card) throws InvalidMoveException {
        if (this.isCardValid(card)){
            cards.add(card);
        }
        else{
            throw new InvalidMoveException();
        }
    }
    public void removeCard(Card card) throws InvalidMoveException{
        if(!cards.remove(card)) {
            throw new InvalidMoveException();
        }
    }

    public void addCards(List<Card> cards) throws InvalidMoveException {
        for (Card card : cards){
            this.addCard(card);
        }
    }

    public void removeCards(List<Card> cards) throws InvalidMoveException {
        for (Card card : cards){
            this.removeCard(card);
        }
    }

    public void flipTopCard(){
        if(getTopCard()!=null)
            getTopCard().flip();
    }

    public int getSize() {
        return cards.size();
    }

    public String toString() {
        if(getTopCard()==null){
            return WHITE_TEXT +"░░░";
        }
        else{
            return getTopCard().toString();
        }
    }

    public Card getCard(int position) {
        try{
            return cards.get(position);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public abstract boolean isCardValid(Card card);

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
