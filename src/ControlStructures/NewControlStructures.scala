package ControlStructures

import java.io.{File, PrintWriter}


object NewControlStructures {
   def twice(op:Double=>Double,x:Double) = op(op(x))

   def withPrintWriter(file:File, op:PrintWriter=>Unit): Unit = {
      val writer = new PrintWriter(file)
      try{
         op(writer)
      } finally {
         writer.close
      }
   }

   def withPrintWriterControl(file:File)(op:PrintWriter => Unit) {
      val writer = new PrintWriter(file)
      try{
         op(writer)
      } finally {
         writer.close
      }
   }

   def main(args:Array[String]) = {
      println{ "hello world"}
      println(twice(_+1, 5))
      withPrintWriter(
         new File("e:\\work\\programminglanguages\\date.txt"),
         writer => writer.println(new java.util.Date)
      )

      val file = new File("e:\\work\\programminglanguages\\date.txt")
      withPrintWriterControl(file) {
         writer => writer.println(new java.util.Date)
      }
   }

}
