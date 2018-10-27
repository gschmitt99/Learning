object AnonymousFunctionDemo {
   def main(args: Array[String]): Unit = {
      val inc = (x:Int) => x+1
      val x = inc(7)-1
      val mul = (x:Int,y:Int) => x*y
      val z = mul(3,6)
      println( "x=" + x)
      println( "z=" + z)
   }
}
