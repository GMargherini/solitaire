package core

import core.Game.{isMoveValid, updateScore}
import core.Table.{moveCard, moveCards}
import deck.*
import piles.*


object Game {
	def isMoveValid(number: Int, from: Pile, to: Pile): Boolean = {
		var isValid = true
		if(number == 0) return false
		val movedCard = from.getCard(from.getSize - number)
		isValid = movedCard match
			case Some(card) => to.canAdd(card)
			case None => true
		isValid
	}

	def updateScore(score: Int, number: Int, from: Pile, to: Pile, wasCovered: Boolean): Int = {
		(from,to) match
			case (_:Lane, _:SuitPile) => score + 20
			case (_: UncoveredPile, _: SuitPile) => score + 10
			case (_, _: SuitPile) => score
			case (_: Lane, _: Lane) =>
				to.getCard(to.getSize - number) match
					case Some(card) =>
						if (to.getSize - number == 0) & !(card.rank eq Rank.KING) then score + 5
						else {
							(to.getCard(to.getSize - number - 1), from.getTopCard) match
								case (Some(previousToTopCard), Some(newFromTopCard)) =>
									if !(((newFromTopCard.rank eq previousToTopCard.rank) &
										(newFromTopCard.color eq previousToTopCard.color)) &
										!wasCovered) then score + 5 else score
								case (None, _) | (_, None) => if !(card.rank eq Rank.KING) then score + 5 else score
						}
					case None => score + 5
			case (_, _: Lane) => score
	}

}

class Game {
	var score = 0
	var moves = 0
	var table = new Table

	private def move(number: Int, input: String): Unit = {
		input match
			case "D" => table.drawCard()
			case "Q" =>
				Output.clearScreen()
				sys.exit(0)
			case _ => Input.handleError()
	}
	private def move(number: Int, from: Pile, to: Pile, input: String): Unit = {
		val isValid = Game.isMoveValid(number, from, to)
		if !execute(from, to, number) then Input.handleError()
	}

	private def autoMove(from: Pile, to: Pile): Unit = {
		val isValid = (1 to from.getSize) map (i => isMoveValid(i, from, to))
		val n = if isValid.contains(true) then isValid.indexOf(true) + 1 else 0
		if !execute(from,to,n) then Input.handleError()
	}

	private def execute(from: Pile, to: Pile, number: Int): Boolean = {
		val isValid = Game.isMoveValid(number, from, to)
		if isValid then {
			val wasCovered = Table.wasCovered(number, from, to)
			if number == 1 then
				moveCard(from.getTopCard.get, from, to) else moveCards(from, to, number)
			score = updateScore(score, number, from, to, wasCovered)
			moves += 1
		}
		isValid
	}

	def parseMove(move: String): Unit = {
		var piles = List[Pile]()
		val fromTo = move.length match {
			case 1 | 2 => move
			case 3 | 4 => move.substring(0, 2)
			case _ => ""
		}
		val number = move.length match {
			case 2 => 1
			case 3 | 4 => move.substring(2).toInt
			case _ => 0
		}
		for (c <- fromTo.toCharArray) {
			piles = piles :+ (c match {
				case 'P' => table.uncoveredPile
				case 'C' => table.getSuitPile(Suit.CLUBS)
				case 'D' => table.getSuitPile(Suit.DIAMONDS)
				case 'H' => table.getSuitPile(Suit.HEARTS)
				case 'S' => table.getSuitPile(Suit.SPADES)
				case '1' => table.getLane(0)
				case '2' => table.getLane(1)
				case '3' => table.getLane(2)
				case '4' => table.getLane(3)
				case '5' => table.getLane(4)
				case '6' => table.getLane(5)
				case '7' => table.getLane(6)
				case _ => null
			})
		}
		piles match
			case first :: second :: tail => (first, second) match
				case (from:Lane, to: Lane) => if (number == 1) autoMove(from, to) else this.move(number, from, to, move)
				case (from:Lane, to:Pile) => this.move(number, from, to, move)
				case (from:Pile, to:Pile) => this.move(number, from, to, move)
			case _ => this.move(number, move)
	}

	def gameOver: Boolean = {
		Suit.values.map(suit => table.getSuitPile(suit).getSize == 13).fold(true)((x,y) => x & y)
	}
}
