package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Test

class NumbersTest {
    val one = "λs.((s $FALSE) $ZERO)".parseExpression()
    val two = "λs.((s $FALSE) λs.((s $FALSE) $ZERO))".parseExpression()
    val three = "λs.((s $FALSE) λs.((s $FALSE) λs.((s $FALSE) $ZERO)))".parseExpression()

    @Test fun oneTwoThree() {
        "($SUCC $ZERO)".assertReducesTo("$one")
        "($SUCC $one)".assertReducesTo("$two")
        "($SUCC $two)".assertReducesTo("$three")
    }

    @Test fun isZero() {
        "($IS_ZERO $ZERO)".assertReducesTo("$TRUE")
        "($IS_ZERO ($SUCC $ZERO))".assertReducesTo("$FALSE")
    }

    @Test fun pred() {
        val pred1 = "λn.(n $SELECT_SECOND)"
        "($pred1 $ZERO)".assertReducesTo("$FALSE")

        val x = "λn.((($COND $ZERO) ($pred1 n)) ($IS_ZERO n))"
        assertEquals(x.parseAndReduce(), PRED.reduce())

        "($PRED $one)".assertReducesTo("$ZERO")
        "($PRED $two)".assertReducesTo("$one")
        "($PRED $three)".assertReducesTo("$two")

        "($PRED $ZERO)".assertReducesTo("$ZERO")
    }
}