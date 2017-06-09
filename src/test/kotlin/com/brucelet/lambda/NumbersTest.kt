package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Test

class NumbersTest {
    @Test fun oneTwoThree() {
        "($SUCC $ZERO)".assertReducesTo("$ONE")
        "($SUCC $ONE)".assertReducesTo("$TWO")
        "($SUCC $TWO)".assertReducesTo("$THREE")
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

        "($PRED $ONE)".assertReducesTo("$ZERO")
        "($PRED $TWO)".assertReducesTo("$ONE")
        "($PRED $THREE)".assertReducesTo("$TWO")

        "($PRED $ZERO)".assertReducesTo("$ZERO")
    }
}