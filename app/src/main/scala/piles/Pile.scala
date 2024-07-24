package piles

import core.Constants.WHITE_TEXT
import core.InvalidMoveException
import deck.Card


abstract class Pile extends Iterable[Card] {
  protected var cards: Seq[Card] = Vector()

  def this(cards: Seq[Card]) = {
    this()
    this.cards = cards
  }

  def getTopCard: Option[Card] = {
    cards.lastOption
  }

  @throws[InvalidMoveException]
  def addCard(card: Card): Unit = {
    if (this.isCardValid(card)) cards = cards :+ card
    else throw new InvalidMoveException
  }

  @throws[InvalidMoveException]
  def removeCard(card: Card): Unit = {
    if (!cards.filter(c => c eq card).contains(card)) throw new InvalidMoveException
    cards = cards.filter(c => !(c eq card))
  }

  @throws[InvalidMoveException]
  def addCards(cards: Seq[Card]): Unit = {
    for (card <- cards) {
      this.addCard(card)
    }
  }

  @throws[InvalidMoveException]
  def removeCards(cards: Seq[Card]): Unit = {
    for (card <- cards) {
      this.removeCard(card)
    }
  }

  def flipTopCard(): Unit = {
    getTopCard match
      case Some(topCard) => topCard.flip()
      case None =>
  }

  def getSize: Int = cards.size

  override def toString: String = {
    getTopCard match
      case Some(topCard) => topCard.toString
      case None => WHITE_TEXT + "░░░"
  }

  def getCard(position: Int): Option[Card] = {
    try Some(cards(position))
    catch {
      case e: IndexOutOfBoundsException =>
        None
    }
  }

  override def isEmpty: Boolean = cards.isEmpty

  def isCardValid(card: Card): Boolean

  override def iterator: Iterator[Card] = cards.iterator

}
