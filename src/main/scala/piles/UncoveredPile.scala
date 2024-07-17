package piles

import deck.Card
import core.InvalidMoveException


class UncoveredPile extends Pile {
  
  @throws[InvalidMoveException]
  override def addCard(card: Card): Unit = {
    super.addCard(card)
    card.flip()
  }

  def isCardValid(card: Card): Boolean = card.covered
}
