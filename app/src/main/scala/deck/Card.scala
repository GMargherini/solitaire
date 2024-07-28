package deck

import deck.Suit.*
import core.Output.{RED_TEXT, WHITE_TEXT}
import deck.Rank.KING

class Card(val rank: Rank, val suit: Suit) {
	val color: Color = suit match
		case DIAMONDS | HEARTS => Color.RED
		case CLUBS | SPADES => Color.BLACK

	var covered = true

	private def nextRank: Rank =  Rank.values(rank.ordinal + 1)

	override def toString: String = {
		if (covered) WHITE_TEXT + "▇▇▇"
		else {
			val r = rank match {
				case Rank.ACE => " A"
				case Rank.TEN => "10"
				case Rank.JACK => " J"
				case Rank.QUEEN => " Q"
				case Rank.KING => " K"
				case _ => " " + (rank.ordinal + 1)
			}
			val s = suit match
				case CLUBS => "\u2663"
				case DIAMONDS => "\u2666"
				case HEARTS => "\u2665"
				case SPADES => "\u2660"
			if ((color eq Color.RED) & !covered) RED_TEXT + r + s
			else WHITE_TEXT + r + s
		}
	}


	def flip(): Unit = {
		covered = !covered
	}

	def isNext(card: Card): Boolean = rank eq card.nextRank

	def isPrevious(card: Card): Boolean = card.rank eq nextRank
}