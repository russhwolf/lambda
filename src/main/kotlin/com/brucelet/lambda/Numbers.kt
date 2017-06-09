package com.brucelet.lambda

val ZERO = IDENTITY
val SUCC = "λn.λs.((s $FALSE) n)".parseExpression()
val IS_ZERO = "λn.(n $SELECT_FIRST)".parseExpression()
val PRED = "λn.((($IS_ZERO n) $ZERO) (n $SELECT_SECOND))".parseExpression()

val ONE = "λs.((s $FALSE) $ZERO)".parseExpression()
val TWO = "λs.((s $FALSE) λs.((s $FALSE) $ZERO))".parseExpression()
val THREE = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO)))".parseExpression()
val FOUR = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO))))".parseExpression()
val FIVE = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO)))))".parseExpression()
val SIX = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO))))))".parseExpression()
val SEVEN = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO)))))))".parseExpression()
val EIGHT = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO))))))))".parseExpression()
val NINE = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO)))))))))".parseExpression()
