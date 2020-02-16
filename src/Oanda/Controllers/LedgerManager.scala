package Oanda.Controllers

dimport java.sql.Timestamp

import Oanda.Backstore.PersistenceManager
import Oanda.Models._

import scala.collection.{immutable, mutable}
import scala.io.Source

class LedgerManager {

}

object LedgerManager {

   def getTrades(account: Account):mutable.Map[String, mutable.MutableList[Trade]] = {
      PersistenceManager.getTrades(account.AccountId)
   }

   def getPositions(account: Account):mutable.Map[String,Long] = {
      PersistenceManager.getPositions(account.AccountId)
   }

   def linesToDatabaseTransactions( account: Account, maxDate: Timestamp, lines:immutable.List[String]): List[DatabaseTransactionRecord] = {
      val transactionList = new mutable.MutableList[DatabaseTransactionRecord]()
      lines.foreach( s =>
      {
         val parts = s.split(",",-1)
         if( !parts(0).equals("Ticket")) {
            val record = DatabaseTransactionRecord.get(new TransactionRecord(
               -1,
               account.AccountId,
               parts(0),
               parts(1),
               parts(2),
               parts(3),
               parts(4),
               parts(5),
               parts(6),
               parts(7),
               parts(8),
               parts(9),
               parts(10),
               parts(11),
               parts(12),
               parts(13),
               parts(14),
               parts(15),
               parts(16),
               parts(17)
            ))
            if( maxDate.compareTo(record.Date) < 0 ) {
               transactionList += record
            }
         }
      })
      transactionList.toList
   }

   def startup(accountName:String, inputFileName:String): Unit = {
      val lines = Source.fromFile(inputFileName).getLines.toList
      val account = PersistenceManager.getAccountByName(accountName)
      val maxDate = PersistenceManager.getMaxTransactionDate(account)
      val records = linesToDatabaseTransactions(account, maxDate, lines)
      PersistenceManager.insertTransactions(account,records)

      val lastTransaction = PersistenceManager.getLastTransaction(account)
      val actions = PersistenceManager.getTransactions(account, lastTransaction)
      val tradeMap = getTrades(account)
      val positions = getPositions(account)
      var lastTransactionId:Long = -1
      actions.foreach(a => {

         if (a.isDefined) {
            lastTransactionId = a.get.transactionId
            a.get match {
               case c: Create =>
               case c: ClientConfigure =>
               case f: OrderFill => {
                  if (!tradeMap.contains(f.instrument)) {
                     tradeMap += (f.instrument -> new mutable.MutableList[Trade]())
                  }
                  val tradeList = tradeMap(f.instrument)
                  if (!positions.contains(f.instrument)) {
                     positions(f.instrument) = 0
                  }
                  positions(f.instrument) = Ledger.addFill(f, positions(f.instrument), tradeList)
               }
               case f: TransferFunds =>
                  Ledger.addFunds(f)
               case c: FixedPriceOrder =>
               case c: MarketOrder =>
               case c: OrderCancel =>
               case f: DailyTicketFinancing =>
                  Ledger.addDailyTicketFinancing(f)
               case f: DailyFinancingSummary =>
                  Ledger.addDailyFinancingSummary(f)
            }
         }
         else {
            val r = 45
         }
      })

      val newLastTransaction = LastTransaction(-1,account.AccountId,lastTransactionId)
      PersistenceManager.checkUpdateLastTransaction(newLastTransaction)

      positions.foreach { ent: (String, Long) =>
         val p: Position = new Position(-1, account.AccountId, ent._1, ent._2)
         PersistenceManager.checkUpdatePosition(p)
      }

      // note, for the moment, sequence is going to be set to -1 if trades are not
      // touched by the code.  I am thinking about actually removing that field
      // but for the time being am going to leave it in until I can figure out
      // whether or not it actually will be needed.
      tradeMap.values.foreach( tradeList =>
         if( !tradeList.isEmpty ) {
            PersistenceManager.deleteTradesByAccountIdAndInstrument(tradeList.head)
            tradeList.foreach( t => PersistenceManager.insertTrade(t) )
         }
      )
   }
}
