package com.brucelet.lambda

import org.junit.Test

class NumbersTest : BaseParserTest() {
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
        """)
    }

    @Test fun oneTwoThree() {
        with(parser) {
            parseLines("""
            #def one = succ zero
            #def two = succ one
            #def three = succ two
            """)
        }

        "one" assertResult "λs.((s false) zero)"
        "two" assertResult "λs.((s false) λs.((s false) zero))"
        "three" assertResult "λs.((s false) λs.((s false) λs.((s false) zero)))"
    }

    @Test fun isZero() {
        with(parser) {
            parseLines("""
            #def one = succ zero
            """)
        }
        "iszero zero" assertResult "true"
        "iszero one" assertResult "false"
    }

    @Test fun pred() {
        with(parser) {
            parseLines("""
            #def one = λs.((s false) zero)
            #def two = λs.((s false) one)
            #def three = λs.((s false) two)
            #def pred1 = λn.(n select_second)
            #def pred2 = λn.(((cond zero) (pred1 n)) (iszero n))
            """)
        }

        "(pred1 one)" assertResult "zero"
        "(pred1 zero)" assertResult "false"

        "pred one" assertResult "zero"
        "pred two" assertResult "one"
        "pred three" assertResult "two"
        "pred zero" assertResult "zero"
    }
}