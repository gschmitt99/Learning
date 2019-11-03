package Options

object OptionTest1 {
   case class User (
      id:Int,
      firstName:String,
      lastName:String,
      age:Int,
      gender:Option[String]
   )

   def main(args: Array[String]): Unit = {
      val user1 = User(2,"John", "Doe", 30, None)
      val user2 = User(3,"Jane", "Doe", 31, Some("F"))
      println( getLine(user1))
      println( getLine(user2))
   }

   def getLine(user:User):String = {
      val gender = user.gender match {
         case Some(gender) => gender
         case None => "None Specified"
      }
      return "Gender:" + gender
   }
}
