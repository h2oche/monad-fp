package example

case class State(st: String)
case class ParseResult[T](res: T, remains: State) {
  def map[S](f: T => S): ParseResult[S] = ParseResult(f(res), remains)
}
case class ParseResults[T](l: List[ParseResult[T]]) {
  def map[S](f: T => S): ParseResults[S] = ParseResults(l.map(_.map(f)))
  def filter(pred: T => Boolean): ParseResults[T] = ParseResults(l.filter {
    case ParseResult(res, _) => pred(res)
  })
}

trait Parser[T] extends Function1[State, ParseResults[T]] {
  def pure(v: T): Parser[T] = (st: State) =>
    ParseResults(List(ParseResult(v, st)))
  def map[S](f: T => S): Parser[S] = (st: State) => this(st).map(f)
  def flatMap[S](f: T => Parser[S]): Parser[S] = (st: State) =>
    this(st) match {
      case ParseResults(l) =>
        ParseResults(l.flatMap { case ParseResult(res, remains) =>
          f(res)(remains).l
        })
    }
  def +(p: Parser[T]): Parser[T] = (st: State) => {
    val pres = this(st)
    if (!pres.l.isEmpty) pres else p(st)
  }
  def ++(p: Parser[T]): Parser[T] = (st: State) =>
    ParseResults(this(st).l ++ p(st).l)
  def filter(pred: T => Boolean): Parser[T] = (st: State) =>
    this(st).filter(pred)
  lazy val rep: Parser[List[T]] = {
    val repParser: Parser[List[T]] = for (v <- this; l <- this.rep) yield v :: l
    repParser ++ Parser.pure(List.empty[T])
  }
  lazy val rep2: Parser[List[T]] = {
    val repParser: Parser[List[T]] =
      for (v <- this; l <- this.rep2) yield v :: l
    repParser + Parser.pure(List.empty[T])
  }
  def apply(s: String): ParseResults[T] = this(State(s))
}

object Parser {
  def pure[T](v: T): Parser[T] = (st: State) =>
    ParseResults(List(ParseResult(v, st)))
  def zero[T]: Parser[T] = (st: State) => ParseResults(List())
}

object ItemParser extends Parser[Char] {
  def apply(st: State): ParseResults[Char] = ParseResults(st match {
    case State(s) if s.length > 0 =>
      List(ParseResult(s.charAt(0), State(s.substring(1))))
    case _ => List()
  })
}

object TwoItemParser extends Parser[(Char, Char)] {
  def apply(st: State): ParseResults[(Char, Char)] = {
    // ItemParser.flatMap( (a: Char) => {
    //   ItemParser.flatMap( (b: Char) => {
    //     Parser.pure((a,b))
    //   })
    // })(st)
    val parser = for (a <- ItemParser; b <- ItemParser) yield (a, b)
    parser(st)
  }
}

object OneOrTwoItemParser extends Parser[String] {
  def apply(st: State): ParseResults[String] = {
    val oneParser = ItemParser.map((a: Char) => a.toString)
    val twoParser = TwoItemParser.map { case (a, b) =>
      a.toString + b.toString
    }
    (oneParser ++ twoParser)(st)
  }
}

object LetterParser extends Parser[Char] {
  def apply(st: State): ParseResults[Char] = ItemParser.filter(_.isLetter)(st)
}

object DigitParser extends Parser[Int] {
  def apply(st: State): ParseResults[Int] =
    ItemParser.filter(_.isDigit).map(c => c - '0')(st)
}

object LitParserGenerator {
  def apply(m: Char): Parser[Char] = ItemParser.filter(_ == m)
}

object NumberParser extends Parser[Int] {
  def adder(l: List[Int]): Int = l.reverse.zipWithIndex.foldLeft(0) {
    case (acc, (n, i)) => acc + n * Math.pow(10, i).toInt
  }
  def apply(st: State): ParseResults[Int] = {
    val p0 = for (i <- DigitParser; ilist <- DigitParser.rep) yield i :: ilist
    p0.map(adder)(st)
  }
}

object TermParser extends Parser[Term] {
  def apply(st: State): ParseResults[Term] = {
    val constp: Parser[Term] = NumberParser.map(Const(_))
    val divp: Parser[Term] =
      for (
        _  <- LitParserGenerator('(');
        t0 <- TermParser;
        _  <- LitParserGenerator('/');
        t1 <- TermParser;
        _  <- LitParserGenerator(')')
      ) yield Div(t0, t1)
    (constp ++ divp)(st)
  }
}
