package FileInfo.Models

import java.io.File

class FileInfoItem( fileInfoId:Long, hashListId:Long, fullFileName:String ) extends Ordered[FileInfoItem] {
   private val _fileInfoId = fileInfoId
   private val _hashListId = hashListId
   private val _fullFileName = fullFileName

   def getFileInfoId():Long = { _fileInfoId }
   def getHashListId():Long = { _hashListId }
   def getFullFileName():String = { _fullFileName }
   def getDirectoryName():String = {
      val f:File = new File(_fullFileName)
      f.getParentFile().getName
   }

   // Ordered trait implementation
   def compare(that:FileInfoItem) = {
      if( this._hashListId == that._hashListId )
         0
      else if( this._hashListId > that._hashListId )
        1
      else
         -1
   }
}
