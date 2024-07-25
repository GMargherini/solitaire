package core

import scala.Console.{WHITE, in, out}
import scala.io.AnsiColor.RED

object Output {
	val RED_TEXT: String = RED
	val WHITE_TEXT: String = WHITE
	def printTable(table: Table): Unit = {
		var lines = 0
		//find the longest lane
		lines = table.lanes.max((l1, l2) => l1.getSize.compareTo(l2.getSize)).getSize
		//print labels
		out.println(WHITE_TEXT + "     P \t     C   D   H   S")
		out.print("" + table.drawPile + " " + table.uncoveredPile + "\t    ")
		table.suitPiles foreach (pair => out.print("" + pair._2 + " "))

		out.print("\n\n ")
		//print lane labels
		table.lanes.indices foreach (i=>out.print(WHITE_TEXT + (i + 1) + "   "))
		out.println()
		//print lanes
		(0 until lines).foreach(i => {
			table.lanes.foreach(lane => out.print("" +
				(lane.getCard(i) match
					case Some(c) => c
					case None => "   ")
				+ " "))
			out.println()
		})
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
}

object Input{
	def readCommand: String = {
		Output.printWhite("Enter move> ")
		val command = in.readLine()
		checkCommand(command)(handleError)
	}

	def checkCommand(command: String)(el: String => String): String = {
		val one = "([DQdq])".r
		val two = "([CDHPScdhps1-7]{2})".r
		val more = "([0-9]{1,2})".r
		val errorMessage = "Invalid Command, press enter to continue"

		(command.length match
			case 1 | 2 => command match
				case one(c) => c
				case two(c) => c
				case _ => el(errorMessage)
			case 3 | 4 => command.substring(2) match
				case more(c) => command.substring(0, 3)
				case _ => el(errorMessage)
			case _ => el(errorMessage)).toUpperCase
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
