package Oanda.Backstore

import Core.DbQuery

class DbSimpleTest( val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   _query = "select * from test"
   def getFirstCol() : Long = {
      return _reader.getLong("FirstCol")
   }
}
