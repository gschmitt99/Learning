package ControlStructures

import java.io.File

object FileMatcher {
   private def filesHere:Array[File] = (new File(".")).listFiles()

   def filesEnding(query:String):Array[File] =
      for( file <- filesHere; if file.getName.endsWith(query))
         yield file

   def filesContaining(query:String):Array[File] =
      for( file <- filesHere; if file.getName.contains(query))
         yield file

   def filesRegex(query:String):Array[File] =
      for( file <- filesHere; if file.getName.matches(query))
         yield file

   // higher order implementation; can replace all 3, and below
   // is an example of how to call it.
   def filesMatching(
     query:String,
     matcher:(String,String) => Boolean
   ):Array[File] =
      for( file <- filesHere; if matcher(file.getName,query))
         yield file

   def shorterFilesMatching(matcher:String => Boolean):Array[File] =
      for( file <- filesHere; if( matcher(file.getName)))
         yield file

}
object HigherOrderFunction {
   def main( args:Array[String]): Unit = {
      // all calls are doing the same thing, just using placeholder syntax on the first call.
      FileMatcher.filesMatching("*",_.matches(_))
      FileMatcher.filesMatching(".scala", (fileName:String,query:String) => fileName.endsWith(query))
      FileMatcher.filesMatching(".scala", (fileName,query) => fileName.contains(query))

      // the query is passed directly to the matching method rather than to the wrapper method
      FileMatcher.shorterFilesMatching(_.matches("*"))
      FileMatcher.shorterFilesMatching((fileName:String)=>fileName.matches("*"))
      FileMatcher.shorterFilesMatching((fileName)=>fileName.contains("*"))
   }
}
