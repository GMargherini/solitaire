package deck

import core.Constants.*
import deck.Suit.*

class Card(val rank: Rank, val suit: Suit) {
  val color: Color = suit match
    case DIAMONDS | HEARTS => Color.RED
    case _ => Color.BLACK

  var covered = true

  def nextRank: Rank = Rank.values(rank.ordinal + 1)

  override def toString: String = {
    var string: String = null
    if (covered) return WHITE_TEXT + "▇▇▇"
    else {
      val r = rank match {
        case Rank.ACE => " A"
        case Rank.TEN => "10"
        case Rank.JACK => " J"
        case Rank.QUEEN => " Q"
        case Rank.KING => " K"
        case _ => " " + (rank.ordinal + 1)
      }
      val s = suit.toString.charAt(0)
      if ((color eq Color.RED) & !covered) string = RED_TEXT + r + s
      else string = WHITE_TEXT + r + s
    }
    string
  }


  def flip(): Unit = {
    covered = !covered
  }

  def isNext(card: Card): Boolean = rank eq card.nextRank

  def isPrevious(card: Card): Boolean = card.rank eq nextRank
}