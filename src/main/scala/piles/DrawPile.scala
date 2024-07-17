package piles

import deck.Card
import java.util


class DrawPile(cards: Seq[Card]) extends Pile(cards) {
  def isCardValid(card: Card) = true
}
