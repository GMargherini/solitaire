package piles

import deck.Card
import deck.Rank
import java.util


class Lane(cards: Seq[Card]) extends Pile(cards) with Iterable[Card] {
  def isCardValid(card: Card): Boolean = {
    var valid = false
    valid = getTopCard match
      case Some(topCard) => (topCard.color ne card.color) && topCard.isNext(card)
      case None => card.rank eq Rank.KING
    valid
  }
}
