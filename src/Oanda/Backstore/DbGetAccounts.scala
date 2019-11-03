package Oanda.Backstore

import Core.DbQuery

class DbGetAccounts(val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   _query = "select * from accounts"
   def getAccountId() : Long = {
      return _reader.getLong("AccountId")
   }
   def getAccountName() : String = {
      return _reader.getString("AccountName")
   }
}
