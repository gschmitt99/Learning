package Oanda

import Oanda.Backstore.PersistenceManager
import Oanda.Models._
import Oanda.Controllers._

import scala.io.Source
import scala.collection.mutable
import scala.collection.immutable

object Driver {
   def main(args:Array[String]): Unit ={
      // will put back if need to test this more.
      //val trades = PersistenceManager.getTrades(1,"EUR/USD")
      LedgerManager.startup("oanda", """c:\log\transactions6.csv""")
      val account = PersistenceManager.getAccountByName("oanda")
      var r=45
   }
}
