package Oanda.Controllers

import java.util.Date

import Oanda.Backstore.PersistenceManager
import Oanda.InstrumentHistory
import Oanda.Models._

import scala.collection.mutable

object Ledger {
   var balance = 0.0
   var instruments = scala.collection.mutable.Map[String,InstrumentHistory]()
   var summaryTickets = scala.collection.mutable.LinkedHashMap[Date,DailyFinancingSummary]()

   def addFunds(funds:TransferFunds): Unit = {
      balance += funds.amount
   }

   def addFill(orderFill:OrderFill, position: Long, tradeList: mutable.MutableList[Trade]): Long = {
      if( !instruments.contains(orderFill.instrument)) {
         instruments += (orderFill.instrument -> new InstrumentHistory(orderFill.instrument))
      }
      val fill = PersistenceManager.getFillByTicket(orderFill.ticket).getOrElse({
         val fill = Fill(
            -1,
            orderFill.ticket,
            orderFill.accountId,
            orderFill.date,
            orderFill.instrument,
            orderFill.price,
            orderFill.volume,
            orderFill.Direction,
            orderFill.spreadCost,
            orderFill.financing
         )
         PersistenceManager.insertFill(fill)
      })
      calculatePosition(fill, position, tradeList)
   }

   def calculatePosition(fill:Fill, position: Long, tradeList: mutable.MutableList[Trade]): Long = {
     if (fill.Direction.equals("Buy")) {
        processLongFill(fill, position, tradeList)
     } else {
        processShortFill(fill, position, tradeList)
     }
   }

   def createTrade(fill:Fill, isBuy:Boolean, sequence:Long): Trade = {
      new Trade(-1,fill.AccountId, fill.Instrument, sequence, isBuy,
         Math.abs(fill.Volume),0.0,fill.FillId,fill.FillDate,fill.Price,0, null,0.0)
   }

   /* to analyze how this is coming out, simply need to:
SELECT Quantity,EntryFillPrice,ExitFillPrice,ClosedProfit,
EntryFillTime,ExitFillTime FROM trades t WHERE instrument='EUR/USD'
INTO OUTFILE 'c:\\log\\query1235.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';

Then sum the appropriate closed profit records.  That number should be the difference
in the account balance after the fill has been added.

Note, per unit cost here is (1/price * commission) / close fill quantity
in the case of EUR/USD.  I do not know why commission is positive here, and I do not
know why the financing cost does not seem to be used in the calcs.
    */
   def processLongFill( fill:Fill, position:Long, trades: mutable.MutableList[Trade] ): Long = {
      var newPosition = position
      var fillQuantity = fill.Volume
      var sequence:Long = 0
      var pnl:Double = 0.0
      var aMatch:Boolean = false
      var newTrade: Trade = null
      val newTrades:mutable.MutableList[Trade] = new mutable.MutableList[Trade]()
      while( fillQuantity > 0 ) {
         newTrade = null
         aMatch = false
         trades.foreach( t => {
            if( fillQuantity > 0 ) {
               // found an exit trade ?
               if (!t.IsBuy && t.ExitFillId == 0) {
                  // the open trade fully takes the fill?
                  if (fillQuantity <= t.Quantity) {
                     // are going to match part or all of this trade
                     if (fill.Volume < t.Quantity) {
                        newTrade = createTrade(fill, true, sequence)
                        newTrade.EntryFillId = fill.FillId
                        newTrade.Quantity = t.Quantity - fillQuantity
                        newTrade.EntryFillPrice = fill.Price
                        newTrade.EntryFillTime = fill.FillDate
                        newTrade.IsBuy = true
                     }
                     newPosition += fillQuantity
                     val commission = fill.PerUnitFees * fillQuantity;
                     pnl = ((fill.Price - t.EntryFillPrice) * -fillQuantity * fill.Fx) + commission
                     t.ExitFillId = fill.FillId
                     t.ExitFillPrice = fill.Price
                     t.ExitFillTime = fill.FillDate
                     t.ClosedProfit = pnl
                     t.Sequence = sequence
                     sequence = sequence + 1
                     newTrades += t
                     fillQuantity = 0
                     aMatch = true

                     if (newTrade != null) {
                        newTrade.Sequence = sequence
                        sequence = sequence + 1
                        newTrades += newTrade
                     }
                  } else {
                     val commission = fill.PerUnitFees * t.Quantity;
                     pnl = ((fill.Price - t.EntryFillPrice) * -t.Quantity * fill.Fx) + commission
                     t.ExitFillId = fill.FillId
                     t.ExitFillPrice = fill.Price
                     t.ExitFillTime = fill.FillDate
                     t.ClosedProfit = pnl
                     t.Sequence = sequence
                     sequence = sequence + 1
                     newTrades += t
                     fillQuantity = fillQuantity - t.Quantity
                     newPosition = newPosition + t.Quantity
                  }
               } else if (t.EntryFillId == fill.FillId) {
                  t.Sequence = sequence
                  sequence = sequence + 1
                  newTrades += t
                  newPosition += t.Quantity
                  fillQuantity = 0
                  aMatch = true
               } else {
                  t.Sequence = sequence
                  sequence = sequence + 1
                  newTrades += t
               }
            } else {
               t.Sequence = sequence
               sequence = sequence + 1
               newTrades += t
            }
         })
         if( !aMatch ) {
            newTrade = createTrade(fill,true,sequence)
            newTrades += newTrade
            newPosition += fill.Volume
            fillQuantity = 0
         }
      }

      trades.clear()
      // now put all trades back.
      newTrades.foreach( t =>
         trades += t
      )
      newPosition
   }

