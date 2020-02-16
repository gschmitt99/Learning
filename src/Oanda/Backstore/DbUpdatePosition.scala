package Oanda.Backstore

import java.text.SimpleDateFormat

import Core.DbUpdate
import Oanda.Models.Position

class DbUpdatePosition(val database:String, val user:String, val pass:String, val position:Position)
extends DbUpdate(database,user,pass) {

   _query = {
      "update positions set " +
         s"AccountId=${position.AccountId}, " +
         s"Instrument='${position.Instrument}', " +
         s"Position=${position.Position} " +
         s"where PositionId=${position.PositionId}"
   }
}
