package com.brucelet.lambda

val IDENTITY = "λx.x".parseExpression()
val SELF_APPLY = "λs.(s s)".parseExpression()
val APPLY = "λfunc.λarg.(func arg)".parseExpression()
val SELECT_FIRST = "λfirst.λsecond.first".parseExpression()
val SELECT_SECOND = "λfirst.λsecond.second".parseExpression()
val MAKE_PAIR = "λfirst.λsecond.λfunc.((func first) second)".parseExpression()
