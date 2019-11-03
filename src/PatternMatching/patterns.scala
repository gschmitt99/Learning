package PatternMatching

object patterns {
   // TODO: This could be expanded to check letters against
   // a dictionary to filter to valid results.
   def main(args: Array[String]): Unit = {
      var set1 = Array('c','i','d','e','m','n')
      GFG.printAllKLength(set1, 5)
   }
}

object GFG { // The method that prints all
   // possible strings of length k.
   // It is mainly a wrapper over
   // recursive function printAllKLengthRec()
   def printAllKLength(set: Array[Char], k: Int): Unit = {
      val n = set.length
      printAllKLengthRec(set, "", n, k)
   }

   // The main recursive method
   // to print all possible
   // strings of length k
   def printAllKLengthRec(set: Array[Char], prefix: String, n: Int, k: Int): Unit = { // Base case: k is 0,
      // print prefix
      if (k == 0) {
         System.out.println(prefix)
         return
      }
      // One by one add all characters
      // from set and recursively
      // call for k equals to k-1
      var i = 0
      while ( {
         i < n
      }) { // Next character of input added
         val newPrefix = prefix + set(i)
         // k is decreased, because
         // we have added a new character
         printAllKLengthRec(set, newPrefix, n, k - 1)

         {
            i += 1; i
         }
      }
   }
}
