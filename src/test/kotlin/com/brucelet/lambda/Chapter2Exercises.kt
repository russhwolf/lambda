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
        with(parser) {
            parseLines("""
                λp.λq.p
                ((λx.λy.(y x) λp.λq.p) λi.i)
                λj.j
                (((λx.λy.λz.((x y) z) λf.λa.(f a)) λi.i) λj.j)
                (λf.(f f) λf.(f f))
                (λh.((λa.λf.(f a) h) h) λf.(f f))
                λb.λk.k
                ((λp.λq.(p q) (λx.x λa.λb.a)) λk.k)
                λb.b
                (((λf.λg.λx.(f (g x)) λs.(s s)) λa.λb.b) λx.λy.x)
                """)
        }
        outputLines[0] assertEquals outputLines[1]
        outputLines[2] assertEquals outputLines[3]
        outputLines[4] assertEquals outputLines[5]
        outputLines[6] assertEquals outputLines[7]
        outputLines[8] assertEquals outputLines[9]
    }

    @Test fun `3`() {
        with(parser) {
            parseLines("""
                (identity a)
                ((apply (apply identity)) a)
                (apply a)
                (λx.λy.(((make_pair x) y) identity) a)
                (identity a)
                ((self_apply (self_apply select_second)) a)
                """)
        }
        outputLines[0] assertEquals outputLines[1]
        outputLines[2] assertActsAs outputLines[3]
        outputLines[4] assertEquals outputLines[5]
    }

    @Test fun `4`() {
        with(parser) {
            parseLines("""
                #def make_triplet = λfirst.λsecond.λthird.λfunc.(((func first) second) third)
                #def triplet_first = λfirst.λsecond.λthird.first
                #def triplet_second = λfirst.λsecond.λthird.second
                #def triplet_third = λfirst.λsecond.λthird.third
                ((((make_triplet a) b) c) triplet_first)
                ((((make_triplet a) b) c) triplet_second)
                ((((make_triplet a) b) c) triplet_third)
                """)
        }

        "((((make_triplet a) b) c) triplet_first)" assertResult "a"
        "((((make_triplet a) b) c) triplet_second)" assertResult "b"
        "((((make_triplet a) b) c) triplet_third)" assertResult "c"
    }
}