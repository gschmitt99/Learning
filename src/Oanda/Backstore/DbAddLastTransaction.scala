package Oanda.Backstore

import Core.DbUpdate
import Oanda.Models.{LastTransaction}

class DbAddLastTransaction(val database:String, val user:String, val pass:String,
                           val lastTransaction:LastTransaction )
extends DbUpdate(database,user,pass) {

   _query = {
      "insert into lasttransaction (" +
         "AccountId," +
         "TransactionId" +
         s") values (${lastTransaction.AccountId}," +
         s"'${lastTransaction.TransactionId}')"
   }
}
