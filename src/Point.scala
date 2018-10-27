import java.io._

class Point(xc: Int,yc:Int ) {
   var x: Int = xc
   var y: Int = yc
   def move(dx:Int, dy:Int): Unit = {
      x = x + dx
      y = y + dy
      println( "x="+x)
      println( "y="+y)
   }
}

// this was the definition from the example, however will not
// compile.
//class Location( override val xc: Int, override val yc: Int,
class Location( val xc: Int, val yc: Int,
               val zc :Int) extends Point(xc, yc){
   var z: Int = zc

   def move(dx: Int, dy: Int, dz: Int) {
      x = x + dx
      y = y + dy
      z = z + dz
      println ("Point x location : " + x);
      println ("Point y location : " + y);
      println ("Point z location : " + z);
   }
}


