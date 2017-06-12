package com.brucelet.lambda

import org.junit.Test

class SimpleFunctionsTest : BaseParserTest() {
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

    @Test fun identity() {
        "identity" assertResult "identity"
        "(identity a)" assertResult "a"
        "(identity identity)" assertResult "identity"
    }

    @Test fun selfApply() {
        "self_apply" assertResult "self_apply"
        "(self_apply a)" assertResult "(a a)"
        "(self_apply identity)" assertResult "identity"
        "(identity self_apply)" assertResult "self_apply"
        "(self_apply self_apply)" assertResult "(self_apply self_apply)"
    }

    @Test fun apply() {
        "apply" assertResult "apply"
        "((apply identity) self_apply)" assertResult "self_apply"
        "((apply a) b)" assertResult "(a b)"
    }

    @Test fun identity2() {
        parser.parseLine("#def identity2 = λx.((apply identity) x)")
        "(identity2 identity)" assertResult "identity"
        "(identity2 a)" assertResult "a"
    }

    @Test fun apply2() {
        parser.parseLine("#def apply2 = λf.λa.((apply f) a)")
        "((apply2 x) y)" assertResult "(x y)"
    }

    @Test fun selfApply2() {
        parser.parseLine("#def self_apply2 = λs.((apply s) s)")
        "(self_apply2 identity)" assertResult "identity"
        "(self_apply2 a)" assertResult "(a a)"
    }

    @Test fun selectFirst() {
        "select_first" assertResult "select_first"
        "((select_first identity) apply)" assertResult "identity"
        "((select_first a) b)" assertResult "a"
    }

    @Test fun selectSecond() {
        "select_second" assertResult "select_second"
        "((select_second identity) apply)" assertResult "apply"
        "((select_second a) b)" assertResult "b"
    }

    @Test fun makePair() {
        "make_pair" assertResult "make_pair"
        "((make_pair identity) apply)" assertResult "λfunc.((func identity) apply)"
        "((make_pair a) b)" assertResult "λfunc.((func a) b)"
        "(((make_pair a) b) select_first)" assertResult "a"
        "(((make_pair a) b) select_second)" assertResult "b"
        "(λfunc.((func a) b) select_first)" assertResult "a"
        "(λfunc.((func a) b) select_second)" assertResult "b"
    }
}