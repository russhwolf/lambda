package com.brucelet.lambda

import org.junit.Test

class Chapter3Exercises : BaseParserTest() {
    override fun Parser.initialize() {
        parseLines("""
        #def identity x = x
        #def self_apply s = s s
        #def apply func arg = func arg
        #def select_first first second = first
        #def select_second first second = second
        #def make_pair e1 e2 c = c e1 e2
        #def cond = make_pair
        #def true = select_first
        #def false = select_second
        #def not x = x false true
        #def and x y = x y false
        #def or x y = x true y
        #def zero = identity
        #def succ = λn.λs.((s false) n)
        #def iszero = λn.(n select_first)
        #def pred = λn.(((iszero n) zero) (n select_second))
        #def implies x y = x y true
        #def equiv x y = x y (not y)
        """)
    }

    @Test fun `1`() {
        "λx.λy.(cond y true x)" assertResult "implies"
        "implies false false" assertResult "true"
        "implies false true" assertResult "true"
        "implies true false" assertResult "false"
        "implies true true" assertResult "true"
    }

    @Test fun `2`() {
        "λx.λy.(cond y (not y) x)" assertSameResult "equiv"
        "equiv false false" assertResult "true"
        "equiv false true" assertResult "false"
        "equiv true false" assertResult "false"
        "equiv true true" assertResult "true"
    }

    @Test fun `3`() {
        `3 helper`("λx.λy.((and (not x)) (not y))", "λx.λy.(not ((or x) y))")
        `3 helper`("implies", "λx.λy.((implies (not y)) (not x))")
        `3 helper`("not", "λx.(not (not (not x)))") // NB this is fine, 2nd arg is just ignored
        `3 helper`("implies", "λx.λy.(not ((and x) (not y)))")
        `3 helper`("equiv", "λx.λy.((and ((implies x) y)) ((implies y) x))")
    }

    fun `3 helper`(a: String, b: String) {
        "$a false false" assertSameResult "$b false false"
        "$a false true" assertSameResult "$b false true"
        "$a true false" assertSameResult "$b true false"
        "$a true true" assertSameResult "$b true true"
    }

    @Test fun `4`() {
        with(parser) {
            parseLine("#def arg = succ a")  // A generic number is the successor of something
        }
        "(λx.(succ (pred x)) arg)" assertSameResult "(λx.(pred (succ x)) arg)"

//        // These will not be equal, because PRED only undoes SUCC for an argument which has had SUCC applied to it
        "(λx.(succ (pred x)) zero)" assertNotSameResult "(λx.(pred (succ x)) zero)"
    }
}
