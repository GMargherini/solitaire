package patience
import patience.core.{Game, Input, Output}

import scala.annotation.tailrec
object Main {
	def main(args: Array[String]): Unit = {
		val game = new Game
		round(game)
	}

	@tailrec
	private def round(game: Game): Unit = {
		if (game.gameOver) {
			Input.handleError(s"Game Over\n$game")
			Output.clearScreen()
		} else {
			Output.printWhite(game.toString)
			Output.printTable(game.table)
			val nextMove = Input.readCommand
			if (nextMove.nonEmpty) game.runMove(nextMove)
			round(game)
		}
	}
}

