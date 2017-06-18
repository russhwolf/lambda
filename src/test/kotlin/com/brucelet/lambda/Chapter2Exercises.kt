package com.brucelet.lambda

import org.junit.Test

class Chapter2Exercises : BaseParserTest() {
    // TODO 1?, 5, 6

    override fun Parser.initialize() {
        parseLines("""
        #def identity = λx.x
        #def self_apply = λs.(s s)
        #def apply = λfunc.λarg.(func arg)
        #def select_first = λfirst.λsecond.first
        #def select_second = λfirst.λsecond.second
        #def make_pair = λfirst.λsecond.λfunc.((func first) second)
                """)
    }

    @Test fun `2`() {
        "λp.λq.p" assertSameResult "((λx.λy.(y x) λp.λq.p) λi.i)"
        "λj.j" assertSameResult "(((λx.λy.λz.((x y) z) λf.λa.(f a)) λi.i) λj.j)"
        "(λf.(f f) λf.(f f))" assertSameResult "(λh.((λa.λf.(f a) h) h) λf.(f f))"
        "λb.λk.k" assertSameResult "((λp.λq.(p q) (λx.x λa.λb.a)) λk.k)"
        "λb.b" assertSameResult "(((λf.λg.λx.(f (g x)) λs.(s s)) λa.λb.b) λx.λy.x)"
    }

    @Test fun `3`() {
        "(identity a)" assertSameResult "((apply (apply identity)) a)"
        "((apply a) b)" assertSameResult "((λx.λy.(((make_pair x) y) identity) a) b)"
        "(identity a)" assertSameResult "((self_apply (self_apply select_second)) a)"
    }

    @Test fun `4`() {
        with(parser) {
            parseLines("""
            #def make_triplet = λfirst.λsecond.λthird.λfunc.(((func first) second) third)
            #def triplet_first = λfirst.λsecond.λthird.first
            #def triplet_second = λfirst.λsecond.λthird.second
            #def triplet_third = λfirst.λsecond.λthird.third
            """)
        }

        "((((make_triplet a) b) c) triplet_first)" assertResult "a"
        "((((make_triplet a) b) c) triplet_second)" assertResult "b"
        "((((make_triplet a) b) c) triplet_third)" assertResult "c"
    }
}