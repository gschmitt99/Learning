package Collections

object TupleDemo {
   def main(args: Array[String]) {
      val t = new Tuple3(1, "hello", Console)

      println("Concatenated String: " + t.toString() )
   }
}
