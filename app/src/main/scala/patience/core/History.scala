package patience.core

import scala.collection.immutable.ListMap

class History {
	var log: Map[Int,String] = Map[Int,String]()
	def record(n:Int,move:String): Unit = {
		log = log + (n -> move)
	}

	override def toString: String = {
		var res=""
		log = ListMap(log.toSeq.sortWith(_._1 < _._1):_*)
		log foreach ((n,m)=>res = res + "("+n+", "+m+")\n")
		res
	}
}
