package example

trait Term
case class Const(i: Int) extends Term
case class Div(t0: Term, t1: Term) extends Term

object Term {
  def eval(t: Term): Int = t match {
    case Const(i) => i
    case Div(t0, t1) => eval(t0) / eval(t1)
  }
}
