package FileInfo.Models

import scala.collection.SortedSet
import Core.CoreUtils

class DirectoryInfoItem(directoryName:String) {
   private val _directoryName:String = directoryName
   private var _files:SortedSet[FileInfoItem] = SortedSet[FileInfoItem]()
   private var _hash:String = null

   def getDirectoryName:String = {_directoryName}
   def getHash():String = {_hash}
   def addFile(file:FileInfoItem) = {
      _files += file
   }

   def computeHash() = {
      var builder = StringBuilder.newBuilder
      _files.foreach((file:FileInfoItem) => builder.append(file.hashListId.toString()))
      _hash=CoreUtils.MD5Hash(builder.toString())
   }
}
