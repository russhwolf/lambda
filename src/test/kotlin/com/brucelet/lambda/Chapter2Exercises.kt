package com.brucelet.lambda

import org.junit.Test

class Chapter2Exercises {
    // TODO 1?, 5, 6

    @Test fun `2`() {
        assertParseAndReduceEquals("λp.λq.p", "((λx.λy.(y x) λp.λq.p) λi.i)")
        assertParseAndReduceEquals("λj.j", "(((λx.λy.λz.((x y) z) λf.λa.(f a)) λi.i) λj.j)")
        assertParseAndReduceEquals("(λf.(f f) λf.(f f))", "(λh.((λa.λf.(f a) h) h) λf.(f f))")
        assertParseAndReduceEquals("λb.λk.k", "((λp.λq.(p q) (λx.x λa.λb.a)) λk.k)")
        assertParseAndReduceEquals("λb.b", "(((λf.λg.λx.(f (g x)) λs.(s s)) λa.λb.b) λx.λy.x)")
    }

    @Test fun `3`() {
        assertParseAndReduceEquals("($IDENTITY a)", "(($APPLY ($APPLY $IDENTITY)) a)")
        assertParseAndReduceEquals("($APPLY a)", "(λx.λy.((($MAKE_PAIR x) y) $IDENTITY) a)", from = "y", to = "arg")
        assertParseAndReduceEquals("($IDENTITY a)", "(($SELF_APPLY ($SELF_APPLY $SELECT_SECOND)) a)")
    }

    @Test fun `4`() {
        val makeTriplet = "λfirst.λsecond.λthird.λfunc.(((func first) second) third)".parseExpression()
        val tripletFirst = "λfirst.λsecond.λthird.first".parseExpression()
        val tripletSecond = "λfirst.λsecond.λthird.second".parseExpression()
        val tripletThird = "λfirst.λsecond.λthird.third".parseExpression()

        "(((($makeTriplet a) b) c) $tripletFirst)".assertReducesTo("a")
        "(((($makeTriplet a) b) c) $tripletSecond)".assertReducesTo("b")
        "(((($makeTriplet a) b) c) $tripletThird)".assertReducesTo("c")
    }
}