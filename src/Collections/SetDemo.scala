package Collections

// a set is a collection of pairwise different elements.  They can
// be mutable or immutable.
object SetDemo {
   def main(args: Array[String]) {
      val fruit = Set("apples", "oranges", "pears")
      val nums = Set(4,5,6)
      val nums1= Set(4,5,6,7)

      // notice here, get 4 elements only
      val set2  = nums ++ nums1

      println( "Head of fruit : " + fruit.head )
      println( "Tail of fruit : " + fruit.tail )
      println( "Check if fruit is empty : " + fruit.isEmpty )
      println( "Check if nums is empty : " + nums.isEmpty )
   }
}
