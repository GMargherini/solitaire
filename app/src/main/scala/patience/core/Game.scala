package patience.core

import patience.core.Game.{isMoveValid, updateScore}
import patience.core.Input.handleError
import patience.core.Table.{forceMoveCard, forceMoveCards, moveCard, moveCards, unDrawCard}
import patience.deck.*
import patience.piles.{Pile, *}


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
			case (_:Lane, _:SuitPile) => 20
			case (_:UncoveredPile, _:SuitPile) => 10
			case (_, _:SuitPile) => 0
			case (_:Lane, _:Lane) =>
				if movingBetweenLanes(wasCovered, movedCard, to.getCard(to.size - number - 1), from.getTopCard)
				then 0
				else 5 * number
			case (_,_) => 5 * number
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
			val addedScore = updateScore(score, number, from, to, wasCovered)
			score += addedScore
			history.updateScore(moves, addedScore)
			moves += 1
		}
		isValid
	}

	private def forceMove(number: Int, from: Pile, to: Pile): Unit = {
		if number == 1 then
			forceMoveCard(from.getTopCard.get, from, to) 
		else forceMoveCards(from, to, number)
	}

	private def undo(): Unit = {
		val moveNumber = moves - 1
		val move = history.lastMove(moveNumber)
		val tuple = parseMove(move)
		tuple match
			case (0, null, null) =>
				if move == "D" then
					unDrawCard(table.drawPile,table.uncoveredPile)
					moves -= 1
				else handleError(s"Cannot undo move $move")
			case (n, f, t) => forceMove(n, t, f)
				moves -= 1
				score -= history.lastScore(moveNumber)
		history.delete(moveNumber)
	}

	def runMove(move:String): Unit = {
		val tuple = parseMove(move)
		tuple match
			case (0, null, null) => move match
				case "D" =>
					Table.drawCard(table.drawPile,table.uncoveredPile)
					moves += 1
				case "Q" =>
					Output.clearScreen()
					sys.exit(0)
				case "L" => Output.printHistory(history)
				case "H" => Output.printHelp()
				case "U" => undo()
				case _ => Input.handleError()
			case (1, from: Lane, to: Lane) => autoMove(from, to)
			case (_, _, _) => this.move.tupled(tuple)
	}

	private def parseMove(move: String): (Int, Pile, Pile) = {
		history.record(moves,move,0)

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
				case (from:Lane, to: Lane) => if (number == 1) (1, from, to) else (number, from, to)
				case (from:Lane, to:Pile) => (number, from, to)
				case (from:Pile, to:Pile) => (number, from, to)
			case _ => (0, null, null)
	}

	def gameOver: Boolean = {
		table.suitPiles.values.map(pile => pile.size == 13).fold(true)((x,y) => x & y)
	}

	override def toString: String = {
		Output.clearScreen()
		s"score: $score\tmoves: $moves\n"
	}
}
