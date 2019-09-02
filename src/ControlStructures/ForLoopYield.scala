package ControlStructures

object ForLoopYield {
   def sum(a:Int,b:Int,c:Int) = a + b + c

   def partiallyAppliedFunctionTest() = {
      val a = sum _
      val theSum = a(1,2,4)
      //syntax error
      //val theSum1 = a(1,2,4,5)
   }
   def main(args:Array[String]) = {
      def multiTable = {
         val table = for( i <- 1 to 10) yield {
            val row = for( j <- 1 to 10) yield  {
               String.format("%4s", (i*j).toString)
            }
            row.mkString + '\n'
         }
         table.mkString
      }
      println(multiTable)
   }

}
