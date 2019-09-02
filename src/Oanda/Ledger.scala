package Oanda
import java.util.{Date}

class Ledger {
   var balance = 0.0
   var instruments = scala.collection.mutable.Map[String,InstrumentHistory]()
   var summaryTickets = scala.collection.mutable.LinkedHashMap[Date,DailyFinancingSummary]()

   def addFunds(funds:TransferFunds): Unit = {
      balance += funds.amount
   }

   def addFill(fill:OrderFill): Unit = {
      if( !instruments.contains(fill.instrument)) {
         instruments += (fill.instrument -> new InstrumentHistory(fill.instrument))
      }
      instruments(fill.instrument).addFill(fill)
   }

   def addDailyTicketFinancing(ticket:DailyTicketFinancing):Unit = {
      if( !instruments.contains(ticket.instrument)) {
         instruments += (ticket.instrument -> new InstrumentHistory(ticket.instrument))
      }
      instruments(ticket.instrument).addDailyFinancing(ticket)
   }

   def addDailyFinancingSummary(ticket:DailyFinancingSummary):Unit = {
      summaryTickets(ticket.date) = ticket
   }

   private def reconcileFinancing():Unit = {
      summaryTickets.foreach( t => {
         var total:Double = 0.0
         instruments.foreach(i => {
            total += i._2.getFinancingForDate(t._1)
         })
         total = (math.rint(total * 10000)) / 10000
         println( "day=%s total=%f", t._1, total)
         if( total != summaryTickets(t._1).financing) {
            val fin = summaryTickets(t._1).financing
         } else{
            balance += total
         }
      } )
   }
   private def summarizeFills():Unit = {
      instruments.foreach( i => {
         val bdelta= i._2.summarizeFills()
         println( "%s delta=%f", i._1,bdelta)
         balance += bdelta
      })
   }
   def calculate():Unit = {
      reconcileFinancing()
      summarizeFills()
      var r=45
   }
}

object Ledger {
   def create(actions:List[Option[AccountAction]]) : Ledger = {
      val ledger = new Ledger()
      actions.foreach( a => {
         if( a.isDefined ) {
            a.get.process(ledger)
         }
      })
      return ledger
   }
}
