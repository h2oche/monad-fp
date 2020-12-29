package example

trait Term
case class Const(i: Int) extends Term
case class Div(t0: Term, t1: Term) extends Term
