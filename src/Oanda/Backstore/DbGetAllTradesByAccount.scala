package Oanda.Backstore

class DbGetAllTradesByAccount(
          override val database:String,
          override val user:String,
          override val pass:String,
          val accountId:Long
          )
extends DbGetTrade(database,user,pass) {
   _query = s"select * from trades where Accountid=${accountId} order by EntryFillId,EntryFillTime"
}
