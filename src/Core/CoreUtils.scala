package Core

import java.security.MessageDigest

object CoreUtils {
   def MD5Hash(s:String):String = {
      return MessageDigest.getInstance("MD5").digest(s.getBytes).map(0xFF & _).map {
         "%02x".format(_) }.foldLeft(""){_ + _}
   }
}
