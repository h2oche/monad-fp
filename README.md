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
- >>=[T, S] : M[T] -> (T -> M[S]) -> M[S], which means how to sequence two computations
