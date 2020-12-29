# Monads for FP

Pure <--- monads --> Impure
monads integrates impure effects into pure FP.

- simple evaluator
- monad laws
- monads application
- recusrive descent parsers

## Evaluating monads

Pure functional langauages have this advantage: all flow of data is made explicit.
disadvantage : sometimes it is painfully explicit

basic evaluator for simple terms
+ exceptions
+ state
+ output

Each of the variations on the intepreter has a similar structure, which may be
abstracted to yield the notion of a *monad*.

A monad is a triple (M, unit, >>=)
- M[T] : type constructor
- unit[T] : T -> M[T], which means how to wrap value to default computation
- bind[T, S] : M[T] -> (T -> M[S]) -> M[S], which means how to sequence two computations

## Monad Laws
- left unit ::= f = b => n; bind(unit(a), f) == f(a)
- right unit ::= bind(m, a => unit(a)) == m
- assosiative ::= bind(m, a => bind(n, b => o)) == bind(bind(m, a => n), b => o)

## Parsers

### Lists
If monads encapsulate effects and lists form a monad, do lists correspond to some effect?
Indeed they do, and the effect they correspond to do is choice.
In short, List[A] can be regarded as offering choice of value of type A.

### Parsers
Parsers are represented in a way similar to state transformers.
A parser m is unambiguous if for every input x the list of possible parses m x
is either empty of has exactly one item.
