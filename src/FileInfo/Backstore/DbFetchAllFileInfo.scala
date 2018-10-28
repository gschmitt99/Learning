package FileInfo.Backstore

import FileInfo.Models.FileInfoItem

class DbFetchAllFileInfo(val database:String, val user:String, val pass:String)
   extends DbQuery(database,user,pass) {

   _query = "select " +
      "FileInfoId," +"" +
      "HashListId," +
      "FullFileName " +
     "from fileinfo"

   def getFileInfoId() : Long = {
      return _reader.getLong("FileInfoId")
   }
   def getHashListId() : Long = {
      return _reader.getLong("HashListId")
   }
   def getFullFileName() : String = {
      return _reader.getString("FullFileName")
   }
   def getFileInfoItem() : FileInfoItem = {
      new FileInfoItem(getFileInfoId(),getHashListId(),getFullFileName())
   }
}
