package patience.deck

import patience.deck.Suit.*
import patience.core.Output.{RED_TEXT, WHITE_TEXT}
import patience.deck.Rank.{ACE, KING}

class Card(val rank: Rank, val suit: Suit) {
	val color: Color = suit match
		case DIAMONDS | HEARTS => Color.RED
		case CLUBS | SPADES => Color.BLACK

	var covered = true

	private def nextRank: Rank =  Rank.values(rank.ordinal + 1)

	override def toString: String = {
		if (covered) WHITE_TEXT + "\u2587\u2587\u2587"
		else {
			val r = rank match {
				case ACE => " A"
				case Rank.TEN => "10"
				case Rank.JACK => " J"
				case Rank.QUEEN => " Q"
				case Rank.KING => " K"
				case _ => s" ${rank.ordinal+1}"
			}
			val s = suit match
				case CLUBS => "\u2663"
				case DIAMONDS => "\u2666"
				case HEARTS => "\u2665"
				case SPADES => "\u2660"

			val card = r+s
			if (color eq Color.RED) & !covered
				then RED_TEXT + card
				else WHITE_TEXT + card
		}
	}

	/*
	unicode cards
	override def toString: String = {
		if (covered) WHITE_TEXT + "\uD83C\uDCA0"
		else {
			val r = rank match {
				case Rank.TEN => "A"
				case Rank.JACK => "B"
				case Rank.QUEEN => "D"
				case Rank.KING => "E"
				case _ => s"${rank.ordinal+1}"
			}
			val s = suit match
				case CLUBS => "D"
				case DIAMONDS => "C"
				case HEARTS => "B"
				case SPADES => "A"

			val codes = raw"\uD83C\uDC$s$r".replace("\\u"," ").split(" ").slice(1,3)
			val chars = codes.map(c => Integer.parseInt(c,16).toChar.toString)
			val card = chars.fold("")((c1, c2) => c1 + c2)
			if (color eq Color.RED) & !covered
				then RED_TEXT + card
				else WHITE_TEXT + card
		}
	}
	 */


	def flip(): Unit = {
		covered = !covered
	}

	def isNext(card: Card): Boolean = rank eq card.nextRank

	def isPrevious(card: Card): Boolean = card.rank eq nextRank
}