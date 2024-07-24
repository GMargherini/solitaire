package core

import core.Constants.*
import core.Game.handleError
import deck.*
import piles.*

import scala.Console.{err, in, out}
import scala.util.control.Breaks.{breakable, break}


object Game {
  def readCommand: String = {
    out.println(WHITE_TEXT + "Enter move> ")
    val move = in.readLine()
    readCommand(move) (handleError)
  }
  def readCommand(move: String)(el: String =>String): String = {
    var mv = move
    if (move.length >= 3 && move.substring(2).matches("[0-9]{1,2}")) mv = move.substring(0, 3)
    else if (!move.matches("[CDHPScdhps1-7]{2}") & !move.matches("[DQdq]")) {
      el("Invalid Command, press enter to continue")
      mv = ""
    }
    mv.toUpperCase
  }

  def clearScreen(): Unit = {
    System.out.print("\u001b[H\u001b[2J")
    System.out.flush()
  }

  def checkScore(number: Int, from: Pile, to: Pile): Boolean = {
    var isValid = true
    val movedCard = from.getCard(from.getSize - number)

    isValid = if (from.getSize == number) {
      movedCard match
        case Some(c) => !(c.rank eq Rank.KING)
        case None => true
    } else to.getTopCard match
      case Some(previousToTopCard) => from.getCard(from.getSize - number - 1) match
        case Some(newFromTopCard) =>
          !(((newFromTopCard.rank eq previousToTopCard.rank) &
            (newFromTopCard.color eq previousToTopCard.color)) &
            !newFromTopCard.covered)
        case None => true
      case None => true
    isValid
  }
  def handleError(message: String): String = {
    err.println(RED_TEXT + message + WHITE_TEXT)
    in.readLine()
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
        Game.clearScreen()
        sys.exit(0)
      case _ => handleError("Illegal move, press enter to continue")
  }
  private def move(number: Int, from: Pile, to: Pile, input: String): Unit = {
    try {
      val isValid = Game.checkScore(number, from, to)
      Table.moveCards(number, from, to)
      updateScore(number, from, to, isValid)
      moves += 1
    } catch {
      case _: InvalidMoveException | _: IndexOutOfBoundsException =>
        handleError("Illegal move, press enter to continue")
    }
  }

  private def autoMove(from: Pile, to: Pile): Unit = {
    try{
      breakable {
        for (i <- 1 to from.getSize) {
          var j = 1
          try {
            val checkScore = Game.checkScore(i, from, to)
            Table.moveCards(i, from, to)
            updateScore(i, from, to, checkScore)
            moves += 1
            break()
          } catch {
            case _: InvalidMoveException =>
              if (j == from.getSize) throw new InvalidMoveException
          }
          j += 1
        }
      }
    } catch {
      case _: InvalidMoveException =>
        handleError("Illegal move, press enter to continue")
    }
  }



  private def updateScore(number: Int, from: Pile, to: Pile, isValid: Boolean): Unit = {
    to match {
      case _: SuitPile =>
        from match {
          case _: Lane => score += 20
          case _: UncoveredPile => score += 10
          case _ =>
        }
      case _: Lane =>
        from match {
          case _:Lane => if(isValid) score += (5 * number)
          case _ =>
        }
    }
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
    var completedPiles = Map[Suit, Boolean]()
    Suit.values.foreach(suit => completedPiles = completedPiles + ((suit, table.getSuitPile(suit).getSize == 13)))
    completedPiles.values.fold(true)((x,y) => x & y)
  }
}
