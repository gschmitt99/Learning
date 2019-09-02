package Traits

trait Friendly {
   def greet() = "Hi"
}

trait ExclamatoryGreeter extends Friendly {
   override def greet() = super.greet() + "!"
}

class Dog extends Friendly {
   override def greet() = "Woof"
}

object MixinDemo {
   def main(args:Array[String]) = {
      // showing the iheritance relationship:
      val pet:Friendly = new Dog
      println( pet.greet() )

      //showing a mixin.  This is an instantiation time mixin or implementation of an interface.
      // the compiler creates a synthetic class extending dog with ExclamatoryGreeter.
      val pup:Friendly = new Dog with ExclamatoryGreeter
      println(pup.greet())
   }
}
