package example

import example.Evaluator._

object Main extends App {
  val ans = Div(Div(Const(1972), Const(2)), Const(23))
  val err = Div(Const(1), Const(0))
  val err1 = Div(Const(3), Const(1))
  println(eval(ans))
  println(eval(err))
  // println(eval(err1))
  // println(eval(ans)(0))
  // println(eval(err)(0))
}
