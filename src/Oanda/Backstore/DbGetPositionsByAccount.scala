package Oanda.Backstore

class DbGetPositionsByAccount(
      override val database:String,
      override val user:String,
      override val pass:String,
      val accountId:Long)
extends DbGetPosition(database,user,pass) {
   _query = s"select * from positions where Accountid=${accountId}"
}
