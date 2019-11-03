package FileInfo.Models

import java.io.File

case class FileInfoItem( fileInfoId:Long, hashListId:Long, fullFileName:String ) extends Ordered[FileInfoItem] {
   /*
   private val _fileInfoId = fileInfoId
   private val _hashListId = hashListId
   private val _fullFileName = fullFileName

   def getFileInfoId(): Long = {
      _fileInfoId
   }

   def getHashListId(): Long = {
      _hashListId
   }

   def getFullFileName(): String = {
      _fullFileName
   }

   */
   def getDirectoryName(): String = {
      val i = fullFileName.lastIndexOf("/")
      return fullFileName.substring(0,i)
   }

   // Ordered trait implementation
   def compare(that: FileInfoItem) = {
      hashListId.compareTo(that.hashListId)
   }
}
