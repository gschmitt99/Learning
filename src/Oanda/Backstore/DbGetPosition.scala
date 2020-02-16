package Oanda.Backstore

import Core.DbQuery

class DbGetPosition(val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   def getPositionId() : Long = {
      return _reader.getLong("PositionId")
   }
   def getAccountId() : Long = {
      return _reader.getLong("AccountId")
   }
   def getInstrument() : String = {
      return _reader.getString("Instrument")
   }
   def getPosition() : Long = {
      return _reader.getLong("Position")
   }
}
