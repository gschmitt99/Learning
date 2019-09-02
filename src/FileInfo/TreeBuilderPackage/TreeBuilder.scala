package FileInfo.TreeBuilderPackage

import FileInfo.Models.{FileInfoItem, Node, Tree}

import scala.collection.mutable

class TreeBuilder(tree:Tree, fileInfo:Seq[FileInfoItem]) {
   def analyzeTree():Unit = {
      tree.computeHash()
      println
   }
   def findIdenticalBranches():Unit = {
      val theList=mutable.Map[String,mutable.Seq[Node]]()
      tree.findIdenticalNodes(theList)

      // the next thing to do is find which hashes have more than one node.
   }

   def generateTree():Unit = {
      fileInfo foreach {
         item =>
            val parts = item.fullFileName.split("/")
            generateNode(tree,item,parts)
            println
      }
   }

   def generateNode(tree:Tree,fileInfo:FileInfoItem,parts:Array[String]):Unit = {
      if( parts.length > 1 ) {
         val newParts = parts.slice(1, parts.length)
         if (tree.contains(parts(0))) {
            generateNode(tree.nodes(parts(0)), fileInfo, newParts)
         } else {
            val newNode = new Node(parts(0))
            tree.nodes.put(parts(0), newNode)
            generateNode(newNode, fileInfo, newParts)
         }
      } else {
         tree.files :+ fileInfo
      }
   }
}
