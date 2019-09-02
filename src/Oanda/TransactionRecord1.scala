package Oanda

import java.text.SimpleDateFormat

case class TransactionRecord1(
   TransactionID:String,
   AccountID:String,
   Type:String,
   CurrencyPair:String,
   Units:String,
   UTCTime:String,
   Price:String,
   Balance:String,
   Interest:String,
   Pl:String,
   HighOrderLimit:String,
   LowOrderLimit:String,
   Amount:String,
   StopLoss:String,
   LimitOrder:String,
   TransactionLink:String,
   TrailingStopPipettes:String
)
{
   def transform(): Option[AccountAction] = {
      //val format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
      val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
      val convertedDate = format.parse(UTCTime)
      Type match {
         case "Fund Withdrawal (System Migration)" =>
            // time and amount are filled out.
            Option(new TransferFunds(TransactionID.toLong, convertedDate, Amount.toDouble))
         case "Close Trade (System Migration)" =>
            Option(new CloseTrade(
               TransactionID.toLong,
               convertedDate,
               CurrencyPair,
               Units.toInt,
               Price.toDouble,
               Amount.toDouble,
               Interest.toDouble
            ))
         case "Interest" =>
            None:Option[AccountAction]
         case "Fund Credit" =>
            None:Option[AccountAction]
         case "Fund Deposit" =>
            None:Option[AccountAction]
         case "Buy Market" =>
            None:Option[AccountAction]
         case "Sell Market" =>
            None:Option[AccountAction]
         case "Order Cancelled" =>
            None:Option[AccountAction]
         case "Sell Order" =>
            None:Option[AccountAction]
         case "Order Filled" =>
            None:Option[AccountAction]
         case "Buy Market Filled" =>
            None:Option[AccountAction]
         case "Sell Market Filled" =>
            None:Option[AccountAction]
         case "Buy Order" =>
            None:Option[AccountAction]
         case "Close Trade" =>
            None:Option[AccountAction]
         case "Order Expired" =>
            None:Option[AccountAction]
         case "Change Order" =>
            None:Option[AccountAction]
         case "Stop Loss" =>
            None:Option[AccountAction]
         case "Change Trade" =>
            None:Option[AccountAction]
         case "Trailing Stop" =>
            None:Option[AccountAction]
         case "Fund Deposit (Account Transfer)" =>
            None:Option[AccountAction]
         case "Fund Withdrawal (Account Transfer)" =>
            None:Option[AccountAction]
         case "Close Trade (Bulk Close All)" =>
            None:Option[AccountAction]
         case "Fund Withdrawal" =>
            None:Option[AccountAction]
         case "Order Cancelled (Bulk Close All)" =>
            None:Option[AccountAction]
         case "Change Margin" =>
            None:Option[AccountAction]
         case _ =>
            None:Option[AccountAction]
      }
   }
}
