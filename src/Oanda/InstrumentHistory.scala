package Oanda

import java.util.Date

import Oanda.Models.{DailyTicketFinancing, OrderFill}

import scala.collection.mutable

object PipCalculator {
   def calcPNL( closingFill:OrderFill, weight:Double): Double = {
      val toUSD = {
         if( closingFill.instrument.startsWith("USD")) {
            1/closingFill.price
         } else {
            1.0
         }
      }
      val avgEntry = math.abs(math.rint(weight/closingFill.volume*1000000)/1000000)
      val pnl=
      if( closingFill.Direction.equals("Buy")) {
         if (closingFill.price > avgEntry) {
            // means a loss
            val delta = closingFill.price - avgEntry
            -delta * closingFill.volume
         } else {
            val delta = closingFill.price - avgEntry
            delta * closingFill.volume
         }
      }
      else {
         if (closingFill.price > avgEntry) {
            // means a gain
            val delta = closingFill.price - avgEntry
            val p = delta * closingFill.volume
            p
         } else {
            val delta = closingFill.price - avgEntry
            -delta * closingFill.volume
         }
      }
      val r = pnl * toUSD + closingFill.financing
      pnl * toUSD + closingFill.financing
   }
}

class InstrumentHistory(val instrument:String) {
   val fills  = new mutable.MutableList[OrderFill]()// : mutable.MutableList[OrderFill]
   val dailyTickets = scala.collection.mutable.Map[Date,scala.collection.mutable.MutableList[DailyTicketFinancing]]()
   val dailyFinancing = scala.collection.mutable.Map[Date,Double]()
   var totalVolume = 0
   var averagePrice = 0.0

   def addFill(fill:OrderFill) : Unit = {
      fills += fill
   }

   def addDailyFinancing(ticket:DailyTicketFinancing) : Unit = {
      if( !dailyTickets.contains(ticket.date)) {
         dailyTickets += (ticket.date -> scala.collection.mutable.MutableList[DailyTicketFinancing]())
      }
      dailyTickets(ticket.date) += ticket
      if( !dailyFinancing.contains(ticket.date)) {
         dailyFinancing += (ticket.date -> 0.0)
      }
      dailyFinancing(ticket.date) += ticket.financing
   }

   def getFinancingForDate(date:Date) : Double = {
      // note, it is possible to not have financing on an instrument
      // after a position has been closed, so need to check.
      if( dailyFinancing.contains(date))
          return dailyFinancing(date)
      return 0.0
   }

   def matchFills( fill:OrderFill ): Double = {
      var vol = Math.abs(fill.volume)
      var weights = 0.0
      fills.foreach( f => {
         if( vol > 0 ) {
            //val fw = f.matchFill(vol)
            //weights += fw.weight
            //vol = vol - fw.volume
         }
      })

      val avg = math.rint(weights/fill.volume*1000000)/1000000
      val dir = {
         if (fill.Direction == "Buy") {
            1
         } else {
            -1
         }
      }
      val delta = dir * PipCalculator.calcPNL(fill,weights)
      delta
   }

   def summarizeFills() : Double = {
      var balanceDelta = 0.0
      var weights = 0.0
      var debug=false
      if( instrument.equals("EUR/USD")) {
         debug=true
      }
      fills.foreach( f => {
         if( totalVolume != 0) {
            if( f.volume > 0 )
               {
                  var r = 45
               }
            // if the fill is in the opposite direction
            // of the current position, we are closing
            // part of the position.
            if( !(totalVolume<0)==(f.volume<0)) {
               //if( debug ) matchFills(f)
               balanceDelta += matchFills(f)
               /*
               // I need to figure out if this works if USD is
               // the quote currency and the base currency both.
               var r=45
               // price of the fill
               val s = math.abs(f.volume * f.price)
               // difference
               val d = (s-weights)/f.price
               // transaction cost
               val c = (f.spreadCost + f.financing)
               // convert to USD
               val e = c / f.price
               // remove the transaction cost
               balanceDelta += d - e
               */
            }
         }
         weights += (f.price * f.volume)
         totalVolume += f.volume
      })
      println(s"${instrument} weights=${weights} totalVolume=${totalVolume}")
      averagePrice = math.rint(weights/totalVolume*1000000)/1000000
      balanceDelta
   }
}
