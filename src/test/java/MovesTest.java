import core.GameJava;
import core.Table;
import deck.*;
import core.InvalidMoveException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;


public class MovesTest {

    private static Card kd = new Card(Rank.KING, Suit.DIAMONDS);
    private static Card kh = new Card(Rank.KING, Suit.HEARTS);
    private static Card qc = new Card(Rank.QUEEN, Suit.CLUBS);
    private static Card fs = new Card(Rank.FIVE,Suit.SPADES);

    private static Lane lane1;
    private static Lane lane2;


    @BeforeClass
    public static void setup() {
        kd.flip();
        kh.flip();
        qc.flip();

    }
    @Test
    public void inputTest(){
        // if the method returns an empty string it means the command is not valid
        assert GameJava.readCommand("c").isEmpty();
        assert GameJava.readCommand("12c").isEmpty();
        assert GameJava.readCommand("232").equals("232");
        assert GameJava.readCommand("5c").equals("5C");
        assert GameJava.readCommand("q").equals("Q");
        assert GameJava.readCommand("d").equals("D");
    }

    @Test
    public void moveOneNoScoreTest() throws InvalidMoveException {
        lane1 = new Lane(new LinkedList<>(List.of(new Card[]{kd, qc})));
        lane2 = new Lane(new LinkedList<>());
        lane2.addCard(kh);
        assert !kd.isCovered();
        assert !kh.isCovered();
        /*
        *  1  2
        *  KD KH
        *  QC
        *
        * moving a card from one lane to another with a card with an equal rank
        * does not increase the score
        * */
        assert !GameJava.checkScore(1,lane1,lane2);
        Table.moveCard(lane1,lane2);
    }

    @Test
    public void moveOneScoreTest() throws InvalidMoveException {
        lane1 = new Lane(new LinkedList<>(List.of(new Card[]{kd, qc})));
        lane2 = new Lane(new LinkedList<>());
        lane2.addCard(kh);
        kd.flip();
        assert kd.isCovered();

        /*
         *  1  2
         *  ██ KH
         *  QC
         *
         * moving a card from one lane to another with a card with an equal rank
         * increases the score if the first is covered
         * */
        assert GameJava.checkScore(1,lane1,lane2);
        Table.moveCard(lane1,lane2);
    }

    @Test
    public void moveTwoNoScoreTest() throws InvalidMoveException {
        lane1 = new Lane(new LinkedList<>(List.of(new Card[]{kd, qc})));
        lane2 = new Lane(new LinkedList<>());
        /*
         *  1  3
         *  KD
         *  QC
         *
         * moving cards between empty lanes
         * does not increase the score
         * */
        assert !GameJava.checkScore(2,lane1, lane2);
        Table.moveCards(2,lane1,lane2);
    }

    @Test
    public void moveOneTest() throws InvalidMoveException {
        lane1 = new Lane(new LinkedList<>(List.of(new Card[]{fs, qc})));
        lane2 = new Lane(new LinkedList<>(List.of(new Card[]{kd, kh})));
        /*
         *  1  2
         *  5S ██
         *  QC KH
         *
         * moving a card from one lane to another
         * increases the score
         * */
        assert GameJava.checkScore(1,lane1,lane2);
        Table.moveCard(lane1,lane2);
    }

    @Test
    public void addToLaneTest() throws InvalidMoveException {
        lane1 = new Lane(new LinkedList<>());
        assert lane1.isCardValid(kh);
        lane1.addCard(kh);
        assert lane1.isCardValid(qc);
        assert !lane1.isCardValid(fs);
        assert !lane1.isCardValid(new Card(Rank.QUEEN, Suit.DIAMONDS));
    }

    @Test
    public void addToSuitTest() throws InvalidMoveException {
        SuitPile heartsPile = new SuitPile(Suit.HEARTS);
        heartsPile.addCard(new Card(Rank.ACE,Suit.HEARTS));
        assert heartsPile.isCardValid(new Card(Rank.TWO,Suit.HEARTS));
        assert !heartsPile.isCardValid(new Card(Rank.TWO,Suit.DIAMONDS));
        assert !heartsPile.isCardValid(new Card(Rank.THREE,Suit.HEARTS));
    }
}



