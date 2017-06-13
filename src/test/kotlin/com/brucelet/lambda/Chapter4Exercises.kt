package com.brucelet.lambda

import org.junit.Test


class Chapter4Exercises : BaseParserTest() {
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
        #def ten = λs.(s false nine)
        #def eleven = λs.(s false ten)
        #def twelve = λs.(s false eleven)
        #def thirteen = λs.(s false twelve)
        #def fourteen = λs.(s false thirteen)
        #def fifteen = λs.(s false fourteen)
        #def sixteen = λs.(s false fifteen)
        #def seventeen = λs.(s false sixteen)
        #def eighteen = λs.(s false seventeen)
        #rec add x y = iszero y x (add (succ x) (pred y))
        #rec mult x y = iszero y zero (add x (mult x (pred y)))
        #rec power x y = iszero y one (mult x (power x (pred y)))
        #rec sub x y = iszero y x (sub (pred x) (pred y))
        #def abs_diff x y = add (sub x y) (sub y x)
        #def equal x y = iszero (abs_diff x y)
        #def greater x y = not (iszero (sub x y))
        #def greater_or_equal x y = iszero (sub y x)
        #rec div1 x y = (greater y x) zero (succ (div1 (sub x y) y))
        #def div x y = iszero y zero (div1 x y)
        """)
    }

    @Test fun `1`() {
        parser.parseLine("#rec sum n = iszero n zero (add n (sum (pred n)))")

        "sum three" assertResult "six"
    }

    @Test fun `2`() {
        parser.parseLine("#rec prod n = iszero n one (mult n (prod (pred n)))")

        "prod three" assertResult "six"
    }

    @Test fun `3`() {
        parser.parseLines("""
        #rec fun_sum fun n = iszero n zero (add (fun n) (fun_sum fun (pred n)))
        #def sq x = mult x x
        #def double x = add x x
        """)

        "fun_sum double three" assertResult "twelve"
        "fun_sum sq three" assertResult "fourteen"
    }

    @Test fun `4`() {
        parser.parseLines("""
        #rec fun_sum_step fun n s = iszero n zero (add (fun n) (fun_sum_step fun (sub n s) s))
        #def double x = add x x
        """)

        // TODO long
//        "fun_sum_step double five two" assertSameResult "eighteen"
        "fun_sum_step double four two" assertSameResult "twelve"
    }

    @Test fun `5`() {
        parser.parseLines("""
        #def less x y = not (iszero (sub y x))
        #def less_or_equal x y = iszero (sub x y)
        """)

        "less three two" assertResult "false"
        "less two three" assertResult "true"
        "less two two" assertResult "false"

        "less_or_equal three two" assertResult "false"
        "less_or_equal two three" assertResult "true"
        "less_or_equal two two" assertResult "true"
    }

    @Test fun `6`() {
        parser.parseLines("""
        #rec mod1 x y = (greater y x) x (mod1 (sub x y) y)
        #def mod x y = iszero y zero (mod1 x y)
        """)

        "mod three two" assertResult "one"
        "mod two three" assertResult "two"
        "mod three zero" assertResult "zero"
    }
}