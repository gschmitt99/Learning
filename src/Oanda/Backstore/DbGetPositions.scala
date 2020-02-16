package Oanda.Backstore

class DbGetPositions(
      override val database:String,
      override val user:String,
      override val pass:String,
      val accountId:Long,
      val instrument:String)
extends DbGetPosition(database,user,pass) {
   _query = s"select * from positions where Accountid=${accountId} and Instrument='${instrument}'"
}
