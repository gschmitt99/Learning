package Oanda.Backstore

import java.util.Date
import java.text.SimpleDateFormat

import Core.DbUpdate
import Oanda.Models.{Account, DatabaseTransactionRecord}

class DbAddTransaction(
                         val database:String
                         ,val user:String
                         ,val pass:String
                      ,val account:Account
                         ,val rec:DatabaseTransactionRecord)
extends DbUpdate(database,user,pass) {

   _query = {
      val _queryFormat = "insert into transactions (" +
         "AccountId," +
         "Ticket," +
         "Date," +
         "Timezone," +
         "Transaction," +
         "Details," +
         "Instrument," +
         "Price," +
         "Units," +
         "Direction," +
         "SpreadCost," +
         "StopLoss," +
         "TakeProfit," +
         "TrailingStop," +
         "Financing," +
         "Commission," +
         "ProfitLoss," +
         "Amount," +
         "Balance" +
         ") values (%d,%s,'%s','%s','%s','%s','%s',%s,'%s',%s,%s,%s,%s,%s,%s,%s,%s)"

         val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
         //val ticket = rec.Ticket.get.toString()
         val Ticket = if( rec.Ticket.isDefined ) rec.Ticket.get.toString() else "NULL"
         val Date = format.format(rec.Date)
         val Timezone = rec.Timezone
         val Transaction = rec.Transaction
         val Details = rec.Details
         val Instrument = rec.Instrument
         val Price= if( rec.Price.isDefined ) rec.Price.get.toString else "NULL"
         val Units = if( rec.Units.isDefined ) rec.Units.get.toString else "NULL"
         val Direction = rec.Direction
         val SpreadCost= if( rec.SpreadCost.isDefined ) rec.SpreadCost.get.toString else "NULL"
         val StopLoss= if( rec.StopLoss.isDefined ) rec.StopLoss.get.toString else "NULL"
         val TakeProfit= if( rec.TakeProfit.isDefined ) rec.TakeProfit.get.toString else "NULL"
         val TrailingStop= if( rec.TrailingStop.isDefined ) rec.TrailingStop.get.toString else "NULL"
         val Financing= if( rec.Financing.isDefined ) rec.Financing.get.toString else "NULL"
         val Commission= if( rec.Commission.isDefined ) rec.Commission.get.toString else "NULL"
         val ProfitLoss= if( rec.ProfitLoss.isDefined ) rec.ProfitLoss.get.toString else "NULL"
         val Amount= if( rec.Amount.isDefined ) rec.Amount.get.toString else "NULL"
         val Balance = if( rec.Balance.isDefined ) rec.Balance.get.toString else "NULL"
       s"insert into transactions (" +
         "AccountId," +
         "Ticket," +
         "Date," +
         "Timezone," +
         "Transaction," +
         "Details," +
         "Instrument," +
         "Price," +
         "Units," +
         "Direction," +
         "SpreadCost," +
         "StopLoss," +
         "TakeProfit," +
         "TrailingStop," +
         "Financing," +
         "Commission," +
         "ProfitLoss," +
         "Amount," +
         s"Balance" +
         s") values (" +
          s"${account.AccountId},$Ticket,'$Date','$Timezone','$Transaction','$Details','$Instrument',$Price,$Units,'$Direction',$SpreadCost,$StopLoss,$TakeProfit,$TrailingStop,$Financing,$Commission,$ProfitLoss,$Amount,$Balance)"

   }
   //_query =

   //def getFirstCol() : Long = {
   //   return _reader.getLong("FirstCol")
   //}
}
