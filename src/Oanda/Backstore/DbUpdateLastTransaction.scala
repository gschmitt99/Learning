package Oanda.Backstore

import Core.DbUpdate
import Oanda.Models.{LastTransaction}

class DbUpdateLastTransaction(val database:String, val user:String, val pass:String,
                              val lastTransaction:LastTransaction)
extends DbUpdate(database,user,pass) {

   _query = {
      "update lasttransaction set " +
         s"AccountId=${lastTransaction.AccountId}, " +
         s"TransactionId='${lastTransaction.TransactionId}' " +
         s"where LastTransactionId=${lastTransaction.LastTransactionId}"
   }
}
