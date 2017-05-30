package com.brucelet.lambda

import org.junit.Test

class ConditionalsTest {
    @Test fun cond() {
        val x = "λc.((c a) b)"
        "(($COND a) b)".assertReducesTo(x)
        "($x $TRUE)".assertReducesTo("a")
        "($x $FALSE)".assertReducesTo("b")
    }

    @Test fun not() {
        val x = "λx.((($COND $FALSE) $TRUE) x)"
        x.assertReducesTo("$NOT")
        "($NOT $TRUE)".assertReducesTo("$FALSE")
        "($NOT $FALSE)".assertReducesTo("$TRUE")
    }

    @Test fun and() {
        val x = "λx.λy.((($COND y) $FALSE) x)"
        x.assertReducesTo("$AND")
        "(($AND $FALSE) $FALSE)".assertReducesTo("$FALSE")
        "(($AND $FALSE) $TRUE)".assertReducesTo("$FALSE")
        "(($AND $TRUE) $FALSE)".assertReducesTo("$FALSE")
        "(($AND $TRUE) $TRUE)".assertReducesTo("$TRUE")
    }

    @Test fun or() {
        val x = "λx.λy.((($COND $TRUE) y) x)"
        x.assertReducesTo("$OR")
        "(($OR $FALSE) $FALSE)".assertReducesTo("$FALSE")
        "(($OR $FALSE) $TRUE)".assertReducesTo("$TRUE")
        // TODO auto-detect substitution instead of manual
        "(($OR $TRUE) $FALSE)".substitute("y", "second").assertReducesTo("$TRUE")
        "(($OR $TRUE) $TRUE)".substitute("y", "second").assertReducesTo("$TRUE")
    }
}