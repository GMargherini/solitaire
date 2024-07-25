package piles

import deck.Card


class DrawPile(cards: Seq[Card]) extends Pile(cards) {
  def isCardValid(card: Card) = true
  override def canAdd(card: Card): Boolean = {
    this.isCardValid(card)
  }
  override def canRemove(card: Card): Boolean = {
    cards.filter(c => c eq card).contains(card) & card.covered
  }
}
