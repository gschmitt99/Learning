package FileInfo.Models

class HashListItem(hashListId:Long,md5Hash:String,fileSize:Long) {
   private val _hashListId = hashListId
   private val _md5Hash = md5Hash
   private val _fileSize = fileSize

   def getHashListId():Long = { _hashListId }
   def getMD5Hash():String = { _md5Hash }
   def getFileSize():Long = { _fileSize }
}
