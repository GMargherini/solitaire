package patience.piles

import patience.deck.{Card, Rank, Suit}

class SuitPile(val suit: Suit) extends Pile {
  def isCardValid(card: Card): Boolean = {
    getTopCard match
      case Some(topCard) => (card.suit eq suit) && topCard.isPrevious(card)
      case None => (card.rank eq Rank.ACE) && (card.suit eq suit)
  }
}
