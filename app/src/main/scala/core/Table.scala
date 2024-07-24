package core

import core.Constants.WHITE_TEXT
import deck.*
import piles.{DrawPile, Lane, Pile, SuitPile, UncoveredPile}

import scala.Console.out


object Table {
  @throws[InvalidMoveException]
  def moveCard(from: Pile, to: Pile): Unit = {
    val card = from.getTopCard.get
    to.addCard(card)
    from.removeCard(card)
    from.getTopCard match
      case Some(topCard) => from match
        case _:DrawPile =>
        case _ => if (topCard.covered) from.flipTopCard()
      case None =>
  }

  @throws[InvalidMoveException]
  def moveCards(number: Int, from: Pile, to: Pile): Unit = {
    if (number == 0 || from.isEmpty) throw new InvalidMoveException
    var cards = Vector[Card]()
    for (i <- (from.getSize - number) until from.getSize){
      from.getCard(i) match
        case Some(x) => cards = cards :+ x
        case None => 
    }
    to.addCards(cards)
    from.removeCards(cards)
    from.getTopCard match
      case Some(topCard) => if (topCard.covered) from.flipTopCard()
      case None =>
  }

  @throws[InvalidMoveException]
  def moveCards(from: Pile, to: Pile): Unit = {
    var cards = Vector[Card]()
    for (i <- (from.getSize - 1) until 0 by -1) {
      from.getCard(i) match
        case Some(c) => cards = cards :+ c
        case None =>
    }
    to.addCards(cards)
    from.removeCards(cards)
    from.getTopCard match
      case Some(topCard) => if (topCard.covered) from.flipTopCard()
      case None =>
  }
}

class Table {
  val deck = new Deck
  private var suitPiles: Map[Suit, SuitPile] = Map[Suit,SuitPile]()
  private var lanes: Seq[Lane] = Vector[Lane]()
  for (i <- 1 to 7) {
    lanes = lanes :+ new Lane(deck.getCards(i))
  }
  for (lane <- lanes) {
    lane.getTopCard.get.flip()
  }

  for (suit <- Suit.values) {
    suitPiles = suitPiles + (suit -> new SuitPile(suit))
  }

  private var drawPile: DrawPile = new DrawPile(deck.getCards(deck.getDeckSize))
  var uncoveredPile: UncoveredPile = new UncoveredPile

  def printTable(): Unit = {
    var lines = 0
    //find the longest lane
    for (lane <- lanes) {
      if (lane.getSize > lines) lines = lane.getSize
    }
    //print labels
    out.println(WHITE_TEXT + "     P \t     C   D   H   S")
    out.print("" + drawPile + " " + uncoveredPile + "\t    ")
    for (suit <- Suit.values) {
      out.print("" +
        (suitPiles.get(suit) match
        case Some(x) => x
        case None => "   " )
        + " ")
    }
    out.print("\n\n ")
    //print lane labels
    for (i <- lanes.indices) {
      out.print(WHITE_TEXT + (i + 1) + "   ")
    }
    out.println()
    //print lanes
    for (i <- 0 until lines) {
      for (lane <- lanes) {
        out.print("" +
          (lane.getCard(i) match
          case Some(c) => c
          case None => "   ")
            + " ")
      }
      out.println()
    }
  }

  @throws[InvalidMoveException]
  def drawCard(): Unit = {
    if (drawPile.isEmpty) {
      if (uncoveredPile.isEmpty) throw new InvalidMoveException
      Table.moveCards(uncoveredPile, drawPile)
      for (card <- drawPile) {
        card.flip()
      }
    }
    else Table.moveCard(drawPile, uncoveredPile)
  }

  def getLane(laneNumber: Int): Lane = lanes(laneNumber)

  def getSuitPile(suit: Suit): SuitPile = suitPiles(suit)
}
