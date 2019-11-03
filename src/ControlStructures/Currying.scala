package ControlStructures

object Currying {
   def plainOldSum(x:Int,y:Int):Int = x + y
   def curriedSum(x:Int)(y:Int):Int = x + y
   // this is more explicitly showing what is happening with the currying sum
   def first(x:Int):Int=>Int = (y:Int) => x + y
   def main(args:Array[String]):Unit = {
      println(plainOldSum(1,2))
      println(curriedSum(1)(2))
      println(first(1)(2))

      val onePlus = curriedSum(1)_
      val twoPlus = curriedSum(2)_

   }
}
