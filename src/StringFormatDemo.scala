object StringFormatDemo {
   def main(args: Array[String]) {
      var floatVar = 12.456
      var intVar = 2000
      var stringVar = "Hello, Scala!"

      var fs = printf("The value of the float variable is "
         + "%f, while the value of the integer "
         + "variable is %d, and the string"
         + "is %s"
         , floatVar, intVar, stringVar);

      println(fs)

      // string interpolation examples
      val name = "James"
      println(s"Hello,$name")
      println(s"1+1=${1 + 1}")

      // f interpolation; similar to printf format strings
      val height = 1.9d
      println(f"$name%s is $height%2.2f meters tall") //James is 1.90 meters tall

      // raw interpolation; similar to C# @
      println(raw"Result = \n a \n b")
   }
}
