package com.brucelet.lambda

import org.junit.Test

class Chapter5Exercises : BaseParserTest() {
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
        #def PRED N = #if isnumb N #then (iszero (value N)) NUMB_ERROR (MAKE_NUMB ((value N) select_second)) #else NUMB_ERROR
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
        #def / X Y = #if both_numbs X Y #then (iszero (value Y)) NUMB_ERROR (MAKE_NUMB (div1 (value X) (value Y))) #else NUMB_ERROR
        #def EQUAL X Y = #if both_numbs X Y #then MAKE_BOOL (equal (value X) (value Y)) #else NUMB_ERROR
        """)
    }

//    #def NOT X = #if isbool X #then MAKE_BOOL (not (value X)) #else BOOL_ERROR
//    #def AND X Y = #if and (isbool X) (isbool Y) #then MAKE_BOOL (and (value X) (value Y)) #else BOOL_ERROR

//    #def NOT X = #IF X #THEN FALSE #ELSE TRUE
//    #def AND X Y = #IF ISBOOL Y #THEN COND Y FALSE X #ELSE BOOL_ERROR

    @Test fun `1`() {
        "ISBOOL 3" assertResult "FALSE"
        "ISNUMB FALSE" assertResult "FALSE"
        "NOT 1" assertResult "BOOL_ERROR"
        // TODO this fails
//        "TRUE AND 2" assertResult "BOOL_ERROR"
        "+ 2 TRUE" assertResult "NUMB_ERROR"
    }

    // TODO 2
}