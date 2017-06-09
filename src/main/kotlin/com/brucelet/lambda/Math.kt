package com.brucelet.lambda

val RECURSIVE = "λf.(λs.(f (s s)) λs.(f (s s)))".parseExpression()

val ADD = "($RECURSIVE λf.λx.λy.((($COND x) ((f ($SUCC x)) ($PRED y))) ($IS_ZERO y)))".parseExpression()
val MULT = "($RECURSIVE λf.λx.λy.((($COND $ZERO) (($ADD x) ((f x) ($PRED y)))) ($IS_ZERO y)))".parseExpression()
val POWER = "($RECURSIVE λf.λx.λy.((($COND $ONE) (($MULT x) ((f x) ($PRED y)))) ($IS_ZERO y)))".parseExpression()
val SUB = "($RECURSIVE λf.λx.λy.((($COND x) ((f ($PRED x)) ($PRED y))) ($IS_ZERO y)))".parseExpression()

val ABS_DIFF = "λx.λy.(($ADD (($SUB x) y)) (($SUB y) x))".parseExpression()
val EQUAL = "λx.λy.($IS_ZERO (($ABS_DIFF x) y))".parseExpression()