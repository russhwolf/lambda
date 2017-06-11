package com.brucelet.lambda

import org.junit.Assert.assertEquals

fun <T> T.assertEquals(value: T): Unit = assertEquals(value, this)
fun Expression.assertReducesTo(expression: Expression) = assertEquals(expression, reduce())
fun String.assertReducesTo(expression: String) = parseExpression().assertReducesTo(expression.parseExpression())

fun assertParseAndReduceEquals(a: String, b: String) = assertEquals(a.parseAndReduce(), b.parseAndReduce())

// TODO auto-detect substitution instead of manual
fun assertParseAndReduceEquals(a: String, b: String, from: String, to: String)
        = assertEquals(a.parseAndReduce(), b.substitute(from, to).parseAndReduce())