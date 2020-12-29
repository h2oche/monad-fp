package example

import example.Evaluator._

object Main extends App {
  ////////////////////////////////////////////////////////////////////////////////
  // 2. Evaluating Monads
  ////////////////////////////////////////////////////////////////////////////////
  // val ans = Div(Div(Const(1972), Const(2)), Const(23))
  // val err = Div(Const(1), Const(0))
  // val err1 = Div(Const(3), Const(1))
  // println(eval(ans))
  // println(eval(err))
  // println(eval(err1))
  // println(eval(ans)(0))
  // println(eval(err)(0))

  ////////////////////////////////////////////////////////////////////////////////
  // 5. Parser
  ////////////////////////////////////////////////////////////////////////////////
  // Parser("23") == [(Const(23), "")]
  // Parser("23 and more") == [(Const(23), " and more")]
  // Parser("not a term") == []
  // Parser("(1972/2)/23)") == [(Div(Div(Const(1972), Const(2)), Const(23)), "")]
  // println(Parser("23"))
  // println(Parser("23 and more"))
  // println(Parser("not a term"))
  // println(Parser("(1972/2)/23)"))

  println(ItemParser(""))
  println(ItemParser("monad"))

  println(TwoItemParser(""))
  println(TwoItemParser("monad"))

  println(OneOrTwoItemParser(""))
  println(OneOrTwoItemParser("m"))
  println(OneOrTwoItemParser("monad"))

  println(LitParserGenerator('m')("monad"))
  println(LitParserGenerator('m')("parse"))

  println(DigitParser("23 and more"))

  val repDigitParser = DigitParser.rep
  println(repDigitParser("23 and more"))

  println(NumberParser("23 and more"))

  val rep2DigitParser = DigitParser.rep2
  println(rep2DigitParser("23 and more"))

  println(TermParser("23"))
  println(TermParser("23 and more"))
  println(TermParser("not a term"))
  println(TermParser("((1972/2)/23)"))
}
