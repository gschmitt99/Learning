package Core

import java.io.InvalidObjectException
import java.sql.{Connection, DriverManager, ResultSet, Statement}

class DbQuery( database:String, user:String, password:String) {
   val _user:String = user
   val _password:String = password
   val _url:String = "jdbc:mysql://localhost:3306/" + database
   var _connection:Connection = null //= DriverManager.getConnection(connectionString)
   var _statement:Statement = null //= _connection.createStatement()
   var _reader:ResultSet = null
   var _query:String = null

   def open() = {
      if( _connection == null) {
         // not quite sure what this does, but it is certainly necessary
         Class.forName("com.mysql.jdbc.Driver")
         _connection = DriverManager.getConnection(_url,"greg","siboco1T")
         _statement = _connection.createStatement()
      }
   }

   def execute() = {
      try {
         _reader = _statement.executeQuery(_query)
      } catch {
         case e: Exception => throw e
      }
   }

   def read() : Boolean = {
      var retval:Boolean = false
      if( _reader == null ) {
         throw new InvalidObjectException("Reader has not been initialized; try calling execute first.")
      }
      // fairly sure this is going to advance the iterator
      if( _reader.next ) {
         retval = true
      }
      return retval;
   }

   def close()= {
      if( _reader != null ) {
         _reader.close()
         _reader = null
      }
      if( _statement != null ) {
         _statement.close()
         _statement = null
      }
      if( _connection != null ) {
         _connection.close()
         _connection = null
      }
   }
}
