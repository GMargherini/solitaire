package patience.core

import java.util.NoSuchElementException
import scala.collection.immutable.ListMap

class History {
	private var log: Map[Int,(String, Int)] = Map[Int,(String, Int)]()
	def record(n:Int, move:String, score: Int): Unit = {
		log = log + (n -> (move, score))
	}
	def updateScore(n: Int, score: Int): Unit = {
		log = log + (n -> (log(n)._1, score))
	}
	def delete(n:Int): Unit = {
		log = log.filterNot((x,_) => x == n)
	}
	def lastMove(moves:Int): String = {
		try {
			log(moves)._1
		} catch
			case e: NoSuchElementException => ""
	}
	def lastScore(moves:Int):Int = {
		try {
			log(moves)._2
		} catch
			case e: NoSuchElementException => 0
	}

	override def toString: String = {
		var res=""
		log = ListMap(log.toSeq.sortWith(_._1 < _._1)*)
		log foreach ((n,m)=>res = res + "("+n+", "+m+")\n")
		res
	}
}
