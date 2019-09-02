package Oanda

import scala.io.Source
import scala.collection.mutable
import scala.collection.{immutable}

object Driver {

   def processLines( lines:immutable.List[String]): List[Option[AccountAction]] = {
      val transactionList = new mutable.MutableList[TransactionRecord]()
      lines.foreach( s =>
      {
         val parts = s.split(",",-1)
         val record = new TransactionRecord(
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
         )
         if( !parts(0).equals("Ticket")) {
            if( parts(0).isEmpty()) {
               parts(0) = "-1"
            }
            transactionList += record
         }
      })

      val actionList = new mutable.MutableList[Option[AccountAction]]()
      transactionList.foreach( s => {
         actionList += s.transform()
      })
      actionList.toList
   }

   def main(args:Array[String]): Unit ={
      val lines = Source.fromFile("""c:\log\transactions2.csv""").getLines.toList
      var accountActions = processLines(lines)
      val ledger = Ledger.create(accountActions)
      ledger.calculate()
      var r=45
   }
}