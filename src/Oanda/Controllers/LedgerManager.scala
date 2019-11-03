package Oanda.Controllers

import Oanda.Backstore.PersistenceManager
import java.util.Date

import Oanda.Models._

class LedgerManager {

}

object LedgerManager {

   def getTransactions(account: Account): List[Option[AccountAction]] = {
      PersistenceManager.getTransactions(account)
   }

    def startup(accountName:String): Unit = {
       val account = PersistenceManager.getAccountByName(accountName)
       val actions = getTransactions(account)
       // TODO: remove for placing elsewhere.
       val c = new FillWeight(5,3.2)
       actions.foreach( a => {

          if( a.isDefined ) {
             a.get match {
                case c: Create =>
                case c: ClientConfigure =>
                case f: OrderFill => {
                   Ledger.addFill(f)
                }
                case c: ClientConfigure =>
                case f: TransferFunds =>
                   Ledger.addFunds(f)
                case c: FixedPriceOrder =>
                case c: MarketOrder =>
                case c: OrderCancel =>
                case f: DailyTicketFinancing =>
                   Ledger.addDailyTicketFinancing(f)
                case f: DailyFinancingSummary =>
                   Ledger.addDailyFinancingSummary(f)
             }
          }
          else {
             val r = 45
          }
       })
       //val ledger = Ledger.create(accountActions)
      //ledger.calculate()
      //ledger.dumpFills("EUR/USD")
      //val r = 45
   }
}