   def processShortFill( fill:Fill, position:Long, trades: mutable.MutableList[Trade] ): Long = {
      var newPosition = position
      var fillQuantity = -fill.Volume // want the quantity to be positive
      var sequence:Long = 0
      var pnl:Double = 0.0
      var aMatch:Boolean = false
      var newTrade: Trade = null
      val newTrades:mutable.MutableList[Trade] = new mutable.MutableList[Trade]()
      while( fillQuantity > 0 ) {
         aMatch = false
         trades.foreach( t => {
            if( fillQuantity > 0 ) {
               // found an exit trade ?
               if (t.IsBuy && t.ExitFillId == 0) {
                  // the open trade fully takes the fill
                  if (fillQuantity <= t.Quantity) {
                     // are going to match part or all of this trade
                     if (fillQuantity < t.Quantity) {
                        newTrade = createTrade(fill, false, sequence)
                        newTrade.EntryFillId = fill.FillId
                        newTrade.Quantity = t.Quantity - fillQuantity
                        newTrade.EntryFillPrice = fill.Price
                        newTrade.EntryFillTime = fill.FillDate
                        newTrade.IsBuy = false
                     }
                     val commission = fill.PerUnitFees * fillQuantity;
                     newPosition -= fillQuantity
                     pnl = (((fill.Price - t.EntryFillPrice) * fillQuantity) * fill.Fx) - commission
                     t.ExitFillId = fill.FillId
                     t.Quantity = fillQuantity
                     t.ExitFillPrice = fill.Price
                     t.ExitFillTime = fill.FillDate
                     t.ClosedProfit = pnl
                     t.Sequence = sequence
                     sequence = sequence + 1
                     newTrades += t
                     fillQuantity = 0
                     aMatch = true

                     if (newTrade != null) {
                        newTrade.Sequence = sequence
                        sequence = sequence + 1
                        newTrades += newTrade
                     }
                  } else {
                     val commission = fill.PerUnitFees * t.Quantity;
                     pnl = (((fill.Price - t.EntryFillPrice) * t.Quantity) * fill.Fx) - commission
                     t.ExitFillId = fill.FillId
                     t.ExitFillPrice = fill.Price
                     t.ExitFillTime = fill.FillDate
                     t.ClosedProfit = pnl
                     t.Sequence = sequence
                     sequence = sequence + 1
                     newTrades += t
                     fillQuantity = fillQuantity - t.Quantity
                     newPosition = newPosition - t.Quantity
                  }
               } else if (t.EntryFillId == fill.FillId) {
                  t.Sequence = sequence
                  sequence = sequence + 1
                  newTrades += t
                  newPosition -= t.Quantity
                  fillQuantity = 0
                  aMatch = true
               } else {
                  t.Sequence = sequence
                  sequence = sequence + 1
                  newTrades += t
               }
            } else {
               t.Sequence = sequence
               sequence = sequence + 1
               newTrades += t
            }
         })
         if( !aMatch ) {
            newTrade = createTrade(fill,false,sequence)
            newTrades += newTrade
            newPosition += fill.Volume
            fillQuantity = 0
         }
      }

      trades.clear()
      // now put all trades back.
      newTrades.foreach( t =>
         trades += t
      )
      newPosition
   }

   def addDailyTicketFinancing(ticket:DailyTicketFinancing):Unit = {
      if( !instruments.contains(ticket.instrument)) {
         instruments += (ticket.instrument -> new InstrumentHistory(ticket.instrument))
      }
      //instruments(ticket.instrument).addDailyFinancing(ticket)
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
   def dumpFills(instrument:String) : Unit = {
      instruments(instrument).fills.foreach( f =>
      println(s"${f.price},${f.volume}"))
      val r = 45
   }
}
