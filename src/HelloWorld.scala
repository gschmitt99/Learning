import Run._

import scala.util.control.Breaks
import util.control.Breaks._

object HelloWorld  {
   def main(args:Array[String]) {
      println("Hello World")

      // prints 'Helloworld --> seems kinda useless for now.
      println(symbolLiteral())

      // execute the point demo
      //pointDemo()

      //implicitDemo()

      //interestingShift()

     //forYield()
      //breakExample1()
      //breakExample2()
      delayed(time())
   }

   def time() = {
      println("Getting time in nano seconds")
      System.nanoTime
   }
   def delayed( t: => Long ) = {
      println("In delayed method")
      println("Param: " + t)
   }

   // a different way to do the same thing
   def breakExample2(): Unit = {
      var a = 0;
      val numList = List(1,2,3,4,5,6,7,8,9,10);
      val loop = new Breaks
      // apparently, this requires import util.control.Breaks._
      loop.breakable {
         for( a <- numList) {
            println("a="+a)
            if( a == 5) loop.break
         }
      }
   }
   def breakExample1(): Unit = {
      var a = 0;
      val numList = List(1,2,3,4,5,6,7,8,9,10);
      // apparently, this requires import util.control.Breaks._
      breakable {
         for( a <- numList) {
            println("a="+a)
            if( a == 5) break
         }
      }
   }
   def forYield(): Unit = {
      var a = 0;
      val numList = List(1,2,3,4,5,6,7,8,9,10);

      // for loop execution with a yield
      var retVal = for{ a <- numList if a != 3; if a < 8 }yield a

      for( a <- retVal )
         println( "a=" + a)
   }

   def interestingShift(): Unit = {
      val A = 60
      val B = A >> 2
      val C = A >>> 2
      val D = C
   }

   def implicitDemo(): Unit = {
      4 times println("Hello")
   }

   def pointDemo(): Unit = {
      val pt = new Point(10,20)
      //pt.move(2,5)
      //pt.move(-1,-2)
      val loc = new Location(10,20, 30)
      loc.move(-1,-2, -4)
   }
   // symbol literal; a string shorthand?
   def symbolLiteral(): Symbol = {
      val aString = 'HelloWorld
      aString
   }
}
