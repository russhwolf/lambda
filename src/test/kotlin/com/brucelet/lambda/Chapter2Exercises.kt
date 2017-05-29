package com.brucelet.lambda

import org.junit.Test

class Chapter2Exercises {
    // TODO 4, 5

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
}