package com.brucelet.lambda

import org.junit.Test

class TypesTest : BaseParserTest() {
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
        #def not x = #if x #then false #else true
        #def and x y = #if x #then y #else false
        #def or x y = #if x #then true #else y

        #def zero = identity
        #def succ = λn.λs.(s false n)
        #def iszero = λn.(n select_first)
        #def pred n = #if iszero n #then zero #else n select_second
        #def implies x y = #if x #then y #else true
        #def equiv x y = #if x #then y #else (not y)
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

        #rec add x y = #if iszero y #then x #else add (succ x) (pred y)
        #rec mult x y = #if iszero y #then zero #else add x (mult x (pred y))
        #rec power x y = #if iszero y #then one #else mult x (power x (pred y))
        #rec sub x y = #if iszero y #then x #else sub (pred x) (pred y)
        #def abs_diff x y = add (sub x y) (sub y x)
        #def equal x y = iszero (abs_diff x y)
        #def greater x y = not (iszero (sub x y))
        #def greater_or_equal x y = iszero (sub y x)
        #rec div1 x y = #if greater y x #then zero #else succ (div1 (sub x y) y)
        #def div x y = #if iszero y #then zero #else div1 x y


        #def make_obj type value = λs.(s type value)
        #def type obj = obj select_first
        #def value obj = obj select_second
        #def istype t obj = equal (type obj) t

        #def error_type = zero
        #def MAKE_ERROR = λvalue.λs.(s error_type value)
        #def ERROR = λs.(s error_type error_type)
        #def iserror = istype error_type

        #def bool_type = one
        #def MAKE_BOOL = λvalue.λs.(s bool_type value)
        #def TRUE = λs.(s bool_type true)
        #def FALSE = λs.(s bool_type false)
        #def isbool = istype bool_type
        #def BOOL_ERROR = λs.(s error_type bool_type)

        #def NOT X = #if isbool X #then MAKE_BOOL (not (value X)) #else BOOL_ERROR
        #def AND X Y = #if and (isbool X) (isbool Y) #then MAKE_BOOL (and (value X) (value Y)) #else BOOL_ERROR

        #def COND E1 E2 C = #if isbool C #then C E1 E2 #else BOOL_ERROR
        #def ISERROR E = MAKE_BOOL (iserror E)
        #def ISBOOL B = MAKE_BOOL (isbool B)

        #def numb_type = two
        #def MAKE_NUMB = λvalue.λs.(s numb_type value)
        #def NUMB_ERROR = λs.(s error_type numb_type)
        #def isnumb = istype numb_type
        #def ISNUMB N = MAKE_BOOL (isnumb N)
        #def 0 = λs.(s numb_type zero)
        #def SUCC N = #if isnumb N #then MAKE_NUMB (succ (value N)) #else NUMB_ERROR
        #def PRED N = #if isnumb N #then (#if iszero (value N) #then NUMB_ERROR #else MAKE_NUMB ((value N) select_second)) #else NUMB_ERROR
        #def 1 = λs.(s numb_type one)
        #def 2 = λs.(s numb_type two)
        #def 3 = λs.(s numb_type three)
        #def 4 = λs.(s numb_type four)
        #def 5 = λs.(s numb_type five)
        #def 6 = λs.(s numb_type six)
        #def 7 = λs.(s numb_type seven)
        #def 8 = λs.(s numb_type eight)
        #def 9 = λs.(s numb_type nine)
        #def ISZERO N = #if isnumb N #then MAKE_BOOL (iszero (value N)) #else NUMB_ERROR
        #def both_numbs X Y = and (isnumb X) (isnumb Y)
        #def + X Y = #if both_numbs X Y #then MAKE_NUMB (add (value X) (value Y)) #else NUMB_ERROR
        #def - X Y = #if both_numbs X Y #then MAKE_NUMB (sub (value X) (value Y)) #else NUMB_ERROR
        #def * X Y = #if both_numbs X Y #then MAKE_NUMB (mult (value X) (value Y)) #else NUMB_ERROR
        #def / X Y = #if both_numbs X Y #then (#if iszero (value Y) #then NUMB_ERROR #else MAKE_NUMB (div1 (value X) (value Y))) #else NUMB_ERROR
        #def EQUAL X Y = #if both_numbs X Y #then MAKE_BOOL (equal (value X) (value Y)) #else NUMB_ERROR
        """)
    }

    @Test fun errorType() {
        "make_obj error_type" assertResult "MAKE_ERROR"
        "MAKE_ERROR error_type" assertResult "ERROR"

        "type ERROR" assertResult "error_type"
        "iserror ERROR" assertResult "true"
    }

    @Test fun boolType() {
        "make_obj bool_type" assertResult "MAKE_BOOL"
        "MAKE_BOOL true" assertResult "TRUE"
        "MAKE_BOOL false" assertResult "FALSE"
        "MAKE_ERROR bool_type" assertResult "BOOL_ERROR"

        "isbool TRUE" assertResult "true"
        "isbool FALSE" assertResult "true"
        "isbool ERROR" assertResult "false"
        "AND TRUE FALSE" assertResult "FALSE"
        "NOT FALSE" assertResult "TRUE"
    }

    @Test fun numbType() {
        "make_obj numb_type" assertResult "MAKE_NUMB"
        "MAKE_ERROR numb_type" assertResult "NUMB_ERROR"

        "MAKE_NUMB zero" assertSameResult "0"
        "SUCC 0" assertSameResult "1"
        "SUCC 1" assertSameResult "2"
        "SUCC 2" assertSameResult "3"
        "PRED 3" assertSameResult "2"
        "PRED 2" assertSameResult "1"
        "PRED 1" assertSameResult "0"
        "PRED 0" assertSameResult "NUMB_ERROR"

        "isnumb 0" assertResult "true"
        "isnumb 1" assertResult "true"
        "isnumb 2" assertResult "true"
        "isnumb 3" assertResult "true"
        "isnumb TRUE" assertResult "false"
        "isnumb FALSE" assertResult "false"
        "isnumb ERROR" assertResult "false"

        "+ 1 2" assertSameResult "3"
        "- 4 2" assertSameResult "2"
        "- 1 2" assertSameResult "0"
        "* 2 3" assertSameResult "6"
        "/ 9 4" assertSameResult "2"
        "/ 9 0" assertSameResult "NUMB_ERROR"
        "EQUAL 2 2" assertSameResult "TRUE"
        "EQUAL 2 3" assertSameResult "FALSE"
    }


}