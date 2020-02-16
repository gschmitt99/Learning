package Oanda.Models

import java.util.Date

abstract class AccountAction(val transactionId:Long, val accountId:Long, val ticket:Long, val date:Date) {
   //def process(l:Ledger) : Unit = {
   //}
}

// can probably eliminate this because it came from the 'old' format.
class CloseTrade(
    override val transactionId:Long,
    override val accountId:Long,
    override val ticket:Long,
    override val date:Date,
    val instrument: String,
    val volume:Int,
    val price:Double,
    val amount:Double,
    val interest:Double
)
extends AccountAction(transactionId,accountId,ticket,date)

class Create(
   override val transactionId:Long,
   override val accountId:Long,
   override val ticket:Long,
   override val date:Date
)
extends AccountAction(transactionId,accountId,ticket,date)

class ClientConfigure(
   override val transactionId:Long,
   override val accountId:Long,
   override val ticket:Long,
   override val date:Date
)
extends AccountAction(transactionId,accountId,ticket,date)

class TransferFunds(
    override val transactionId:Long,
    override val accountId:Long,
    override val ticket:Long,
    override val date:Date,
    val amount:Double
)
extends AccountAction(transactionId,accountId,ticket,date)

class FixedPriceOrder(
   override val transactionId:Long,
   override val accountId:Long,
   override val ticket:Long,
   override val date:Date
)
extends AccountAction(transactionId,accountId,ticket,date)

case class FillWeight( volume:Int, weight:Double )

class OrderFill(
    override val transactionId:Long
    ,override val accountId:Long
    ,override val ticket:Long
    ,override val date:Date
    ,val instrument:String
    ,val price:Double
    ,val _volume:Int
    ,val Direction:String
    ,val spreadCost:Double = 0.0
    ,val financing:Double = 0.0
) extends AccountAction(transactionId,accountId,ticket,date){
   var _matchedVolume = 0
   val volume = if( Direction.equals("Sell")) -_volume else _volume
}

case class Fill(
   var FillId:Long
   ,val Ticket:Long
   ,val AccountId:Long
   ,val FillDate:Date
   ,val Instrument:String
   ,val Price:Double
   ,val Volume:Long
   ,val Direction:String
   ,val PerUnitFees:Double
   ,val SpreadCost:Double
   ,val Financing:Double
   ,val Fx: Double
)

object Fill {

   def determineFx(instrument: String, price:Double):Double = {
      val parts = instrument.split("/")
      val fx = if( parts.length == 2 ) {
         if( parts(0).equals("USD")) {
            1/price
         } else {
            1
         }
      } else {
         1
      }
      fx
   }

   /* I don't fully understand this, but it appears the financing
   is based on the opposite of the base currency.  In the case of EUR/USD
   it is in EUR where the PnL for the same trade would be in USD.
    */
   def determineInverseFx(instrument: String, price:Double):Double = {
      val parts = instrument.split("/")
      val fx = if( parts.length == 2 ) {
         if( parts(1).equals("USD")) {
            1/price
         } else {
            1
         }
      } else {
         1
      }
      fx
   }

   def apply(
   FillId: Long
   ,Ticket: Long
   ,AccountId: Long
   ,FillDate: Date
   ,Instrument: String
   ,Price: Double
   ,Volume: Long
   ,Direction: String
   ,SpreadCost: Double = 0.0
   ,Financing: Double = 0.0
   ): Fill = {
      val fx = determineFx(Instrument, Price)
      val inverseFx = determineInverseFx(Instrument, Price)
      val perUnitFee = (Financing * inverseFx) / Volume
      new Fill(
         FillId,
         Ticket,
         AccountId,
         FillDate,
         Instrument,
         Price,
         Volume,
         Direction,
         perUnitFee,
         SpreadCost,
         Financing,
         fx
      )
   }
}

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

case class Position(
    var PositionId:Long,
    var AccountId:Long,
    var Instrument:String,
    var Position:Long
    )

class MarketOrder(
  override val transactionId:Long,
  override val accountId:Long,
  override val ticket:Long,
  override val date:Date
)
extends AccountAction(transactionId,accountId,ticket,date)

class OrderCancel(
  override val transactionId:Long,
  override val accountId:Long,
  override val ticket:Long,
  override val date:Date
)
extends AccountAction(transactionId,accountId,ticket,date)

class DailyTicketFinancing(
  override val transactionId:Long,
  override val accountId:Long,
  override val ticket:Long,
  override val date:Date,
  val instrument:String, val financing:Double
)
extends AccountAction(transactionId,accountId,ticket,date)

class DailyFinancingSummary(
   override val transactionId:Long,
   override val accountId:Long,
   override val ticket:Long,
   override val date:Date,
   val financing:Double
)
extends AccountAction(transactionId,accountId,ticket,date)

case class Account(var AccountId:Long, var AccountName:String)

// represents the last transaction that has been processed by the system.
case class LastTransaction(val LastTransactionId:Long, val AccountId:Long, val TransactionId:Long)