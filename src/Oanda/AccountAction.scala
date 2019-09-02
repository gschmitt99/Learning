package Oanda
import java.util.{Date}

abstract class AccountAction(val ticket:Long, val date:Date) {
   def process(l:Ledger) : Unit = {
   }
}

// can probably eliminate this because it came from the 'old' format.
class CloseTrade(
                   override val ticket:Long,
                   override val date:Date,
                   val instrument: String,
                   val volume:Int,
                   val price:Double,
                   val amount:Double,
                   val interest:Double
                )
   extends AccountAction(ticket,date) {

   override def process(l:Ledger) : Unit = {
   }
}

class ClientConfigure(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date) {
   override def process(l:Ledger) : Unit = {

   }
}

class TransferFunds(override val ticket:Long,override val date:Date, val amount:Double) extends AccountAction(ticket,date) {
   override def process(l:Ledger) : Unit = {
      l.addFunds(this)
   }
}

class FixedPriceOrder(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date){
   override def process(l:Ledger) : Unit = {
      println( s"FixedPriceOrder: ticket=$ticket ")
   }
}

case class FillWeight( volume:Int, weight:Double )

class OrderFill(
    override val ticket:Long
    ,override val date:Date
    ,val instrument:String
    ,val price:Double
    ,val _volume:Int
    ,val Direction:String
    ,val spreadCost:Double = 0.0
    ,val financing:Double = 0.0
) extends AccountAction(ticket,date){
   var _matchedVolume = 0
   val volume = if( Direction.equals("Sell")) -_volume else _volume
   override def process(l:Ledger) : Unit = {
      println( s"OrderFill $instrument $price $volume $Direction" )
      l.addFill(this)
   }
   def matchFill(matchVol:Int) : FillWeight = {
      val absVol = Math.abs(volume)
      val absMatchVol = Math.abs(matchVol)
      val d = absMatchVol - absVol
      if( d > 0 ) {
         _matchedVolume = absVol
         val w = price * _matchedVolume
         FillWeight(_matchedVolume, w)
      } else if( d == 0 ) {
         _matchedVolume = absVol
         val w = price * absVol
         FillWeight(absVol,w)
      } else {
         _matchedVolume = 3
         val w = price * absVol
         FillWeight(d,w)
      }
   }
}

class MarketOrder(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date){
   override def process(l:Ledger) : Unit = {

   }
}

class DailyTicketFinancing(override val ticket:Long,override val date:Date, val instrument:String, val financing:Double) extends AccountAction(ticket,date){
   override def process(l:Ledger) : Unit = {
      l.addDailyTicketFinancing(this)
   }
}

class DailyFinancingSummary(override val ticket:Long,override val date:Date, val financing:Double) extends AccountAction(ticket,date){
   override def process(l:Ledger) : Unit = {
      l.addDailyFinancingSummary(this)
   }
}
