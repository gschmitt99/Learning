package PatternMatching

object PatternMatchingDemo {
   def main(args: Array[String]): Unit = {
      val times = 1

      times match {
         case 1 => println("one")
         case 2 => println("two")
         case _ =>println( "some other number")
      }
   }
}
