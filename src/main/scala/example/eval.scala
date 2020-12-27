package example

object Evaluator {
  trait Term
  case class Const(i: Int) extends Term
  case class Div(t0: Term, t1: Term) extends Term

  ////////////////////////////////////////////////////////////////////////////////
  // Variation1 : Excpetion
  ////////////////////////////////////////////////////////////////////////////////
  // type Ex = String
  // trait Result[+T]
  // case class Raise(e: Ex) extends Result[Nothing]
  // case class Return[T](r: T) extends Result[T]

  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => Return(i)
  //   case Div(t0, t1) => eval(t0) match {
  //     case Raise(e) => Raise(e)
  //     case Return(r0) => eval(t1) match {
  //       case Raise(e) => Raise(e)
  //       case Return(r1) => if ( r1 == 0) Raise("divide by zero") else Return(r0 / r1)
  //     }
  //   }
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Variation2 : State (which means # of division during evaluation)
  ////////////////////////////////////////////////////////////////////////////////
  type State = Int
  type Result[T] = State => (T, State)
  def eval(t: Term): Result[Int] = t match {
    case Const(i) => (st: State) => (i, st)
    case Div(t0, t1) => (st: State) => {
      val (r0, st0) = eval(t0)(st)
      val (r1, st1) = eval(t1)(st0)
      (r0 / r1, st1 + 1)
    }
  }
}
