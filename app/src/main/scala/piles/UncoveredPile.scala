package piles

import deck.Card


class UncoveredPile extends Pile {
  
  override def addCard(card: Card): Unit = {
    super.addCard(card)
    card.flip()
  }
  override def canAdd(card: Card): Boolean = {
    true
  }

  def isCardValid(card: Card): Boolean = card.covered
}
