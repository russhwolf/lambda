package com.brucelet.lambda

import org.junit.Test

class MathTest : BaseParserTest() {
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
        #def one = λs.((s false) zero)
        #def two = λs.((s false) one)
        #def three = λs.((s false) two)
        #def four = λs.((s false) three)
        #def five = λs.((s false) four)
        #def six = λs.((s false) five)
        #def seven = λs.((s false) six)
        #def eight = λs.((s false) seven)
        #def nine = λs.((s false) eight)
        #def recursive f = λs.(f (s s)) λs.(f (s s))
        #def add = recursive λf.λx.λy.(cond x (f (succ x) (pred y)) (iszero y))
        #def mult = recursive λf.λx.λy.(cond zero (add x (f x (pred y))) (iszero y))
        #def power = recursive λf.λx.λy.(cond one (mult x (f x (pred y))) (iszero y))
        #def sub = recursive λf.λx.λy.(cond x (f (pred x) (pred y)) (iszero y))
        #def abs_diff x y = add (sub x y) (sub y x)
        #def equal x y = iszero (abs_diff x y)
        #def greater x y = not (iszero (sub x y))
        #def greater_or_equal x y = iszero (sub y x)
        #def div = recursive λf.λx.λy.(cond zero (succ (f (sub x y) y)) (greater y x))
        """)
    }

    @Test
    fun addTest() {
        "add one two" assertResult "three"
    }

    @Test
    fun multTest() {
        "mult two three" assertResult "six"
    }

    @Test
    fun powerTest() {
        "power two zero" assertResult "one"
        "power two one" assertResult "two"
        "power two two" assertResult "four"
    }

    @Test
    fun twoCubedTest() {
        // TODO this failed to complete after 30 min
//        "power two three" assertResult "eight"
    }

    @Test
    fun threeSquaredTest() {
        // TODO this takes ~40s to complete
//        "power three two" assertResult "nine"
    }

    @Test
    fun subTest() {
        "sub four two" assertResult "two"
        "sub one two" assertResult "zero"
    }

    @Test
    fun equalTest() {
        "equal two two" assertResult "true"
        "equal two three" assertResult "false"
    }

    @Test
    fun greaterTest() {
        "greater three two" assertResult "true"
        "greater_or_equal two three" assertResult "false"
    }

    @Test
    fun divTest() {
        "div nine four" assertResult "two"
    }
}