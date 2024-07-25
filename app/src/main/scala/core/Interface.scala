package core

import deck.Suit

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
		for (suit <- Suit.values) {
			out.print("" +
				(table.suitPiles.get(suit) match
					case Some(x) => x
					case None => "   ")
				+ " ")
		}
		out.print("\n\n ")
		//print lane labels
		for (i <- table.lanes.indices) {
			out.print(WHITE_TEXT + (i + 1) + "   ")
		}
		out.println()
		//print lanes
		for (i <- 0 until lines) {
			for (lane <- table.lanes) {
				out.print("" +
					(lane.getCard(i) match
						case Some(c) => c
						case None => "   ")
					+ " ")
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
}

object Input{
	def readCommand: String = {
		Output.printWhite("Enter move> ")
		val move = in.readLine()
		readCommand(move)(handleError)
	}

	def readCommand(move: String)(el: String => String): String = {
		var mv = move
		if (move.length >= 3 && move.substring(2).matches("[0-9]{1,2}")) mv = move.substring(0, 3)
		else if (!move.matches("[CDHPScdhps1-7]{2}") & !move.matches("[DQdq]")) {
			el("Invalid Command, press enter to continue")
			mv = ""
		}
		mv.toUpperCase
	}

	def handleError(message: String): String = {
		Output.printRed(message)
		in.readLine()
	}
	def handleError():String = {
		Output.printRed("Illegal move, press enter to continue")
		in.readLine()
	}
}
