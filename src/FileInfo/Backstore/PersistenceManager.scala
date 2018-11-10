package FileInfo.Backstore

import FileInfo.Models.{DirectoryInfoItem, FileInfoItem, HashListItem}

import scala.collection.mutable
import scala.collection.mutable.{Map, Set}

class PersistenceManager {
   val _database:String = "filetrack"
   val _user:String = "greg"
   val _password:String = "siboco1T"

   def getDuplicateMap():mutable.Map[HashListItem,mutable.Set[FileInfoItem]] = {
      var retval = mutable.Map[HashListItem,mutable.Set[FileInfoItem]]()
      val fileInfoList = getAllFileInfo()
      var hashIdToFileInfoMap = getHashIdToFileInfoMap(fileInfoList)
      //hashIdToFileInfoMap.foreach((key:HashListItem,value:Set[FileInfoItem]) => {
      for((key,value)<-hashIdToFileInfoMap)
      {
        if( value.size > 1)
           retval += (key -> value)
      }
      retval
   }

   def buildDirectoryMap(duplicateMap:mutable.Map[HashListItem,mutable.Set[FileInfoItem]]):mutable.Map[String,mutable.Set[DirectoryInfoItem]] = {
      var directories=mutable.Map[String,DirectoryInfoItem]()
      var dupDirectories=mutable.Map[String,mutable.Set[DirectoryInfoItem]]()
      var trimmedDupDirectories=mutable.Map[String,mutable.Set[DirectoryInfoItem]]()
      for((key,value)<-duplicateMap) {
         for( item <- value) {
            if( !directories.contains(item.getDirectoryName()))
               directories += (item.getDirectoryName() -> new DirectoryInfoItem(item.getDirectoryName()))
            directories(item.getDirectoryName()).addFile(item)
         }
      }

      for((key,value)<-directories) {
         value.computeHash()
         if(!dupDirectories.contains(value.getHash()))
            dupDirectories += (value.getHash() -> mutable.Set[DirectoryInfoItem]())
         dupDirectories(value.getHash()).add(value)
      }

      // now, think the thing to do here is find the ones with more than 1 in the list.
      for((key,value)<-dupDirectories) {
         if( value.size > 1) {
           trimmedDupDirectories += (key->value)
         }
      }
      return trimmedDupDirectories
   }
   def getHashIdToFileInfoMap(fileInfoList:mutable.Set[FileInfoItem]):mutable.Map[HashListItem,mutable.Set[FileInfoItem]] = {
      var hashListMap = mutable.Map[Long,HashListItem]()
      var retval = mutable.Map[HashListItem,mutable.Set[FileInfoItem]]()
      val hashList = getAllHashList()
      hashList.foreach((item:HashListItem) => {
         hashListMap += (item.getHashListId() -> item)
      } )

      fileInfoList.foreach((item:FileInfoItem) => {
         if(!retval.contains(hashListMap(item.hashListId))) {
            retval += (hashListMap(item.hashListId) -> mutable.Set[FileInfoItem]() )
         }
         retval(hashListMap(item.hashListId)) += item
      })

      retval
   }
   def getAllFileInfo():mutable.Set[FileInfoItem] = {
      val fetch = new DbFetchAllFileInfo(_database, _user, _password)
      var retval = mutable.Set[FileInfoItem]()
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

   def getAllHashList():mutable.Set[HashListItem] = {
      val fetch = new DbFetchAllHashList(_database,_user,_password)
      var retval = mutable.Set[HashListItem]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            retval += fetch.getHashListItem()
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      retval
   }
}
