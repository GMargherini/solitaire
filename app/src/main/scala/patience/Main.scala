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
			Output.clearScreen()
			Input.handleError("Game Over\nscore: " + game.score + " moves:" + game.moves + "\n")
			Output.clearScreen()
		} else {
			Output.clearScreen()
			Output.printWhite("score: " + game.score + "\tmoves:" + game.moves + "\n")
			Output.printTable(game.table)
			val nextMove = Input.readCommand
			if (nextMove.nonEmpty) game.parseMove(nextMove)
			round(game)
		}
	}
}

