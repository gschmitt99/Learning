package Oanda
import java.text.SimpleDateFormat

case class TransactionRecord(
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
   def transform(): Option[AccountAction] = {
      val format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
      val convertedDate = format.parse(Date)
      Transaction match {
         case "CREATE" => None:Option[AccountAction] // new AccountAction(Ticket.toInt, convertedDate)
         case "CLIENT_CONFIGURE" => None:Option[AccountAction] // new AccountAction(Ticket.toInt, convertedDate)
         case "TRANSFER_FUNDS" => Option(new TransferFunds(Ticket.toInt, convertedDate, Amount.toDouble))
         case "FIXED_PRICE_ORDER" =>
            Option(new FixedPriceOrder(Ticket.toInt, convertedDate))
         case "ORDER_FILL" => {
            if( SpreadCost.isEmpty() ) {
               Option(new OrderFill(Ticket.toInt, convertedDate, Instrument, Price.toDouble, Units.toInt, Direction))
            } else {
               Option(new OrderFill(Ticket.toInt, convertedDate, Instrument, Price.toDouble, Units.toInt, Direction,SpreadCost.toDouble,Financing.toDouble))
            }
      }
         case "DAILY_FINANCING" => {
            if( Ticket.isEmpty()) {
               val tradeId = Details.split(":")(1).trim().toInt
               Option(new DailyTicketFinancing(tradeId, convertedDate, Instrument, Financing.toDouble))
            }
            else {
               Option(new DailyFinancingSummary(Ticket.toInt, convertedDate, Financing.toDouble))
            }
         }
         case "MARKET_ORDER" => Option(new MarketOrder(Ticket.toInt, convertedDate))
         case _ => None:Option[AccountAction]
      }
   }

}
