package Oanda.Backstore

import Core.DbUpdate
import Oanda.Models.Position

class DbAddPosition(val database:String, val user:String, val pass:String, val position:Position)
extends DbUpdate(database,user,pass) {

   _query = {
      "insert into positions (" +
         "AccountId," +
         "Instrument," +
         "Position" +
         s") values (${position.AccountId}," +
         s"'${position.Instrument}'," +
         s"${position.Position})"
   }
}
