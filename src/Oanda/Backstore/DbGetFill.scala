package Oanda.Backstore

import java.util.Date
import Core.DbQuery

class DbGetFill(val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   def getFillId(): Long = {
      return _reader.getLong("FillId")
   }
   def getTicket(): Long = {
      return _reader.getLong("Ticket")
   }
   def getAccountId() : Long = {
      return _reader.getLong("AccountId")
   }
   def getFillDate() : Date = {
      return _reader.getDate("Date")
   }
   def getInstrument() : String = {
      return _reader.getString("Instrument")
   }
   def getPrice() : Double = {
      return _reader.getDouble("Price")
   }
   def getVolume() : Int = {
      return _reader.getInt("Volume")
   }
   def getDirection() : String = {
      return _reader.getString("Direction")
   }
   def getSpreadCost() : Double = {
      return _reader.getDouble("SpreadCost")
   }
   def getFinancing() : Double = {
      return _reader.getDouble("Financing")
   }
}
