package com.brucelet.lambda

import org.junit.Assert.assertNotEquals
import org.junit.Test

class Chapter3Exercises {
    // a ? b : c  ==>  ((($COND b) c) a)  ==>  ((a b) c)

    // x ? y : true
    val implies = "λx.λy.((x y) $TRUE)".parseExpression()
    // x ? y : !y
    val equiv = "λx.λy.((x y) ($NOT y))".parseExpression()

    @Test fun `1`() {
        val x = "λx.λy.((($COND y) $TRUE) x)"
        x.assertReducesTo("$implies")
        "(($implies $FALSE) $FALSE)".assertReducesTo("$TRUE")
        "(($implies $FALSE) $TRUE)".assertReducesTo("$TRUE")
        "(($implies $TRUE) $FALSE)".assertReducesTo("$FALSE")
        "(($implies $TRUE) $TRUE)".assertReducesTo("$TRUE")
    }

    @Test fun `2`() {
        val x = "λx.λy.((($COND y) ($NOT y)) x)"
        assertParseAndReduceEquals(x, "$equiv")
        "(($equiv $FALSE) $FALSE)".assertReducesTo("$TRUE")
        "(($equiv $FALSE) $TRUE)".assertReducesTo("$FALSE")
        "(($equiv $TRUE) $FALSE)".assertReducesTo("$FALSE")
        "(($equiv $TRUE) $TRUE)".assertReducesTo("$TRUE")
    }

    @Test fun `3`() {
        `3 helper`("λx.λy.(($AND ($NOT x)) ($NOT y))", "λx.λy.($NOT (($OR x) y))")
//        `3 helper`("$implies", "λx.λy.(($implies ($NOT y)) ($NOT x))") // TODO fails on true/true check
        `3 helper`("$NOT", "λx.($NOT ($NOT ($NOT x)))") // NB this is fine, 2nd arg is just ignored
        `3 helper`("$implies", "λx.λy.($NOT (($AND x) ($NOT y)))")
//        `3 helper`("$equiv", "λx.λy.(($AND (($implies x) y)) (($implies y) x))") // TODO fails on false/true and true/false
    }

    fun `3 helper`(a: String, b: String) {
        assertParseAndReduceEquals("(($a $FALSE) $FALSE)", "(($b $FALSE) $FALSE)")
        assertParseAndReduceEquals("(($a $FALSE) $TRUE)", "(($b $FALSE) $TRUE)")
        assertParseAndReduceEquals("(($a $TRUE) $FALSE)", "(($b $TRUE) $FALSE)")
        assertParseAndReduceEquals("(($a $TRUE) $TRUE)", "(($b $TRUE) $TRUE)")
    }

    @Test fun `4`() {
        val arg = "($SUCC a)" // A generic number is the successor of something
        assertParseAndReduceEquals("(λx.($SUCC ($PRED x)) $arg)", "(λx.($PRED ($SUCC x)) $arg)")
        // These will not be equal, because PRED only undoes SUCC for an argument which has had SUCC applied to it
        assertNotEquals("(λx.($SUCC ($PRED x)) $ZERO)".parseAndReduce(), "(λx.($PRED ($SUCC x)) $ZERO)".parseAndReduce())
    }
}
