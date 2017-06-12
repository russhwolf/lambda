package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

infix fun <T> T.assertEquals(value: T): Unit = assertEquals(value, this)
infix fun <T> T.assertNotEquals(value: T): Unit = assertNotEquals(value, this)
infix fun Expression.assertReducesTo(expression: Expression) = assertEquals(expression, reduce())
infix fun String.assertReducesTo(expression: String) = parseExpression().assertReducesTo(expression.parseExpression())
infix fun Expression.assertActsAs(that: Expression) = "($this a)".parseAndReduce() assertEquals "($that a)".parseAndReduce()
infix fun String.assertActsAs(that: String) = this.parseExpression() assertActsAs that.parseExpression()