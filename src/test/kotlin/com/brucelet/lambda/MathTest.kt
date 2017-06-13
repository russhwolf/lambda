package com.brucelet.lambda

import org.junit.Ignore
import org.junit.Test

class MathTest : BaseParserTest() {
    override fun Parser.initialize() {
        parseLines("""
        #def recursive f = λs.(f (s s)) λs.(f (s s))
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
        #def succ = λn.λs.(s false n)
        #def iszero = λn.(n select_first)
        #def pred = λn.(iszero n zero (n select_second))
        #def implies x y = x y true
        #def equiv x y = x y (not y)
        #def one = λs.(s false zero)
        #def two = λs.(s false one)
        #def three = λs.(s false two)
        #def four = λs.(s false three)
        #def five = λs.(s false four)
        #def six = λs.(s false five)
        #def seven = λs.(s false six)
        #def eight = λs.(s false seven)
        #def nine = λs.(s false eight)
        #rec add x y = iszero y x (add (succ x) (pred y))
        #rec mult x y = iszero y zero (add x (mult x (pred y)))
        #rec power x y = iszero y one (mult x (power x (pred y)))
        #rec sub x y = iszero y x (sub (pred x) (pred y))
        #def abs_diff x y = add (sub x y) (sub y x)
        #def equal x y = iszero (abs_diff x y)
        #def greater x y = not (iszero (sub x y))
        #def greater_or_equal x y = iszero (sub y x)
        #rec div x y = (greater y x) zero (succ (div (sub x y) y))
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

    @Ignore
    @Test
    fun twoCubedTest() {
        // TODO this failed to complete after 10hr
        "power two three" assertResult "eight"
    }

    @Ignore
    @Test
    fun threeSquaredTest() {
        // TODO this takes ~40s to complete
        "power three two" assertResult "nine"
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