package piles

import deck.Card


class DrawPile(cards: Seq[Card]) extends Pile(cards) {
  def isCardValid(card: Card) = true
}
