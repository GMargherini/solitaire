package patience.piles

import patience.deck.{Card, Rank}


class Lane(cards: Seq[Card]) extends Pile(cards) with Iterable[Card] {
	override def isCardValid(card: Card): Boolean = {
		getTopCard match
			case Some(topCard) => (topCard.color ne card.color) && topCard.isNext(card)
			case None => card.rank eq Rank.KING
	}
}
