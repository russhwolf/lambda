package com.brucelet.lambda

val ZERO = IDENTITY
val SUCC = "λn.λs.((s $FALSE) n)".parseExpression()
val IS_ZERO = "λn.(n $SELECT_FIRST)".parseExpression()
val PRED = "λn.((($IS_ZERO n) $ZERO) (n $SELECT_SECOND))".parseExpression()
