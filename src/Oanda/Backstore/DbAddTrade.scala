package Oanda.Backstore

import java.text.SimpleDateFormat
import java.util.Date

import Core.DbUpdate
import Oanda.Models.Trade

class DbAddTrade(val database:String, val user:String, val pass:String, val trade:Trade)
extends DbUpdate(database,user,pass) {

   val format = new SimpleDateFormat("YYYY-MM-dd")
   val entryTime = format.format(if( trade.EntryFillTime == null ) new Date(0) else trade.EntryFillTime)
   val exitTime = format.format(if( trade.ExitFillTime == null ) new Date(0) else trade.ExitFillTime)
   _query = {
      "insert into trades (" +
         "AccountId," +
         "Instrument," +
         "Sequence," +
         "IsBuy," +
         "Quantity," +
         "ClosedProfit," +
         "EntryFillId," +
         "EntryFillTime," +
         "EntryFillPrice," +
         "ExitFillId," +
         "ExitFillTime," +
         "ExitFillPrice" +
         s") values (${trade.AccountId}," +
         s"'${trade.Instrument}'," +
         s"${trade.Sequence}," +
         s"${trade.IsBuy}," +
         s"${trade.Quantity}," +
         s"${trade.ClosedProfit}," +
         s"${trade.EntryFillId}," +
         s"'${entryTime}'," +
         s"${trade.EntryFillPrice}," +
         s"${trade.ExitFillId}," +
         s"'${exitTime}'," +
         s"${trade.ExitFillPrice})"
   }
}
