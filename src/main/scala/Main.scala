import core.Constants.WHITE_TEXT
import core.Game

@main def main(): Unit = {
  val game = new Game
  while (!game.isGameOver) {
    Game.clearScreen()
    System.out.println(WHITE_TEXT + "score: " + game.score + "\tmoves:" + game.moves)
    game.table.printTable()
    val nextMove = Game.readCommand
    if (nextMove.nonEmpty) game.parseMove(nextMove)
  }
  Game.clearScreen()
  System.out.println(WHITE_TEXT + "Game Over\nscore: " + game.table + " moves:" + game.moves)
}
