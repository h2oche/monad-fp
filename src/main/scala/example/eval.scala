package example

object Evaluator {
  trait Term
  case class Const(i: Int) extends Term
  case class Div(t0: Term, t1: Term) extends Term

  type Ex = String
  trait Result[+T]
  case class Raise(e: Ex) extends Result[Nothing]
  case class Return[T](r: T) extends Result[T]

  def eval(t: Term): Result[Int] = t match {
    case Const(i) => Return(i)
    case Div(t0, t1) => eval(t0) match {
      case Raise(e) => Raise(e)
      case Return(r0) => eval(t1) match {
        case Raise(e) => Raise(e)
        case Return(r1) => if (r1 == 0) Raise("divide by zero") else Return(r0 / r1)
      }
    }
  }
}
