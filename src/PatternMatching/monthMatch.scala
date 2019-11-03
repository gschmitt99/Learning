package PatternMatching

object monthMatch {
   def main(args: Array[String]): Unit = {
      println(getMonthName(1))
      println(getMonthName(5))
      println(getMonthName(10))
   }
   def getMonthName(month:Int) = {
      month match {
         case 1 => "Jan"
         case 2 => "Feb"
         case 3 => "Mar"
         case 4 => "Apr"
         case 5 => "May"
         case 6 => "Jun"
         case 7 => "Jul"
         case 8 => "Aug"
         case 9 => "Sep"
         case 10 => "Oct"
         case 11 => "Nov"
         case 12 => "Dec"
         case _ => "Invalid"
      }
   }
}
