package Oanda.Backstore

import java.sql.Timestamp

import Oanda.Models._

import scala.collection.mutable
import scala.util.{Failure, Try}

object PersistenceManager {
   val _database:String = "forex"
   val _user:String = "greg"
   val _password:String = "siboco1T"

   def getMaxTransactionDate(account:Account):Timestamp = {
      val query = new DbGetMaxTransactionDate( _database, _user, _password )
      Try({
         query.open()
         query.execute()
         val theDate = {
            if (query.read()) {
               query.getMaxDate()
            } else {
               new Timestamp(0)
            }
         }
         query.close()
         theDate
         }).recoverWith({
         case ex: Throwable => println(ex.toString())
            query.close()
            Failure(ex)
      }).getOrElse(new Timestamp(0))
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

   def getTransactions(account: Account, lastTransaction:Option[LastTransaction]): List[Option[AccountAction]] = {
      val fetch = new DbGetTransactionsString(_database, _user, _password, account, lastTransaction )
      val actionList = new mutable.MutableList[Option[AccountAction]]()
      try {
         fetch.open()
         fetch.execute()
         while( fetch.read()) {
            val trans:Option[AccountAction] = new TransactionRecord(
               fetch.getTransactionId(),
               fetch.getAccountId(),
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
            ).transform(account)
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

   def insertPosition(position:Position): Position = {
      val addition = new DbAddPosition( _database, _user, _password, position )
      try {
         addition.open()
         position.PositionId = addition.execute()
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         addition.close()
      }
      position
   }

   def getLastTransaction(account:Account):Option[LastTransaction] = {
      val fetch = new DbGetLastTransaction(_database,_user,_password,account)
      try {
         fetch.open()
         fetch.execute()
         if( fetch.read() ){
            return Some(LastTransaction(
               fetch.getLastTransactionId(),
               account.AccountId,
               fetch.getTransactionId()
            ))
         }
      } catch {
         case ex:Exception => println(ex.toString())
      } finally {
         fetch.close()
      }
      None
   }

   def addLastTransaction(lastTransaction:LastTransaction):Unit = {
      val insertion = new DbAddLastTransaction(_database,_user,_password,lastTransaction)
      try {
         insertion.open()
         insertion.execute()
      } catch {
         case ex:Exception => println(ex.toString())
      } finally {
         insertion.close()
      }
   }

   def updateLastTransaction(lastTransaction:LastTransaction):Unit = {
      val insertion = new DbUpdateLastTransaction(_database,_user,_password,lastTransaction)
      try {
         insertion.open()
         insertion.execute()
      } catch {
         case ex:Exception => println(ex.toString())
      } finally {
         insertion.close()
      }
   }

   def checkUpdateLastTransaction(lastTransaction:LastTransaction):Unit = {
      val lastTransactionOpt = getLastTransaction(Account(lastTransaction.AccountId,"temp"))
      if( lastTransactionOpt.isDefined ) {
         val newLastTransaction = LastTransaction(
            lastTransactionOpt.get.LastTransactionId,
            lastTransaction.AccountId,
            lastTransaction.TransactionId
         )
         updateLastTransaction(newLastTransaction)
      } else {
         addLastTransaction(lastTransaction)
      }
   }

   def getPositions(accountId:Long): mutable.Map[String,Long] = {
      val fetch = new DbGetPositionsByAccount(_database, _user, _password, accountId )
      val positions = mutable.Map[String,Long]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            val position = fetch.getPosition()
            val instrument = fetch.getInstrument()
            positions(fetch.getInstrument()) = fetch.getPosition()
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      positions
   }

   def getPositionsByInstrument(accountId:Long,instrument:String): Option[Position] = {
      val fetch = new DbGetPositions(_database, _user,_password,accountId,instrument)
      val positionList = new mutable.MutableList[Position]()
      try {
         fetch.open()
         fetch.execute()
         while(fetch.read()) {
            val position = new Position(
               fetch.getPositionId(),
               fetch.getAccountId(),
               fetch.getInstrument(),
               fetch.getPosition())
            positionList += position
         }
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         fetch.close()
      }
      if( positionList.length == 1 )
         Some(positionList.head)
      else
         None
   }

   def checkUpdatePosition(position:Position):Unit = {
      val origPosition = getPositionsByInstrument(position.AccountId,position.Instrument)
      if( origPosition.isDefined ) {
         position.PositionId = origPosition.get.PositionId
         updatePosition(position)
      } else {
         insertPosition(position)
      }
   }
   def updatePosition(position:Position): Unit = {
      val addition = new DbUpdatePosition( _database, _user, _password, position )
      try {
         addition.open()
         position.PositionId = addition.execute()
      } catch {
         case ex:Exception => println( ex.toString())
      } finally {
         addition.close()
      }
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

   def getTrades(accountId: Long):mutable.Map[String, mutable.MutableList[Trade]] = {
      val tradeMap = mutable.Map[String, mutable.MutableList[Trade]]()
      val fetch = new DbGetAllTradesByAccount(_database, _user, _password, accountId )
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
            if( !tradeMap.contains(trade.Instrument)) {
               tradeMap(trade.Instrument) = mutable.MutableList[Trade]()
            }
            tradeMap(trade.Instrument) += trade
         }
      } catch {
         case ex:Exception => println(ex.toString())
      } finally {
         fetch.close()
      }
      tradeMap
   }

   def getTradesByInstrument(accountId:Long,instrument:String): List[Trade] = {
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
}
