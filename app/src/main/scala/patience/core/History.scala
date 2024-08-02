package patience.core

import java.util.NoSuchElementException
import scala.collection.immutable.ListMap

class History {
	private var log: Map[Int,String] = Map[Int,String]()
	def record(n:Int,move:String): Unit = {
		log = log + (n -> move)
	}
	def delete(n:Int): Unit = {
		log = log.filterNot((x,_) => x == n)
	}
	def lastMove(moves:Int): String = {
		try {
			log(moves)
		} catch
			case e: NoSuchElementException => ""

	}

	override def toString: String = {
		var res=""
		log = ListMap(log.toSeq.sortWith(_._1 < _._1)*)
		log foreach ((n,m)=>res = res + "("+n+", "+m+")\n")
		res
	}
}
