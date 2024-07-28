package patience.piles

import patience.core.Output.WHITE_TEXT
import patience.deck.Card


abstract class Pile extends Iterable[Card] {
	protected var cards: Seq[Card] = Vector()

	def this(cards: Seq[Card]) = {
		this()
		this.cards = cards
	}

	def getTopCard: Option[Card] = {
		cards.lastOption
	}

	def addCard(card: Card): Unit = {
		if (isCardValid(card)) cards = cards :+ card
	}
	def getCards(number: Int):Seq[Card] = {
		cards.takeRight(number)
	}
	def getAllCards:Seq[Card] = cards
	def canAdd(card: Card): Boolean = {
		isCardValid(card) & !card.covered
	}

	def removeCard(card: Card): Unit = {
		cards = cards.filter(c => !(c eq card))
	}
	def canRemove(card: Card): Boolean = {
		cards.filter(c => c eq card).contains(card) & !card.covered
	}

	private def addCards(cards: Seq[Card]): Unit = {
		cards foreach (card => if canAdd(card) then addCard(card))
	}

	private def removeCards(cards: Seq[Card]): Unit = {
		cards foreach (card => if canRemove(card) then removeCard(card))
	}

	def flipTopCard(): Unit = {
		getTopCard match
			case Some(topCard) => topCard.flip()
			case None =>
	}

	override def size: Int = cards.size

	override def toString: String = {
		getTopCard match
			case Some(topCard) => topCard.toString
			case None => WHITE_TEXT + "░░░"
	}

	def getCard(position: Int): Option[Card] = {
		if (1 to cards.size) contains (position + 1) then Some(cards(position)) else None
	}

	override def isEmpty: Boolean = cards.isEmpty

	def isCardValid(card: Card): Boolean

	override def iterator: Iterator[Card] = cards.iterator

}
