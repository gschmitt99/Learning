package CaseClassesAndPatternMatching

abstract class Expr
case class Var(name:String) extends Expr
case class Number(num:Double) extends Expr
case class UnOp(operator:String, arg:Expr) extends Expr
case class BinOp(operator:String, left:Expr, right:Expr) extends Expr

object Driver {
  // here, constant patterns match values that are equal to the constant (==)
  // variable patterns (e) matches every value.  Notice how e refers to the value
  // in the right side (after the =>)
  // the wildcart pattern "_" also matches every value, but does not introduce
  // a variable namem to refer to that value.
  def simplifyTop(expr:Expr):Expr = expr match {
    case UnOp("-", UnOp("-",e)) => e // double negation
    case BinOp("+", e, Number(0)) => e // adding zero
    case BinOp("*", e, Number(1)) => e // multiplying by 1
  }

  // wildcard examples:
  def wildcardExamples(expr:Expr):Unit = expr match {
    case BinOp(_,_,_) => println( expr+"is a binary operation")
    case _ => println( expr+"is something else")
  }

  // the task here is to simplify an add operator x+x into a multiply operation x*2
  def simplifyAdd(e:Expr) = e match {
    // willl not comiple, so need to use a pattern guard.
    //case BinOp("+",x,x) => BinOp("*",x,Number(2))
    case BinOp("+",x,y)  if x == y => BinOp("*",x,Number(2))
    case _ => e
  }

  def main(args:Array[String]):Unit = {
    // this will match the first case in simplify top.
     simplifyTop(UnOp("-",UnOp("-",Var("x"))))
  }
}