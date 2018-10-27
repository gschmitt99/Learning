package Collections

object OptionDemo {
   def main(args: Array[String]) {
      val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")

      println("show(capitals.get( \"Japan\")) : " + show(capitals.get( "Japan")) )
      println("show(capitals.get( \"India\")) : " + show(capitals.get( "India")) )
      getOrElseDemo()
   }

   def show(x: Option[String]) = x match {
      case Some(s) => s
      case None => "?"
   }

   def getOrElseDemo(): Unit = {
      val a:Option[Int] = Some(5)
      val b:Option[Int] = None

      println("a.getOrElse(0): " + a.getOrElse(0) )
      println("a.getOrElse(1): " + a.getOrElse(3) )
      println("b.getOrElse(10): " + b.getOrElse(10) )
   }

}
