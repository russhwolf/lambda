package com.brucelet.lambda

val COND = "λe1.λe2.λc.((c e1) e2)".parseExpression()
val TRUE = SELECT_FIRST
val FALSE = SELECT_SECOND
val NOT = "λx.((x $FALSE) $TRUE)".parseExpression()
val AND = "λx.λy.((x y) $FALSE)".parseExpression()
val OR = "λx.λy.((x $TRUE) y)".parseExpression()