package core

import core.Game.{isMoveValid, updateScore}
import core.Table.{moveCard, moveCards}
import deck.*
import piles.*


object Game {
	def isMoveValid(number: Int, from: Pile, to: Pile): Boolean = {
		if(number == 0) return false
		val movedCard = from.getCard(from.size - number)
		movedCard match
			case Some(card) => to.canAdd(card)
			case None => true
	}

	def updateScore(score: Int, number: Int, from: Pile, to: Pile, wasCovered: Boolean): Int = {
		def movingBetweenLanes(wasCovered: Boolean, movedCard: Card, previousToTopCard: Option[Card], newFromTopCard: Option[Card]): Boolean = {
			(previousToTopCard, newFromTopCard) match
				case (None, None) => movedCard.rank eq Rank.KING
				case (None, Some(y)) => (movedCard.rank eq Rank.KING) & !wasCovered
				case (Some(x), None) => false
				case (Some(x), Some(y)) => ((x.rank eq y.rank) & (x.color eq y.color)) & !wasCovered
		}

		val movedCard = to.getCard(to.size - number).get
		(from,to) match
			case (_:Lane, _:SuitPile) => score + 20
			case (_:UncoveredPile, _:SuitPile) => score + 10
			case (_, _:SuitPile) => score
			case (_:Lane, _:Lane) =>
				if movingBetweenLanes(wasCovered, movedCard, to.getCard(to.size - number - 1), from.getTopCard)
				then score
				else score + (5 * number)
			case (_,_) => score + (5 * number)
	}
}

class Game {
	var score = 0
	var moves = 0
	var table = new Table
	var history = new History

	private def move(number: Int, from: Pile, to: Pile): Unit = {
		if !execute(from, to, number) then Input.handleError()
	}

	private def autoMove(from: Pile, to: Pile): Unit = {
		val isValid = (1 to from.size) map (i => isMoveValid(i, from, to))
		val n = if isValid.contains(true) then isValid.indexOf(true) + 1 else 0
		move(n, from, to)
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
		history.record(moves,move)

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
				case (from:Lane, to: Lane) => if (number == 1) autoMove(from, to) else this.move(number, from, to)
				case (from:Lane, to:Pile) => this.move(number, from, to)
				case (from:Pile, to:Pile) => this.move(number, from, to)
			case _ => move match
				case "D" => table.drawCard()
					moves += 1
				case "Q" =>
					Output.clearScreen()
					sys.exit(0)
				case "L" => Output.printHistory(history)
				case "H" => Output.printHelp()
				case _ => Input.handleError()
	}

	def gameOver: Boolean = {
		table.suitPiles.values.map(pile => pile.size == 13).fold(true)((x,y) => x & y)
	}
}
