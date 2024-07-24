import core.{Game, Table}
import deck.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import piles.{Lane, SuitPile}

class MovesTest extends AnyFunSuite with Matchers {
  private val kd = new Card(Rank.KING, Suit.DIAMONDS)
  private val kh = new Card(Rank.KING, Suit.HEARTS)
  private val qc = new Card(Rank.QUEEN, Suit.CLUBS)
  private val fs = new Card(Rank.FIVE, Suit.SPADES)
  private var lane1: Lane = Lane(Vector[Card]())
  private var lane2: Lane = Lane(Vector[Card]())

  test ("input test") {
    def error(m:String) : String = {
      m
    }
    assert(Game.readCommand("c")(error).isEmpty)
    assert(Game.readCommand("12c")(error).isEmpty)
    assert(Game.readCommand("232")(error).equals("232"))
    assert(Game.readCommand("5c")(error).equals("5C"))
    assert(Game.readCommand("q")(error).equals("Q"))
    assert(Game.readCommand("d")(error).equals("D"))
  }

  test("moveOneNoScoreTest") {
    kd.flip()
    kh.flip()
    qc.flip()
    lane1 = new Lane(Vector[Card](kd,qc))
    lane2 = new Lane(Vector[Card]())
    lane2.addCard(kh)
    assert(!kd.covered)
    assert(!kh.covered)
    /*
            *  1  2
            *  KD KH
            *  QC
            *
            * moving a card from one lane to another with a card with an equal rank
            * does not increase the score
            * */
    assert(!Game.checkScore(1, lane1, lane2))
    Table.moveCard(lane1, lane2)
  }

  test("moveOneScoreTest") {
    lane1 = new Lane(Vector[Card](kd, qc))
    lane2 = new Lane(Vector[Card]())
    lane2.addCard(kh)
    kd.flip()
    assert(kd.covered)
    /*
             *  1  2
             *  ██ KH
             *  QC
             *
             * moving a card from one lane to another with a card with an equal rank
             * increases the score if the first is covered
             * */
    assert(Game.checkScore(1, lane1, lane2))
    Table.moveCard(lane1, lane2)
  }

  test("moveTwoNoScoreTest") {
    lane1 = new Lane(Vector[Card](kd, qc))
    lane2 = new Lane(Vector[Card]())
    /*
             *  1  3
             *  KD
             *  QC
             *
             * moving cards between empty lanes
             * does not increase the score
             * */
    assert(!Game.checkScore(2, lane1, lane2))
    Table.moveCards(2, lane1, lane2)
  }

  test("moveOneTest") {
    lane1 = new Lane(Vector[Card](fs, qc))
    lane2 = new Lane(Vector[Card](kd,kh))
    /*
             *  1  2
             *  5S ██
             *  QC KH
             *
             * moving a card from one lane to another
             * increases the score
             * */
    assert(Game.checkScore(1, lane1, lane2))
    Table.moveCard(lane1, lane2)
  }

  test("addToLaneTest") {
    lane1 = new Lane(Vector[Card]())
    assert(lane1.isCardValid(kh))
    lane1.addCard(kh)
    assert(lane1.isCardValid(qc))
    assert(!lane1.isCardValid(fs))
    assert(!lane1.isCardValid(new Card(Rank.QUEEN, Suit.DIAMONDS)))
  }

  test("addToSuitTest") {
    val heartsPile = new SuitPile(Suit.HEARTS)
    heartsPile.addCard(new Card(Rank.ACE, Suit.HEARTS))
    assert(heartsPile.isCardValid(new Card(Rank.TWO, Suit.HEARTS)))
    assert(!heartsPile.isCardValid(new Card(Rank.TWO, Suit.DIAMONDS)))
    assert(!heartsPile.isCardValid(new Card(Rank.THREE, Suit.HEARTS)))
  }
}



