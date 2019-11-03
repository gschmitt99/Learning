package Oanda.Backstore

import java.sql.Timestamp

import Core.DbQuery

class DbGetMaxTransactionDate(val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   _query = "select max(Date) as MaxDate from transactions"
   def getMaxDate() : Timestamp = {
      _reader.getTimestamp("MaxDate")
   }
}
