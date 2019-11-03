package FileInfo

import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import FileInfo.Backstore.PersistenceManager
import FileInfo.Models.{FileInfoItem, Node}
import FileInfo.TreeBuilderPackage.TreeBuilder

object Program {
   def main(args:Array[String]): Unit = {
      val manager:PersistenceManager = new PersistenceManager()
      val fileInfo = manager.getAllFileInfo()
      val tree = new Node("")
      val treeBuilder:TreeBuilder = new TreeBuilder(tree,fileInfo.toSeq)
      treeBuilder.generateTree()
      treeBuilder.analyzeTree()
      println
   }
   def createSummary():Unit = {
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
