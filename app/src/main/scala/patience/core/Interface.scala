package patience.core

import scala.Console.{WHITE, in, out}
import scala.io.AnsiColor.RED

object Output {
	val RED_TEXT: String = RED
	val WHITE_TEXT: String = WHITE
	def printTable(table: Table): Unit = {
		printWhite("\t P\t\t C\u2663\t D\u2666\t H\u2665\t S\u2660\n")
		out.print(s"${table.drawPile}\t${table.uncoveredPile}\t\t")
		table.suitPiles foreach ((_, pile) => out.print(s"$pile\t"))
		out.print("\n\n ")

		table.lanes.indices foreach (i=>printWhite(s"${i + 1}\t "))
		out.println()

		val lines = table.lanes.max((l1, l2) => l1.size.compareTo(l2.size)).size
		for(i <- 0 until lines){
			for(lane <- table.lanes){
				val card = lane.getCard(i) match
					case Some(c) => s"$c\t"
					case None => "\t"
				out.print(s"$card")
			}
			out.println()
		}
	}

	def printWhite(string: String): Unit = {
		out.print(WHITE_TEXT+string)
	}

	def printRed(string: String): Unit = {
		out.print(RED_TEXT + string + WHITE_TEXT)
	}

	def clearScreen(): Unit = {
		System.out.print("\u001b[H\u001b[2J")
		System.out.flush()
	}
	def printHistory(history: History): String = {
		clearScreen()
		out.println(history)
		Input.handleError("Press enter to continue")
	}
	def printHelp(): Unit = {
		clearScreen()
		out.println("""
		  | H					Print help
		  | Q					Quit game
		  | L					Print move history
		  | [Pile1][Pile2]		Automatically move cards from Pile1 to Pile2
		  | [Pile1][Pile2][n]	Move n cards from Pile1 to Pile2
		  | D					Draw a card from the uncovered pile
		  |
		  | Pile can be any between 1-7,P,C,D,H,S
		  |""".stripMargin)
		Input.handleError("Press enter to continue")
	}
}

object Input{
	def readCommand: String = {
		Output.printWhite("Enter move> ")
		val command = in.readLine()
		checkCommand(command)(handleError)
	}

	def checkCommand(command: String)(er: String => String): String = {
		val one = "([DHLQdhlq])".r
		val two = "([CDHPScdhps1-7]{2})".r
		val more = "([0-9]{1,2})".r
		val errorMessage = "Invalid Command, press enter to continue"

		(command.length match
			case 1 | 2 => command match
				case one(c) => c
				case two(c) => c
				case _ => er(errorMessage)
			case 3 | 4 => command.substring(2) match
				case more(c) => command.substring(0, 3)
				case _ => er(errorMessage)
			case _ => er(errorMessage)).toUpperCase
	}

	def handleError(message: String): String = {
		Output.printRed(message)
		in.readLine()
		""
	}
	def handleError():String = {
		handleError("Illegal move, press enter to continue")
	}
}
