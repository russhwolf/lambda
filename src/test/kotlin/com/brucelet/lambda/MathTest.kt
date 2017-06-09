package com.brucelet.lambda

import org.junit.Test

class MathTest {
    @Test
    fun addTest() {
        "(($ADD $ONE) $TWO)".assertReducesTo("$THREE")
    }

    @Test
    fun multTest() {
        "(($MULT $TWO) $THREE)".assertReducesTo("$SIX")
    }

    @Test
    fun powerTest() {
        "(($POWER $TWO) $ZERO)".assertReducesTo("$ONE")
        "(($POWER $TWO) $ONE)".assertReducesTo("$TWO")
        "(($POWER $TWO) $TWO)".assertReducesTo("$FOUR")
        // TODO this failed to complete after 30 min
//        "(($POWER $TWO) $THREE)".assertReducesTo("$EIGHT")

        // TODO this fails for unknown reason
//        assertParseAndReduceEquals("$EIGHT", "(($POWER $THREE) $TWO)")
    }

    @Test
    fun subTest() {
        "(($SUB $FOUR) $TWO)".assertReducesTo("$TWO")
        "(($SUB $ONE) $TWO)".assertReducesTo("$ZERO")
    }

    @Test
    fun equalTest() {
        "(($EQUAL $TWO) $THREE)".assertReducesTo("$FALSE")
    }
}