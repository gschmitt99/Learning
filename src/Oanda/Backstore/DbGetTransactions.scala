package Oanda.Backstore

import java.util.Date

import Core.DbQuery

class DbGetTransactions(val database:String, val user:String, val pass:String)
extends DbQuery(database,user,pass) {

   _query = "select * from transactions"
   def getTicket() : Long = {
      return _reader.getLong("Ticket")
   }
   def getDate() : Date = {
      return _reader.getDate("Date")
   }
   def getTimezone() : String = {
      return _reader.getString("Timezone")
   }
   def getTransaction() : String = {
      return _reader.getString("Transaction")
   }
   def getDetails() : String = {
      return _reader.getString("Details")
   }
   def getInstrument() : String = {
      return _reader.getString("Instrument")
   }
   def getPrice() : Double = {
      return _reader.getDouble("Price")
   }
   def getUnits() : Long = {
      return _reader.getLong("Units")
   }
   def getDirection() : String = {
      return _reader.getString("Direction")
   }
   def getSpreadCost() : Double = {
      return _reader.getDouble("SpreadCost")
   }
   def getStopLoss() : Double = {
      return _reader.getDouble("StopLoss")
   }
   def getTakeProfit() : Double = {
      return _reader.getDouble("TakeProfit")
   }
   def getTrailingStop() : Double = {
      return _reader.getDouble("TrailingStop")
   }
   def getFinancing() : Double = {
      return _reader.getDouble("Financing")
   }
   def getCommission() : Double = {
      return _reader.getDouble("Commission")
   }
   def getProfitLoss() : Double = {
      return _reader.getDouble("ProfitLoss")
   }
   def getAmount() : Double = {
      return _reader.getDouble("Amount")
   }
   def getBalance() : Double = {
      return _reader.getDouble("Balance")
   }
}
