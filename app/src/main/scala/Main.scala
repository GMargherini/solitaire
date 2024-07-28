import core.{Game, Input, Output}

import scala.annotation.tailrec

@main def main(): Unit = {
	val game = new Game
	round(game)
}

@tailrec
def round(game: Game): Unit = {
	if (game.gameOver) {
		Output.clearScreen()
		Output.printWhite("Game Over\nscore: " + game.score + " moves:" + game.moves + "\n")
	} else {
		Output.clearScreen()
		Output.printWhite("score: " + game.score + "\tmoves:" + game.moves + "\n")
		Output.printTable(game.table)
		val nextMove = Input.readCommand
		if (nextMove.nonEmpty) game.parseMove(nextMove)
		round(game)
	}
}
