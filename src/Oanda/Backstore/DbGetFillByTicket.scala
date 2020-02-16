package Oanda.Backstore

class DbGetFillByTicket(
      override val database:String,
      override val user:String,
      override val pass:String,
      val ticket:Long)
extends DbGetFill(database,user,pass) {

   _query = s"select * from fills where Ticket=${ticket}"
}
