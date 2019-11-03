package Collections.Lists

class ListExamples {
   val theList = List( "Cool", "tools", "rule")

   def createNew() : Unit = {
      // creation of list with ::
      // the reason Nil is required at the ind is :: is defined on class
      // List.  "until" is a string, and :: is not defined there.
      val thrill = "Will" :: "fill" :: "until" :: Nil
      val thrillBase = List( "Will", "fill", "until")

      assert( thrill.equals(thrillBase))

      // appending two lists.
      val appender = List( "a", "b" ) ::: List( "c", "d")
      assert( appender.equals( List("a","b","c","d")))

      // indexing
      assert(thrill(2).equals("until"))

      // counting with a filter.
      val thrillC = thrill.count(s => s.length == 4)
      assert( thrillC.equals(2))

      // returns the list without its first two elements.
      val thrill1 = thrill.drop(2)
      assert( thrill1.equals(List("until")))
      // returns the list without its rightmost 2 elements.
      val thrill2 = thrill.dropRight(2)
      assert(thrill2.equals(List("Will")))

      // determines whether a string element exists in thrill that
      // has the value "until"; should be true.
      assert( thrill.exists(s => s == "until") )

      /** Selects all elements of this $coll which satisfy a predicate.
        *
        *  @param p     the predicate used to test elements.
        *  @return      a new $coll consisting of all elements of this $coll that satisfy the given
        *               predicate `p`. The order of the elements is preserved.
        */
      val thrill3 = thrill.filter( p => p.length == 4)
      assert( thrill3.equals(List("Will","fill")))

      //forAll(thrill, s => endsWith("l"))
      // this is the forall definition in List (actually from a different trait List implements)
      // I don't yet understand the head and tail
      //def forall(theList:List[String], p: A => Boolean): Boolean = {
      //   var these = theList
      //   while (!these.isEmpty) {
      //      if (!p(these.head)) return false
      //      these = these.tail
      //   }
      //   true
     // }
      assert( thrill.forall(s => s.endsWith("l")).equals(true))

      // prints each element of the list
      thrill.foreach(s => print(s))

      // same as the previous, but more concise
      thrill.foreach(print)

      assert(!thrill.isEmpty)

      // returns the last element in the list
      assert(thrill.last.equals("until"))

      // returns the number of elements in the list
      assert( thrill.length.equals(3))

      // returns a List resulting from adding "y" to each element
      assert( thrill.map( s => s + "y").equals( List("Willy", "filly", "untily")))

      // appears remove was depricated.  Probably use filter rather.
      //assert( thrill.remove( s => s.length == 4).equals(List("until")))

      assert( thrill.reverse.equals( List("until", "fill", "Will")))

      val sortedThrill = thrill.sortWith((s,t) => s.charAt(0).toLower < t.charAt(0).toLower)
      assert(sortedThrill.equals(List("fill", "until", "Will")))

      val tailThrill = thrill.tail
      assert( tailThrill.equals(List("fill","until")))

      val headThrill = thrill.head

      val r=45
      assert(r.equals(45))
   }

   def mapExamples():Unit = {
      val a = List(1,2,3).map(_ + 1)
      assert(a.equals(List(2,3,4)))
      val b = List("the", "quick", "brown", "fox").map(_.length)
      assert(b.equals(List(3,5,5,3)))
      val c = List("the", "quick", "brown", "fox").map(_.reverse.mkString(""))
      assert(c.equals(List("eht","kciuq","nworb","xof")))
   }

   def flatMapExamples():Unit = {
      val a = List.range(1,5)
      val b = a flatMap ( i => {
         println("flat: i="+i)
         List.range(1, i).map(j=> {
            println("map: i="+i+" j="+j)
            (i, j)
         })
      })
      //a.foreach
      val c = b
      //val c = b map ( j => (i,j))
      //val d = c
   }

}

object ListExamples {
   def main(args:Array[String]): Unit ={
      val examples = new ListExamples
      examples.createNew()
      examples.flatMapExamples()
   }
}
