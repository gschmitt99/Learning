package FuncionalProgramming

object SideEffects {
   def main(args: Array[String]): Unit = {

   }
}

case class Coffee(price:Double=.99){}

class CreditCard(var amount:Double) {
   def charge(value:Double):Unit = {
      amount -= value
   }
}
trait Payments {
   def charge(card:CreditCard,amount:Double)
}
class Cafe {
   // this is an impure function because cc.charge is a side effect.
   def buyCoffee(cc: CreditCard): Coffee = {
      val cup = new Coffee()
      cc.charge(cup.price)
      cup
   }
   // still impure, but better encapsulation of how charging takes place.
   def betterBuyCoffee(cc:CreditCard,p:Payments): Coffee = {
     val cup = new Coffee()
      p.charge(cc,cup.price)
      cup
   }
}

