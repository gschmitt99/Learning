package Oanda.Backstore

import java.text.SimpleDateFormat

import Core.DbUpdate
import Oanda.Models.Fill

class DbAddFill(val database:String, val user:String, val pass:String, val fill:Fill)
extends DbUpdate(database,user,pass) {

   val format = new SimpleDateFormat("YYYY-MM-dd")
   _query = {
      "insert into fills (" +
         "Ticket," +
         "AccountId," +
         "Date," +
         "Instrument," +
         "Price," +
         "Volume," +
         "Direction," +
         "SpreadCost," +
         "Financing" +
         s") values (${fill.Ticket}," +
         s"${fill.AccountId}," +
         s"'${format.format(fill.FillDate)}'," +
         s"'${fill.Instrument}'," +
         s"${fill.Price}," +
         s"${fill.Volume}," +
         s"'${fill.Direction}'," +
         s"${fill.SpreadCost}," +
         s"${fill.Financing})"
   }
}
