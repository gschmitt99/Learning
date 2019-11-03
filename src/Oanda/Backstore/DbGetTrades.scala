package Oanda.Backstore

import java.util.Date

import Core.DbQuery

class DbGetTrades(val database:String, val user:String, val pass:String, val accountId:Long, val instrument:String)
extends DbQuery(database,user,pass) {
   _query = s"select * from trades where Accountid=${accountId} and Instrument='${instrument}'"

   def getTradeId() : Long = {
      return _reader.getLong("TradeId")
   }
   def getAccountId() : Long = {
      return _reader.getLong("AccountId")
   }
   def getInstrument() : String = {
      return _reader.getString("Instrument")
   }
   def getSequence() : Long = {
      return _reader.getLong("Sequence")
   }
   def getIsBuy() : Boolean = {
      return _reader.getBoolean("IsBuy")
   }
   def getQuantity() : Long = {
      return _reader.getLong("Quantity")
   }
   def getClosedProfit() : Double = {
      return _reader.getDouble("ClosedProfit")
   }
   def getEntryFillId() : Long = {
      return _reader.getLong("EntryFillId")
   }
   def getEntryFillTime() : Date = {
      return _reader.getDate("EntryFillTime")
   }
   def getEntryFillPrice() : Double = {
      return _reader.getDouble("EntryFillPrice")
   }
   def getExitFillId() : Long = {
      return _reader.getLong("ExitFillId")
   }
   def getExitFillTime() : Date = {
      return _reader.getDate("ExitFillTime")
   }
   def getExitFillPrice() : Double = {
      return _reader.getDouble("ExitFillPrice")
   }
}
