package FileInfo

import FileInfo.Backstore.PersistenceManager
import FileInfo.Models.FileInfoItem

object Program {
   def main(args:Array[String]): Unit = {
      var item:PersistenceManager = new PersistenceManager()
      var fileInfoList:Set[FileInfoItem] = item.getAllFileInfo()
      var y:Int=3
      y = 5
   }
}
