package example

object Hello extends App {
  val ans = Div(Div(Const(1972), Const(2)), Const(23))
  val err = Div(Const(1), Const(0))
  println(Term.eval(ans))
  println(Term.eval(err))
}
