package FileInfo.Models

import java.security.MessageDigest

import scala.collection.SortedSet

class DirectoryInfoItem(directoryName:String,hash:String) {
   private val _directoryName:String = directoryName
   private var _files:SortedSet[FileInfoItem] = SortedSet[FileInfoItem]()
   private var _hash:String = null

   def getDirectoryName:String = {_directoryName}
   def getHash:String = {_hash}
   def addFile(file:FileInfoItem) = {
      _files += file
   }
   def computeHash() = {
      var builder = StringBuilder.newBuilder
      _files.foreach((file:FileInfoItem) => builder.append(file.getHashListId().toString()))
      _hash = MessageDigest.getInstance("MD5").digest(builder.toString().getBytes()).toString
   }
}
