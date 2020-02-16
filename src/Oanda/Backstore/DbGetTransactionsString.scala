package Oanda.Backstore

import java.sql.Timestamp
import java.util.Date

import Core.DbQuery
import Oanda.Models.{Account, LastTransaction}

class DbGetTransactionsString(
                                val database:String,
                                val user:String,
                                val pass:String,
                                val account:Account,
                                val lastTransaction:Option[LastTransaction]
                             )
extends DbQuery(database,user,pass) {

   if( lastTransaction.isDefined ) {
      _query = s"select * from transactions where AccountId=${account.AccountId} and TransactionId>'${lastTransaction.get.TransactionId}'"
   } else {
      _query = s"select * from transactions where AccountId=${account.AccountId}"

   }
   def getTransactionId():Long = {
      _reader.getLong("TransactionId")
   }
   def getAccountId():Long = {
      _reader.getLong("AccountId")
   }
   def getTicket() : String = {
      val v = _reader.getString("Ticket")
      return if( v != null) v else ""
   }
   def getDate() : String = {
      val v = _reader.getString("Date")
      return if( v != null) v else ""
   }
   def getTimezone() : String = {
      val v = _reader.getString("Timezone")
      return if( v != null) v else ""
   }
   def getTransaction() : String = {
      val v = _reader.getString("Transaction")
      return if( v != null) v else ""
   }
   def getDetails() : String = {
      val v = _reader.getString("Details")
      return if( v != null) v else ""
   }
   def getInstrument() : String = {
      val v = _reader.getString("Instrument")
      return if( v != null) v else ""
   }
   def getPrice() : String = {
      val v = _reader.getString("Price")
      return if( v != null) v else ""
   }
   def getUnits() : String = {
      val v = _reader.getString("Units")
      return if( v != null) v else ""
   }
   def getDirection() : String = {
      val v = _reader.getString("Direction")
      return if( v != null) v else ""
   }
   def getSpreadCost() : String = {
      val v = _reader.getString("SpreadCost")
      return if( v != null) v else ""
   }
   def getStopLoss() : String = {
      val v = _reader.getString("StopLoss")
      return if( v != null) v else ""
   }
   def getTakeProfit() : String = {
      val v = _reader.getString("TakeProfit")
      return if( v != null) v else ""
   }
   def getTrailingStop() : String = {
      val v = _reader.getString("TrailingStop")
      return if( v != null) v else ""
   }
   def getFinancing() : String = {
      val v = _reader.getString("Financing")
      return if( v != null) v else ""
   }
   def getCommission() : String = {
      val v = _reader.getString("Commission")
      return if( v != null) v else ""
   }
   def getProfitLoss() : String = {
      val v = _reader.getString("ProfitLoss")
      return if( v != null) v else ""
   }
   def getAmount() : String = {
      val v = _reader.getString("Amount")
      return if( v != null) v else ""
   }
   def getBalance() : String = {
      val v = _reader.getString("Balance")
      return if( v != null) v else ""
   }
}
