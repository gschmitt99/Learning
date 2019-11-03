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

   def addFill(orderFill:OrderFill): Unit = {
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
      calculatePosition(fill)
      //instruments(fill.instrument).addFill(fill)
   }

   def calculatePosition(fill:Fill): Unit = {
      var position:Long = 0
      if( fill.Direction.equals("Buy")) {
         processLongFill( fill, position )
      }else {
         processShortFill( fill, position )
      }
   }

   def createTrade(fill:Fill, isBuy:Boolean, sequence:Long): Trade = {
      new Trade(-1,fill.AccountId, fill.Instrument, sequence, isBuy,
         fill.Volume,0.0,fill.FillId,fill.FillDate,fill.Price,0, null,0.0)
   }

   def processLongFill( fill:Fill, position:Long ): Long = {
      var newPosition = position
      var fillQuantity = fill.Volume
      var sequence:Long = 0
      val trades = PersistenceManager.getTrades(1, fill.Instrument)
      while( fillQuantity > 0 ) {
         trades.foreach( t => {
            if( t.IsBuy && t.ExitFillId == 0) {
               if( fill.Volume < t.Quantity) {
                  val trade = createTrade(fill,false,sequence)
                  trade.EntryFillId = fill.FillId
                  trade.Quantity = t.Quantity - fill.Volume
                  trade.EntryFillPrice = fill.Price
                  trade.IsBuy = false
               }
            }
            newPosition += fill.Volume
         })
      }
      newPosition
   }

   def processShortFill( fill:Fill, position:Long ): Long = {
      var newPosition = position
      var fillQuantity = fill.Volume
      var sequence:Long = 0
      var pnl:Double = 0.0
      var aMatch:Boolean = false
      var leftOff:Long = 0
      var newTrade: Trade = null
      val newTrades:mutable.MutableList[Trade] = new mutable.MutableList[Trade]()
      val trades = PersistenceManager.getTrades(fill.AccountId,fill.Instrument)
      if( trades.length > 0 ) {
         PersistenceManager.deleteTradesByAccountIdAndInstrument(trades.head)
      }
      while( fillQuantity < 0 ) {
         aMatch = false
         trades.foreach( t => {
            // found an exit trade ?
            if(t.IsBuy && t.ExitFillId == 0) {
               // the open trade fully takes the fill
               if(fill.Volume < t.Quantity) {
                  newTrade = createTrade(fill,false,sequence)
                  newTrade.EntryFillId = fill.FillId
                  newTrade.Quantity = t.Quantity - fill.Volume
                  newTrade.EntryFillPrice = fill.Price
                  newTrade.IsBuy = false
               }
               newPosition += fill.Volume
            } else if( t.EntryFillId == fill.FillId) {
               t.Sequence = sequence
               sequence = sequence + 1
               newTrades += t
               newPosition += t.Quantity
               fillQuantity = 0
               aMatch = true
               // need to do something about leftoff
            } else {
               t.Sequence = sequence
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

      //while( leftOff < origTrades.length ) {

      //}

      // now put all trades back.
      newTrades.foreach( t =>
         PersistenceManager.insertTrade(t)
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
