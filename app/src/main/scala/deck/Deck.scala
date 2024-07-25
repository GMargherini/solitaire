package deck

import java.util
import java.util.Random


class Deck {
  private val random = new Random()
  var cards: Seq[Card] = Vector[Card]()

  cards = Suit.values.flatMap(suit => Rank.values.filterNot(Rank.EMPTY.==).map(rank => new Card(rank, suit))).toVector

  private def pickRandomCard: Card = {
    val index = random.nextInt(cards.size)
    val card = cards(index)
    cards = cards filterNot card.==
    card
  }

  def getCards(numberOfCards: Int): Seq[Card] = {
    ((0 until numberOfCards) map (_ => pickRandomCard)).toVector
  }

  def getDeckSize: Int = cards.size
}
