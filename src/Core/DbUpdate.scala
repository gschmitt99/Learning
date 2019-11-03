package Core

import java.io.InvalidObjectException
import java.sql.{Connection, DriverManager, ResultSet, Statement}

class DbUpdate(database:String, user:String, password:String) {
   val _user:String = user
   val _password:String = password
   val _url:String = "jdbc:mysql://localhost:3306/" + database
   var _connection:Connection = null //= DriverManager.getConnection(connectionString)
   var _statement:Statement = null //= _connection.createStatement()
   var _query:String = null

   def open() = {
      if( _connection == null) {
         // not quite sure what this does, but it is certainly necessary
         Class.forName("com.mysql.jdbc.Driver")
         _connection = DriverManager.getConnection(_url,"greg","siboco1T")
         _statement = _connection.createStatement()
      }
   }

   def execute(): Int = {
      var retval:Int = -1
      try {
         //_statement.execute(_query)
         _statement.executeUpdate(_query, Statement.RETURN_GENERATED_KEYS)
         val rs = _statement.getGeneratedKeys()
         if( rs.next() ) {
            retval = rs.getInt(1)
         }
      } catch {
         case e: Exception => throw e
      }
      return retval
   }

   def close()= {
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
