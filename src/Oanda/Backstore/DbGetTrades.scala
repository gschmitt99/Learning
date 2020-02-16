package Oanda.Backstore

class DbGetTrades(
          override val database:String,
          override val user:String,
          override val pass:String,
          val accountId:Long,
          val instrument:String)
extends DbGetTrade(database,user,pass) {
   _query = s"select * from trades where Accountid=${accountId} and Instrument='${instrument}' order by EntryFillId,EntryFillTime"
}
