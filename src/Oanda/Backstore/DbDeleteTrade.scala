package Oanda.Backstore

import Core.DbUpdate
import Oanda.Models.Trade

class DbDeleteTradeByAccountIdAndInstrument(val database:String, val user:String, val pass:String, val trade:Trade)
extends DbUpdate(database,user,pass) {
   _query = s"delete from trades where AccountId=${trade.AccountId} and Instrument='${trade.Instrument}'"
}
