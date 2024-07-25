import core.{Game, Input, Output}

@main def main(): Unit = {
	val game = new Game
	while (!game.gameOver) {
		Output.clearScreen()
		Output.printWhite("score: " + game.score + "\tmoves:" + game.moves + "\n")
		Output.printTable(game.table)
		val nextMove = Input.readCommand
		if (nextMove.nonEmpty) game.parseMove(nextMove)
	}
	Output.clearScreen()
	Output.printWhite("Game Over\nscore: " + game.score + " moves:" + game.moves + "\n")
}
