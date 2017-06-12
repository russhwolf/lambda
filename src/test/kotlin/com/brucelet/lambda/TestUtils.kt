package com.brucelet.lambda

import org.junit.Assert.assertEquals

infix fun <T> T.assertEquals(value: T): Unit = assertEquals(value, this)
infix fun Expression.assertReducesTo(expression: Expression) = assertEquals(expression, reduce())
infix fun String.assertReducesTo(expression: String) = parseExpression().assertReducesTo(expression.parseExpression())
infix fun Expression.assertActsAs(that: Expression) = "($this a)".parseAndReduce() assertEquals "($that a)".parseAndReduce()
infix fun String.assertActsAs(that: String) = this.parseExpression() assertActsAs that.parseExpression()

fun assertParseAndReduceEquals(a: String, b: String) = assertEquals(a.parseAndReduce(), b.parseAndReduce())

// TODO auto-detect substitution instead of manual
fun assertParseAndReduceEquals(a: String, b: String, from: String, to: String)
        = assertEquals(a.parseAndReduce(), b.substitute(from, to).parseAndReduce())