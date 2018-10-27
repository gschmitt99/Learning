package Collections

object MapDemo {
   def main(args: Array[String]) {
      // immutable
      val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF", "peru" -> "#CD853F")

      //mutable
      var nums: Map[Int, Int] = Map()
      nums += (5 -> 7)

      println( "Keys in colors : " + colors.keys )
      println( "Values in colors : " + colors.values )
      println( "Check if colors is empty : " + colors.isEmpty )
      println( "Check if nums is empty : " + nums.isEmpty )

   }
}
