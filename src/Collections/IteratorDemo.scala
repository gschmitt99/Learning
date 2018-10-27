package Collections

object IteratorDemo {
   def main(args: Array[String]) {
      val ita = Iterator(20,40,2,50,69, 90)
      val itb = Iterator(20,40,2,50,69, 90)

      while( ita.hasNext ){
         println(ita.next())
      }
      println("Value of ita.size : " + ita.size )
      println("Value of itb.length : " + itb.length )

      val hmm = itb.hasNext

   }
}
