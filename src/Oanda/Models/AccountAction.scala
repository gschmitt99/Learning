package Oanda.Models

import java.util.Date

abstract class AccountAction(val ticket:Long, val date:Date) {
   //def process(l:Ledger) : Unit = {
   //}
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

   //override def process(l:Ledger) : Unit = {
   //}
}
class Create(override val ticket:Long, override val date:Date) extends AccountAction(ticket,date) {

}

class ClientConfigure(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date) {
   //override def process(l:Ledger) : Unit = {
   //}
}

class TransferFunds(override val ticket:Long,override val date:Date, val amount:Double) extends AccountAction(ticket,date) {
   //override def process(l:Ledger) : Unit = {
   //   l.addFunds(this)
   //}
}

class FixedPriceOrder(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date){
   //override def process(l:Ledger) : Unit = {
   //   println( s"FixedPriceOrder: ticket=$ticket ")
   //}
}

case class FillWeight( volume:Int, weight:Double )

class OrderFill(
    override val ticket:Long
    ,override val date:Date
    ,val accountId:Long
    ,val instrument:String
    ,val price:Double
    ,val _volume:Int
    ,val Direction:String
    ,val spreadCost:Double = 0.0
    ,val financing:Double = 0.0
) extends AccountAction(ticket,date){
   var _matchedVolume = 0
   val volume = if( Direction.equals("Sell")) -_volume else _volume
   //override def process(l:Ledger) : Unit = {
   //   println( s"OrderFill $instrument $price $volume $Direction" )
   //   //l.addFill(this)
   //}
   //def matchFill(matchVol:Int) : FillWeight = {
   //   val absVol = Math.abs(volume)
   //   val absMatchVol = Math.abs(matchVol)
   //   val d = absMatchVol - absVol
   //   if( d > 0 ) {
   //      _matchedVolume = absVol
   //      val w = price * _matchedVolume
   //      FillWeight(_matchedVolume, w)
   //   } else if( d == 0 ) {
   //      _matchedVolume = absVol
   //      val w = price * absVol
   //      FillWeight(absVol,w)
   //   } else {
   //      _matchedVolume = 3
   //      val w = price * absVol
   //      FillWeight(d,w)
   //   }
   //}
}

case class Fill(
   var FillId:Long
   ,val Ticket:Long
   ,val AccountId:Long
   ,val FillDate:Date
   ,val Instrument:String
   ,val Price:Double
   ,val Volume:Int
   ,val Direction:String
   ,val SpreadCost:Double = 0.0
   ,val Financing:Double = 0.0
)

case class Trade(
    var TradeId:Long=0,
    var AccountId:Long=0,
    var Instrument:String="",
    var Sequence:Long=0,
    var IsBuy:Boolean=false,
    var Quantity:Long=0,
    var ClosedProfit:Double=0.0,
    var EntryFillId:Long=0,
    var EntryFillTime:Date=new Date(),
    var EntryFillPrice:Double=0.0,
    var ExitFillId:Long=0,
    var ExitFillTime:Date=new Date(),
    var ExitFillPrice:Double=0.0)

class MarketOrder(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date){
   //override def process(l:Ledger) : Unit = {
   //}
}
class OrderCancel(override val ticket:Long,override val date:Date) extends AccountAction(ticket,date){
   //override def process(l:Ledger) : Unit = {
   //}
}

class DailyTicketFinancing(override val ticket:Long,override val date:Date, val instrument:String, val financing:Double) extends AccountAction(ticket,date){
   //override def process(l:Ledger) : Unit = {
   //   //l.addDailyTicketFinancing(this)
   //}
}

class DailyFinancingSummary(override val ticket:Long,override val date:Date, val financing:Double) extends AccountAction(ticket,date){
   //override def process(l:Ledger) : Unit = {
   //   //l.addDailyFinancingSummary(this)
   //}
}

case class Account(var AccountId:Long, var AccountName:String)
