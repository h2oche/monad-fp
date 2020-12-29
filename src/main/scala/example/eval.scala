package example

object Evaluator {
  // trait Monad[C[_]] {
  //   def pure[A](v: => A): C[A]
  //   def flatMap[A, B](m: C[A])(f: A => C[B]): C[B]
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Variation0 : Basic Evaluator
  ////////////////////////////////////////////////////////////////////////////////
  // def eval(t: Term): Int = t match {
  //   case Const(i) => i
  //   case Div(t0, t1) => eval(t0) / eval(t1)
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Revisited0 : Basic
  ////////////////////////////////////////////////////////////////////////////////
  // case class Result[T](v: T) {
  //   def map[S](f: T => S): Result[S] = Result(f(v))
  //   def flatMap[S](f: T => Result[S]): Result[S] = f(v)
  // }
  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => Result(i)
  //   case Div(t0, t1) => for (
  //     r0 <- eval(t0);
  //     r1 <- eval(t1)
  //   ) yield r0 / r1
  // }

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
  // Revisited1 : Basic
  ////////////////////////////////////////////////////////////////////////////////
  // type Ex = String
  // trait Result[+T] {
  //   def map[S](f: T => S): Result[S] = Result.map(this)(f)
  //   def flatMap[S](f: T => Result[S]): Result[S] = Result.flatMap(this)(f)
  //   // def withFilter(pred: T => Boolean): Result[T] = Result.withFilter(this)(pred)
  // }
  // case class Raise(e: Ex) extends Result[Nothing]
  // case class Return[T](r: T) extends Result[T]

  // object Result {
  //   def pure[T](v: T): Result[T] = Return(v)
  //   def map[T, S](m: Result[T])(f: T => S): Result[S] = m match {
  //     case Raise(e) => Raise(e)
  //     case Return(r) => pure(f(r))
  //   }
  //   def flatMap[T, S](m: Result[T])(f: T => Result[S]): Result[S] = m match {
  //     case Raise(e) => Raise(e)
  //     case Return(r) => f(r)
  //   }
  //   // def withFilter[T](m: Result[T])(pred: T => Boolean): Result[T] = m match {
  //   //   case Raise(e) => Raise(e)
  //   //   case Return(r) => if(pred()) m else Raise("")
  //   // }
  // }
  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => Result.pure(i)
  //   case Div(t0, t1) => for (
  //     r0 <- eval(t0);
  //     r1 <- eval(t1);
  //     r <- {
  //       if (r1 == 0) Raise("divide by zero")
  //       else if (r1 == 1) Raise("divide by one")
  //       else Result.pure(r0 / r1)
  //     }
  //   ) yield r
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Variation2 : State (which means # of division during evaluation)
  ////////////////////////////////////////////////////////////////////////////////
  // type State = Int
  // type Result[T] = State => (T, State)
  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => (st: State) => (i, st)
  //   case Div(t0, t1) => (st: State) => {
  //     val (r0, st0) = eval(t0)(st)
  //     val (r1, st1) = eval(t1)(st0)
  //     (r0 / r1, st1 + 1)
  //   }
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Revisited2 : State (which means # of division during evaluation)
  ////////////////////////////////////////////////////////////////////////////////
  // type State = Int
  // abstract class Result[+T] extends Function1[State, (T, State)] {
  //   def map[S](f: T => S): Result[S] = Result.map(this)(f)
  //   def flatMap[S](f: T => Result[S]): Result[S] = Result.flatMap(this)(f)
  // }
  // object Result {
  //   def pure[T](v: T): Result[T] = (st: State) => (v, st)
  //   def map[T, S](m: Result[T])(f: T => S): Result[S] = (st: State) => {
  //     val (v0, st0) = m(st)
  //     (f(v0), st0)
  //   }
  //   def flatMap[T, S](m : Result[T])(f: T => Result[S]): Result[S] = (st: State) => {
  //     val (v0, st0) = m(st)
  //     f(v0)(st0)
  //   }
  // }
  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => Result.pure(i)
  //   case Div(t0, t1) => for (
  //     r0 <- eval(t0);
  //     r1 <- eval(t1);
  //     r <- new Result[Int] {
  //       override def apply(st: State): (Int, State) = (r0 / r1, st + 1)
  //     }
  //   ) yield r
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Variation3 : Output
  ////////////////////////////////////////////////////////////////////////////////
  // type Output = String
  // type Result[T] = (T, Output)
  // def line(t: Term, r: Int): String = f"eval($t) => $r\n"
  // def eval(t: Term): Result[Int] = t match {
  //   case Const(i) => (i, line(t, i))
  //   case Div(t0, t1) => {
  //     val (r0, out0) = eval(t0)
  //     val (r1, out1) = eval(t1)
  //     val r = r0 / r1
  //     (r, out0 + out1 + line(t, r))
  //   }
  // }

  ////////////////////////////////////////////////////////////////////////////////
  // Revisited3 : Output
  ////////////////////////////////////////////////////////////////////////////////
  type Output = String
  case class Result[+T](v: T, o: Output) {
    def map[S](f: T => S): Result[S] = Result.map(this)(f)
    def flatMap[S](f: T => Result[S]): Result[S] = Result.flatMap(this)(f)
  }
  object Result {
    // def pure[T](v: T): Result[T] = Result(v, "")
    def map[T, S](m: Result[T])(f: T => S): Result[S] = m match {
      case Result(v, o) => Result(f(v), o)
    }
    def flatMap[T, S](m: Result[T])(f: T => Result[S]): Result[S] = m match {
      case Result(v0, o0) => f(v0) match {
        case Result(v1, o1) => Result(v1, o0 + o1)
      }
    }
  }
  def line(t: Term, r: Int): String = f"eval($t) => $r\n"
  def eval(t: Term): Result[Int] = t match {
    case Const(i) => Result(i, line(t, i))
    case Div(t0, t1) => for (
      r0 <- eval(t0);
      r1 <- eval(t1);
      r <- Result(r0 / r1, line(t, r0 / r1))
    ) yield r
  }
}
