package deck

import java.util
import java.util.Random


class Deck {
  val random = new Random()
  var cards: Seq[Card] = Vector[Card]()

  for (suit <- Suit.values) {
    for (rank <- Rank.values) {
      val newCard = new Card(rank, suit)
      cards = cards :+ newCard
    }
  }

  def pickRandomCard: Card = {
    val index = random.nextInt(cards.size)
    val card = cards(index)
    cards = cards filterNot card.==
    card
  }

  def getCards(numberOfCards: Int): Seq[Card] = {
    var cards = Vector[Card]()
    for (i <- 0 until numberOfCards) {
      cards = cards :+ pickRandomCard
    }
    cards
  }

  def getDeckSize: Int = cards.size
}
