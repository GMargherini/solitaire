package core;

import deck.*;
import piles.*;

import java.util.*;

import static core.Constants.WHITE_TEXT;

public class Table {
    private DrawPile drawPile;
    private UncoverdPile uncoveredPile;

    private Map<Suit, SuitPile> suitPiles = new HashMap<>();

    private Lane[] lanes = new Lane[7];
    public Table(){
        Deck deck = new Deck();
        for(int i=0;i<7;i++){
            lanes[i] = new Lane(deck.getCards(i+1));
        }
        for(Lane lane : lanes){
            lane.getTopCard().flip();
        }
        for(Suit suit : Suit.values()){
            suitPiles.put(suit,new SuitPile(suit));
        }
        uncoveredPile = new UncoverdPile();
        drawPile = new DrawPile(deck.getCards(deck.getDeckSize()));
    }

    public void printTable(){
        int lines=0;
        //find the longest lane
        for(Lane lane : lanes){
            if(lane.getSize() > lines)
                lines=lane.getSize();
        }
        //print labels
        System.out.println(WHITE_TEXT +"     P \t     C   D   H   S");
        System.out.print(drawPile + " " + uncoveredPile + "\t    ");
        for(Suit suit : Suit.values()){
            System.out.print(suitPiles.get(suit)+" ");
        }
        System.out.print("\n\n ");
        //print lane labels
        for(int i=0;i < lanes.length;i++){
            System.out.print(WHITE_TEXT +(i+1)+"   ");
        }
        System.out.println();
        //print lanes
        for(int i=0;i<lines;i++){
            for (Lane lane : lanes){
                Card card = lane.getCard(i);
                if(card!=null){
                    System.out.print(card + " ");
                }
                else{
                    System.out.print("    ");
                }
            }
            System.out.println();
        }
    }

    public static void moveCard(Pile from, Pile to) throws InvalidMoveException {
        Card card = from.getTopCard();
        to.addCard(card);
        from.removeCard(card);
        if(from.getTopCard() != null && from.getTopCard().isCovered() && !(from instanceof DrawPile))
            from.flipTopCard();
    }

    public static void moveCards(int number, Pile from, Pile to) throws InvalidMoveException {
        if (number==0 || from.isEmpty()){
            throw new InvalidMoveException();
        }
        List<Card> cards=new LinkedList<>();
        for(int i= from.getSize()-number;i < from.getSize();i++){
            if(from.getCard(i)!=null)
                cards.add(from.getCard(i));
        }
        to.addCards(cards);
        from.removeCards(cards);
        if(from.getTopCard() != null && from.getTopCard().isCovered())
            from.flipTopCard();
    }

    public static void moveCards(Pile from, Pile to) throws InvalidMoveException {
        List<Card> cards=new LinkedList<>();
        for(int i= from.getSize()-1;i > 0;i--){
            if(from.getCard(i)!=null)
                cards.add(from.getCard(i));
        }
        to.addCards(cards);
        from.removeCards(cards);
        if(from.getTopCard() != null && from.getTopCard().isCovered())
            from.flipTopCard();
    }

    public void drawCard() throws InvalidMoveException {
        if(drawPile.isEmpty()){
            if(uncoveredPile.isEmpty()){
                throw new InvalidMoveException();
            }
            moveCards(uncoveredPile,drawPile);
            for(Card card : drawPile){
                card.flip();
            }
        }
        else{
            moveCard(drawPile,uncoveredPile);
        }
    }

    public Lane getLane(int laneNumber) {
        return lanes[laneNumber];
    }

    public Pile getSuitPile(Suit suit) {
        return suitPiles.get(suit);
    }

    public UncoverdPile getUncoveredPile() {
        return uncoveredPile;
    }
}
