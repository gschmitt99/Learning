package FileInfo

import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import FileInfo.Backstore.PersistenceManager
import FileInfo.Models.FileInfoItem

object Program {
   def main(args:Array[String]): Unit = {
      var item:PersistenceManager = new PersistenceManager()
      //var fileInfoList:Set[FileInfoItem] = item.getAllFileInfo()
      val duplicateMap = item.getDuplicateMap()
      var output=item.buildDirectoryMap(duplicateMap)
      var builder = new StringBuilder()
      for((key,value)<-output) {
         for( item <-value) {
            builder.append(key + ",")
            builder.append(item.getDirectoryName + "\n")
         }
         builder.append("\n")
      }

      Files.write(Paths.get("c:\\log\\summary.txt"), builder.toString().getBytes(StandardCharsets.UTF_8))
      //new PrintWriter( "c\\log\\summary.txt") {write(builder.toString()) }
   }
}
