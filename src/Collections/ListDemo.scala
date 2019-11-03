package Collections

object ListDemo {
   def main(args: Array[String]): Unit = {
      tabulateExample()
   }

   def tabulateExample(): Unit = {
      // Creates 5 elements using the given function.
      val squares = List.tabulate(6)(n => n * n)
      println( "squares : " + squares  )

      // good documentation of the List type was found here:
      // https://www.scala-lang.org/docu/files/collections-api/collections_45.html
      // tabulate(m,n){f} gives a sequence of dimensions m by n where
      // each index is computed by f(i,j)
      // therefore, the below will create the following matrix
      // more correctly, it will be a list with 4 elements
      // each having a list of 5 elements as shown.
      // - 0 1 2 3 4
      // 0 0 0 0 0 0
      // 1 0 1 2 3 4
      // 2 0 2 4 6 8
      // 3 0 3 6 9 12
      val mul = List.tabulate( 4,5 )( _ * _ )
      println( "mul : " + mul  )
   }
   def uniformList(): Unit = {
      val fruit = List.fill(3)("apples") // Repeats apples three times.
      println( "fruit : " + fruit  )

      val num = List.fill(10)(2)         // Repeats 2, 10 times.
      println( "num : " + num  )
   }

   def concatList(): Unit = {
      val fruit1 = "apples" :: ("oranges" :: ("pears" :: Nil))
      val fruit2 = "mangoes" :: ("banana" :: Nil)

      // use two or more lists with ::: operator
      var fruit = fruit1 ::: fruit2
      println( "fruit1 ::: fruit2 : " + fruit )

      // use two lists with Set.:::() method
      fruit = fruit1.:::(fruit2)
      println( "fruit1.:::(fruit2) : " + fruit )

      // pass two or more lists as arguments
      fruit = List.concat(fruit1, fruit2)
      println( "List.concat(fruit1, fruit2) : " + fruit  )
   }

   def createList() {
      val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
      val nums = Nil

      println( "Head of fruit : " + fruit.head )
      println( "Tail of fruit : " + fruit.tail )
      println( "Check if fruit is empty : " + fruit.isEmpty )
      println( "Check if nums is empty : " + nums.isEmpty )
   }
}
