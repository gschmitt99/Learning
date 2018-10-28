package FileInfo.Backstore

import FileInfo.Models.FileInfoItem

class PersistenceManager {
   val _database:String = "filetrack"
   val _user:String = "greg"
   val _password:String = "siboco1T"

   def getAllFileInfo():Set[FileInfoItem] = {
      val fetch:DbFetchAllFileInfo = new DbFetchAllFileInfo(_database, _user, _password)
      var retval:Set[FileInfoItem] = Set[FileInfoItem]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            retval += fetch.getFileInfoItem()
         }
      } catch {
          case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      retval
   }
}
