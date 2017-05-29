package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionsTest {
    val a = Name("a")
    val b = Name("b")

    @Test fun identity() {
        IDENTITY.assertReducesTo(IDENTITY)
        Application(IDENTITY, a).assertReducesTo(a)
        Application(IDENTITY, IDENTITY).assertReducesTo(IDENTITY)
    }

    @Test fun selfApply() {
        SELF_APPLY.assertReducesTo(SELF_APPLY)
        Application(SELF_APPLY, a).assertReducesTo(Application(a, a))
        Application(SELF_APPLY, IDENTITY).assertReducesTo(IDENTITY)
        Application(IDENTITY, SELF_APPLY).assertReducesTo(SELF_APPLY)
        Application(SELF_APPLY, SELF_APPLY).assertReducesTo(Application(SELF_APPLY, SELF_APPLY))
    }

    @Test fun apply() {
        APPLY.assertReducesTo(APPLY)
        Application(Application(APPLY, a), b).assertReducesTo(Application(a, b))
        Application(Application(APPLY, IDENTITY), SELF_APPLY).assertReducesTo(SELF_APPLY)
    }

    @Test fun identity2() {
        val identity2 = Function("x", Application(Application(APPLY, IDENTITY), "x"))
        Application(identity2, IDENTITY).assertReducesTo(IDENTITY)
        Application(identity2, a).assertReducesTo(a)
    }

    @Test fun selfApply2() {
        val selfApply2 = Function("s", Application(Application(APPLY, "s"), "s"))
        Application(selfApply2, IDENTITY).assertReducesTo(IDENTITY)
        Application(selfApply2, a).assertReducesTo(Application(a, a))
    }

    @Test fun selectFirst() {
        SELECT_FIRST.assertReducesTo(SELECT_FIRST)
        Application(Application(SELECT_FIRST, IDENTITY), APPLY).assertReducesTo(IDENTITY)
        Application(Application(SELECT_FIRST, a), b).assertReducesTo(a)
    }

    @Test fun selectSecond() {
        SELECT_SECOND.assertReducesTo(SELECT_SECOND)
        Application(Application(SELECT_SECOND, a), b).assertReducesTo(b)
        // TODO auto-detect substitution instead of manual
        Application(SELECT_SECOND, a).assertReducesTo(IDENTITY.substitute("x", "second"))
    }

    @Test fun makePair() {
        val x = Application(Application(MAKE_PAIR, a), b)
        x.assertReducesTo(Function("func", Application(Application("func", a), b)))
        Application(x, SELECT_FIRST).assertReducesTo(a)
        Application(x, SELECT_SECOND).assertReducesTo(b)
    }

    private fun Expression.assertReducesTo(expression: Expression) = assertEquals(expression, reduce())
}