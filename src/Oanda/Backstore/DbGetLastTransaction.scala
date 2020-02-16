package Oanda.Backstore

import Core.DbQuery
import Oanda.Models.Account

class DbGetLastTransaction(val database:String, val user:String, val pass:String,
                           val account:Account)
extends DbQuery(database,user,pass) {

   _query = s"select * from lasttransaction where AccountId=${account.AccountId}"

   def getLastTransactionId():Long = {
      _reader.getInt("LastTransactionId")
   }
   def getTransactionId():Long = {
      _reader.getInt("TransactionId")
   }
}
