package FileInfo.Backstore

import FileInfo.Models.{FileInfoItem, HashListItem}

class DbFetchAllHashList(val database:String, val user:String, val pass:String)
   extends DbQuery(database,user,pass) {

   _query = "select " +
      "HashListId," +"" +
      "MD5Hash," +
      "FileSize " +
     "from hashlist"

   def getHashListId() : Long = {
      return _reader.getLong("HashListId")
   }
   def getMD5Hash() : String = {
      return _reader.getString("MD5Hash")
   }
   def getFileSize() : Long = {
      return _reader.getLong("FileSize")
   }
   def getHashListItem() : HashListItem = {
      new HashListItem(getHashListId(),getMD5Hash(),getFileSize())
   }
}
