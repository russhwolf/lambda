package com.brucelet.lambda

import org.junit.Test

class ConditionalsTest : BaseParserTest() {
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
        """)
    }

    @Test fun cond() {
        val x = "λc.((c a) b)"
        "((cond a) b)" assertResult "λc.((c a) b)"
        "($x true)" assertResult "a"
        "($x false)" assertResult "b"
    }

    @Test fun not() {
        "λx.(((cond false) true) x)" assertResult "not"
        "(not true)" assertResult "false"
        "(not false)" assertResult "true"
    }

    @Test fun and() {
        "λx.λy.(((cond y) false) x)" assertResult "and"
        "((and false) false)" assertResult "false"
        "((and false) true)" assertResult "false"
        "((and true) false)" assertResult "false"
        "((and true) true)" assertResult "true"
    }

    @Test fun or() {
        "λx.λy.(((cond true) y) x)" assertResult "or"
        "((or false) false)" assertResult "false"
        "((or false) true)" assertResult "true"
        "((or true) false)" assertResult "true"
        "((or true) true)" assertResult "true"
    }
}