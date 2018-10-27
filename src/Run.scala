// At the time I pull this out of the tutorial, I do not quite
// understand the syntax.  [A](f: =>A)
// but hopefully as more is learned, it will begin to come to light.
object Run {
   implicit class IntTimes(x: Int) {
      def times [A](f: =>A): Unit = {
         def loop(current: Int): Unit =

            if(current > 0){
               f
               loop(current - 1)
            }
         loop(x)
      }
   }
}