package Oanda.Models

import java.util.Date
import java.text.SimpleDateFormat

case class TransactionRecord(
    TransactionId:Long,
    AccountId:Long,
    Ticket:String,
    Date:String,
    Timezone:String,
    Transaction:String,
    Details:String,
    Instrument:String,
    Price:String,
    Units:String,
    Direction:String,
    SpreadCost:String,
    StopLoss:String,
    TakeProfit:String,
    TrailingStop:String,
    Financing:String,
    Commission:String,
    ProfitLoss:String,
    Amount:String,
    Balance:String
                            )
{
   def transformToDbRecord():Unit = {

   }

   val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

   def transform(account: Account): Option[AccountAction] = {
      //val format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
      //val format = new SimpleDateFormat(dateFormat)
      val convertedDate = format.parse(Date)
      Transaction match {
         //case "CREATE" => None:Option[AccountAction]
         //case "CLIENT_CONFIGURE" => None:Option[AccountAction]
         case "CREATE" => Option(new Create(TransactionId,AccountId,Ticket.toInt, convertedDate))
         case "CLIENT_CONFIGURE" => Option(new ClientConfigure(TransactionId,AccountId,Ticket.toInt, convertedDate))
         case "TRANSFER_FUNDS" => Option(new TransferFunds(TransactionId,AccountId,Ticket.toInt, convertedDate, Amount.toDouble))
         case "FIXED_PRICE_ORDER" =>
            Option(new FixedPriceOrder(TransactionId,AccountId,Ticket.toInt, convertedDate))
         case "ORDER_FILL" => {
            if( SpreadCost.isEmpty() ) {
               Option(new OrderFill(TransactionId,AccountId,Ticket.toInt, convertedDate,Instrument, Price.toDouble, Units.toInt, Direction))
            } else {
               Option(new OrderFill(TransactionId,AccountId,Ticket.toInt, convertedDate,Instrument, Price.toDouble, Units.toInt, Direction,SpreadCost.toDouble,Financing.toDouble))
            }
         }
         case "DAILY_FINANCING" => {
            if( Ticket.isEmpty()) {
               val tradeId = Details.split(":")(1).trim().toInt
               Option(new DailyTicketFinancing(TransactionId,AccountId,tradeId, convertedDate, Instrument, Financing.toDouble))
            }
            else {
               Option(new DailyFinancingSummary(TransactionId,AccountId,Ticket.toInt, convertedDate, Financing.toDouble))
            }
         }
         case "MARKET_ORDER" => Option(new MarketOrder(TransactionId,AccountId,Ticket.toInt, convertedDate))
         case "MARKET_IF_TOUCHED_ORDER" => Option(new MarketOrder(TransactionId,AccountId,Ticket.toInt,convertedDate))
         case "ORDER_CANCEL" => Option(new OrderCancel(TransactionId,AccountId,Ticket.toInt,convertedDate))
         case _ => None:Option[AccountAction]
      }
   }
}
case class DatabaseTransactionRecord(
   Ticket:Option[Int],
   Date:Date,
   Timezone:String,
   Transaction:String,
   Details:String,
   Instrument:String,
   Price:Option[Double],
   Units:Option[Int],
   Direction:String,
   SpreadCost:Option[Double],
   StopLoss:Option[Double],
   TakeProfit:Option[Double],
   TrailingStop:Option[Double],
   Financing:Option[Double],
   Commission:Option[Double],
   ProfitLoss:Option[Double],
   Amount:Option[Double],
   Balance:Option[Double]
)
object DatabaseTransactionRecord
{
   private val dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")
   def get(tr:TransactionRecord):DatabaseTransactionRecord = {
      val convertedDate = dateFormat.parse(tr.Date)
      new DatabaseTransactionRecord(
         if (tr.Ticket.length() > 0) Some(tr.Ticket.toInt) else None
         ,convertedDate
         ,tr.Timezone
         ,tr.Transaction
         ,tr.Details
         ,tr.Instrument
         ,if( tr.Price.length() > 0) Some(tr.Price.toDouble) else None
         ,if( tr.Units.length() > 0 ) Some(tr.Units.toInt) else None
         ,tr.Direction
         ,if( tr.SpreadCost.length() > 0 ) Some(tr.SpreadCost.toDouble) else None
         ,if( tr.StopLoss.length() > 0 ) Some(tr.StopLoss.toDouble) else None
         ,if( tr.TakeProfit.length() > 0 ) Some(tr.TakeProfit.toDouble) else None
         ,if( tr.TrailingStop.length() > 0 ) Some(tr.TrailingStop.toDouble) else None
         ,if( tr.Financing.length() > 0 ) Some(tr.Financing.toDouble) else None
         ,if( tr.Commission.length() > 0 ) Some(tr.Commission.toDouble) else None
         ,if( tr.ProfitLoss.length() > 0 ) Some(tr.ProfitLoss.toDouble) else None
         ,if( tr.Amount.length() > 0 ) Some(tr.Amount.toDouble) else None
         ,if( tr.Balance.length() > 0 ) Some(tr.Balance.toDouble) else None
      )
   }
}