package patience.core

import patience.core.Input.handleError
import patience.core.Table.{moveCard, moveCards}
import patience.deck.*
import patience.piles.*


object Table {
	def moveCard(card: Card, from: Pile, to: Pile): Unit = {
		from match
			case _:DrawPile =>
				if (to.canAdd(card) & from.canRemove(card)) {
					to.addCard(card)
					from.removeCard(card)
				}
			case _ =>
				if (to.canAdd(card) & from.canRemove(card)) {
					to.addCard(card)
					from.removeCard(card)
					from.getTopCard match
						case Some(topCard) => if (topCard.covered) from.flipTopCard()
						case None =>
		}
	}

	def moveCards(from: Pile, to: Pile, number: Int): Unit = {
		val cards = from.getCards(number)
		cards foreach (card => moveCard(card, from, to))
	}

	def moveCards(from: Pile, to: Pile): Unit = {
		val cards = from.getAllCards.reverse
		cards foreach (card => moveCard(card, from, to))
	}

	def wasCovered(number: Int, from: Pile, to: Pile): Boolean = {
		val previousFromTopCard = from.getCard(from.size - number-1)

		previousFromTopCard match
			case Some(card) => card.covered
			case None => false
	}
}

class Table {
	val deck = new Deck
	var suitPiles: Map[Suit, SuitPile] = Map[Suit,SuitPile]()
	var lanes: Seq[Lane] = Vector[Lane]()
	lanes = (1 to 7) map (i => new Lane(deck.getCards(i)))
	lanes foreach (lane => lane.getTopCard.get.flip())
	Suit.values foreach (suit => suitPiles = suitPiles + (suit -> new SuitPile(suit)))
	var drawPile: DrawPile = new DrawPile(deck.getCards(deck.getDeckSize))
	var uncoveredPile: UncoveredPile = new UncoveredPile

	def drawCard(): Unit = {
		drawPile.getTopCard match
			case Some(card) => moveCard(card, drawPile, uncoveredPile)
			case None =>
				if (uncoveredPile.isEmpty) handleError()
				else {
					moveCards(uncoveredPile, drawPile)
					drawPile foreach (card => card.flip())
				}
	}

	def getLane(laneNumber: Int): Lane = lanes(laneNumber)

	def getSuitPile(suit: Suit): SuitPile = suitPiles(suit)
}
