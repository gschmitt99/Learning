package Oanda.Backstore

import Oanda.Models.{Account,AccountAction,Fill,Trade,TransactionRecord,DatabaseTransactionRecord}

import scala.collection.mutable

object PersistenceManager {
   val _database:String = "forex"
   val _user:String = "greg"
   val _password:String = "siboco1T"

   def insertTransactions1(account:Account,recs:List[DatabaseTransactionRecord]): Unit = {
      val query = new DbGetMaxTransactionDate(_database, _user,_password)
      try{
         query.open()
         query.execute()
         if( query.read() ) {
            val maxDate = query.getMaxDate()
            recs.foreach(r => {
               if (maxDate == null || r.Date.after(maxDate)) {
                  val addition = new DbAddTransaction( _database, _user, _password,account,r )
                  addition.open()
                  addition.execute()
                  addition.close()
               }
            })
         }
      } catch {
         case ex:Exception => println(ex.toString())
      }  finally {
         query.close()
      }

//      recs.foreach(r => {
//         val addition = new DbAddTransaction( _database, _user, _password,r )
//         try {
//            addition.open()
//            addition.execute()
//         } catch {
//            case ex:Exception => println( ex.toString())
//         } finally {
//            addition.close()
//         }
//      })
   }

   def getAccountByName(name:String): Account = {
      getAccounts().filter(_.AccountName.equals(name)).last
   }

   def getAccounts(): List[Account] = {
      val fetch = new DbGetAccounts(_database,_user,_password)
      val accountList = new mutable.MutableList[Account]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            accountList += new Account(fetch.getAccountId(),fetch.getAccountName())
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      accountList.toList
   }

   def insertTransactions(account:Account,recs:List[DatabaseTransactionRecord]): Unit = {
      recs.foreach(r => {
         val addition = new DbAddTransaction( _database, _user, _password,account,r )
         try {
            addition.open()
            addition.execute()
         } catch {
            case ex:Exception => println( ex.toString())
         } finally {
            addition.close()
         }
      })
   }

   def getTransactions(account: Account): List[Option[AccountAction]] = {
      val fetch = new DbGetTransactionsString(_database, _user, _password, account )
      val actionList = new mutable.MutableList[Option[AccountAction]]()
      try {
         fetch.open()
         fetch.execute()
         while( fetch.read()) {
            val trans:Option[AccountAction] = new TransactionRecord(
               fetch.getTicket(),
               fetch.getDate(),
               fetch.getTimezone(),
               fetch.getTransaction(),
               fetch.getDetails(),
               fetch.getInstrument(),
               fetch.getPrice(),
               fetch.getUnits(),
               fetch.getDirection(),
               fetch.getSpreadCost(),
               fetch.getStopLoss(),
               fetch.getTakeProfit(),
               fetch.getTrailingStop(),
               fetch.getFinancing(),
               fetch.getCommission(),
               fetch.getProfitLoss(),
               fetch.getAmount(),
               fetch.getBalance()
            ).transform(account,"yyyy-MM-dd hh:mm:ss")
            actionList += trans
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      actionList.toList
   }

   def insertFill(fill:Fill): Fill = {
      val addition = new DbAddFill(_database,_user,_password,fill)
      try {
         addition.open()
         fill.FillId = addition.execute()
      } catch {
         case ex:Exception => println(ex.toString)
      } finally {
         addition.close()
      }
      fill
   }

   def getFillByTicket(ticket:Long): Option[Fill] = {
      var result:Option[Fill] = None
      val fetch = new DbGetFillByTicket(_database, _user, _password, ticket )
      try {
         fetch.open()
         fetch.execute()

         result = if( fetch.read() ) {
               Some(Fill(
                  fetch.getFillId(),
                  fetch.getTicket(),
                  fetch.getAccountId(),
                  fetch.getFillDate(),
                  fetch.getInstrument(),
                  fetch.getPrice(),
                  fetch.getVolume(),
                  fetch.getDirection(),
                  fetch.getSpreadCost(),
                  fetch.getFinancing()))
            } else {
               None
            }

      } catch {
         case ex: Exception => println(ex.toString)
      } finally {
         fetch.close()
      }
      result
   }

   def insertTrade(trade:Trade): Unit = {
      val addition = new DbAddTrade( _database, _user, _password,trade )
      try {
         addition.open()
         trade.TradeId = addition.execute()
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         addition.close()
      }
   }

   def getTrades(accountId:Long,instrument:String): List[Trade] = {
      val fetch = new DbGetTrades(_database, _user,_password,accountId,instrument)
      val tradeList = new mutable.MutableList[Trade]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            val trade = new Trade(
               fetch.getTradeId(),
               fetch.getAccountId(),
               fetch.getInstrument(),
               fetch.getSequence(),
               fetch.getIsBuy(),
               fetch.getQuantity(),
               fetch.getClosedProfit(),
               fetch.getEntryFillId(),
               fetch.getEntryFillTime(),
               fetch.getEntryFillPrice(),
               fetch.getExitFillId(),
               fetch.getExitFillTime(),
               fetch.getExitFillPrice())
            tradeList += trade
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      tradeList.toList
   }

   def deleteTradesByAccountIdAndInstrument(trade:Trade): Unit = {
      val deletion = new DbDeleteTradeByAccountIdAndInstrument(_database,_user,_password,trade)
      try {
         deletion.open()
         deletion.execute()
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         deletion.close()
      }
   }

   def getSimpleTest(): Unit = {
      val fetch = new DbSimpleTest( _database, _user, _password )
      try {
         fetch.open()
         fetch.execute()
         while( fetch.read())
            {
               val i = fetch.getFirstCol()
               val r = 45
            }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
   }
}
