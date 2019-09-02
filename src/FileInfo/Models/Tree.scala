package FileInfo.Models

import Core.CoreUtils

import scala.collection.mutable

abstract class Tree(){
   val files: mutable.Seq[FileInfoItem] = mutable.Seq[FileInfoItem]()
   val nodes: mutable.Map[String,Node] = mutable.Map[String,Node]()
   protected var hash:String
   def contains(key:String):Boolean
   def computeHash():Unit
   def findIdenticalNodes(list:mutable.Map[String,mutable.Seq[Node]]):Unit

   def getHash(): String = { hash }
}

case class Node(val path:String, override var hash:String="") extends Tree() with Equals {
   override def equals(obj: Any): Boolean = {
      super.equals(obj)
   }

   override def findIdenticalNodes(list:mutable.Map[String, mutable.Seq[Node]]):Unit = {
      for((k,v)<- nodes) {
         v.findIdenticalNodes(list)
      }
      if ( !list.contains(hash)) {
         list(hash) = mutable.Seq[Node]()
      }
      list(hash) :+ this
   }
   override def computeHash():Unit = {
      val builder = StringBuilder.newBuilder
      for( (k,v)<- nodes) {
         builder.append(v.computeHash())
      }
      files.foreach( (file:FileInfoItem) => builder.append(file.hashListId.toString()))
      hash = CoreUtils.MD5Hash(builder.toString())
   }

   override def contains(key: String): Boolean = {
      return nodes.contains(key)
      //nodes foreach { node =>
      //   if( node.path.equals(key)) return true
      //}
      //return false
   }
}

