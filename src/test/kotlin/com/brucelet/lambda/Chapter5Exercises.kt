package com.brucelet.lambda

import org.junit.Test

class Chapter5Exercises : BaseParserTest() {
    override fun Parser.initialize() {
        parseUntypedNumbers()
        parseTypedNumbers()
        parseTypedChars()
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

        #def succ = λn.λs.(s false n)
        #def iszero = λn.(n select_first)
        #def pred n = #if iszero n #then zero #else n select_second
        #def implies x y = #if x #then y #else true
        #def equiv x y = #if x #then y #else (not y)

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

        #def COND E1 E2 C = #if isbool C #then (#if value C #then E1 #else E2) #else BOOL_ERROR
        #def ISERROR E = MAKE_BOOL (iserror E)
        #def ISBOOL B = MAKE_BOOL (isbool B)

        #def numb_type = two
        #def MAKE_NUMB = λvalue.λs.(s numb_type value)
        #def NUMB_ERROR = λs.(s error_type numb_type)
        #def isnumb = istype numb_type
        #def ISNUMB N = MAKE_BOOL (isnumb N)
        #def SUCC N = #if isnumb N #then MAKE_NUMB (succ (value N)) #else NUMB_ERROR
        #def PRED N = #if isnumb N #then (#if iszero (value N) #then NUMB_ERROR #else MAKE_NUMB ((value N) select_second)) #else NUMB_ERROR
        #def ISZERO N = #if isnumb N #then MAKE_BOOL (iszero (value N)) #else NUMB_ERROR
        #def both_numbs X Y = and (isnumb X) (isnumb Y)
        #def + X Y = #if both_numbs X Y #then MAKE_NUMB (add (value X) (value Y)) #else NUMB_ERROR
        #def - X Y = #if both_numbs X Y #then MAKE_NUMB (sub (value X) (value Y)) #else NUMB_ERROR
        #def * X Y = #if both_numbs X Y #then MAKE_NUMB (mult (value X) (value Y)) #else NUMB_ERROR
        #def / X Y = #if both_numbs X Y #then (#if iszero (value Y) #then NUMB_ERROR #else MAKE_NUMB (div1 (value X) (value Y))) #else NUMB_ERROR
        #def EQUAL X Y = #if both_numbs X Y #then MAKE_BOOL (equal (value X) (value Y)) #else NUMB_ERROR
        #def GREATER X Y = #if both_numbs X Y #then MAKE_BOOL (greater (value X) (value Y)) #else NUMB_ERROR

        #def char_type = four
        #def MAKE_CHAR = λvalue.λs.(s char_type value)
        #def CHAR_ERROR = λs.(s error_type char_type)
        #def ischar = istype char_type
        #def ISCHAR C = MAKE_BOOL (ischar C)

        #def CHAR_LESS C1 C2 = #if and (ischar C1) (ischar C2) #then MAKE_BOOL (less (value C1) (value C2)) #else CHAR_ERROR
        #def ORD C = #if ischar C #then MAKE_NUMB (value C) #else CHAR_ERROR
        #def CHAR N = #if isnumb N #then MAKE_CHAR (value N) #else NUMB_ERROR
        #def CHAR_EQUAL C1 C2 = #if and (ischar C1) (ischar C2) #then MAKE_BOOL (equal (value C1) (value C2)) #else CHAR_ERROR
        """)
    }

    @Test fun `1`() {
        "ISBOOL 3" assertResult "FALSE"
        "ISNUMB FALSE" assertResult "FALSE"
        "NOT 1" assertResult "BOOL_ERROR"
        "AND TRUE 2" assertResult "BOOL_ERROR"
        "+ 2 TRUE" assertResult "NUMB_ERROR"
    }

    @Test fun `2i`() {
        with(parser) {
            parseLines("""
            #def signed_type = five
            #def SIGN_ERROR = MAKE_ERROR signed_type
            #def POS = TRUE
            #def NEG = FALSE
            #def MAKE_SIGNED N SIGN = make_obj signed_type (make_obj SIGN N)

            #def +0 = MAKE_SIGNED 0 POS
            #def -0 = MAKE_SIGNED 0 NEG
            #def +1 = MAKE_SIGNED 1 POS
            #def -1 = MAKE_SIGNED 1 NEG
            #def +2 = MAKE_SIGNED 2 POS
            #def -2 = MAKE_SIGNED 2 NEG
            #def +3 = MAKE_SIGNED 3 POS
            #def -3 = MAKE_SIGNED 3 NEG
            #def +4 = MAKE_SIGNED 4 POS
            #def -4 = MAKE_SIGNED 4 NEG
            #def +5 = MAKE_SIGNED 5 POS
            #def -5 = MAKE_SIGNED 5 NEG
            """)
        }

        parser.parseLine("#def issigned = istype signed_type")
        "issigned 0" assertSameResult "false"
        "issigned +0" assertSameResult "true"
        "issigned -0" assertSameResult "true"
        "issigned 2" assertSameResult "false"
        "issigned +2" assertSameResult "true"
        "issigned -2" assertSameResult "true"

        parser.parseLine("#def ISSIGNED N = MAKE_BOOL (issigned N)")
        "ISSIGNED 0" assertSameResult "FALSE"
        "ISSIGNED +0" assertSameResult "TRUE"
        "ISSIGNED -0" assertSameResult "TRUE"
        "ISSIGNED 2" assertSameResult "FALSE"
        "ISSIGNED +2" assertSameResult "TRUE"
        "ISSIGNED -2" assertSameResult "TRUE"

        parser.parseLine("#def sign N = type (value N)")
        "sign +0" assertSameResult "POS"
        "sign -0" assertSameResult "NEG"
        "sign +2" assertSameResult "POS"
        "sign -2" assertSameResult "NEG"

        parser.parseLine("#def SIGN N = #if issigned N #then sign N #else SIGN_ERROR")
        "SIGN +0" assertSameResult "POS"
        "SIGN -0" assertSameResult "NEG"
        "SIGN +2" assertSameResult "POS"
        "SIGN -2" assertSameResult "NEG"
        "SIGN 0" assertSameResult "SIGN_ERROR"
        "SIGN 2" assertSameResult "SIGN_ERROR"

        parser.parseLine("#def sign_value N = value (value N)")
        "sign_value +0" assertSameResult "0"
        "sign_value -0" assertSameResult "0"
        "sign_value +2" assertSameResult "2"
        "sign_value -2" assertSameResult "2"

        parser.parseLine("#def VALUE N = #if issigned N #then MAKE_SIGNED (sign_value N) POS #else SIGN_ERROR")
        "VALUE +0" assertSameResult "+0"
        "VALUE -0" assertSameResult "+0"
        "VALUE +2" assertSameResult "+2"
        "VALUE -2" assertSameResult "+2"
        "VALUE 0" assertSameResult "SIGN_ERROR"
        "VALUE 2" assertSameResult "SIGN_ERROR"

        parser.parseLine("#def sign_iszero N = value (EQUAL (sign_value N) 0)")
        "sign_iszero +0" assertSameResult "true"
        "sign_iszero -0" assertSameResult "true"
        "sign_iszero +2" assertSameResult "false"
        "sign_iszero -2" assertSameResult "false"
    }

    @Test fun `2ii`() {
        with(parser) {
            parseLines("""
            #def signed_type = five
            #def SIGN_ERROR = MAKE_ERROR signed_type
            #def POS = TRUE
            #def NEG = FALSE
            #def MAKE_SIGNED N SIGN = make_obj signed_type (make_obj SIGN N)

            #def +0 = MAKE_SIGNED 0 POS
            #def -0 = MAKE_SIGNED 0 NEG
            #def +1 = MAKE_SIGNED 1 POS
            #def -1 = MAKE_SIGNED 1 NEG
            #def +2 = MAKE_SIGNED 2 POS
            #def -2 = MAKE_SIGNED 2 NEG
            #def +3 = MAKE_SIGNED 3 POS
            #def -3 = MAKE_SIGNED 3 NEG
            #def +4 = MAKE_SIGNED 4 POS
            #def -4 = MAKE_SIGNED 4 NEG
            #def +5 = MAKE_SIGNED 5 POS
            #def -5 = MAKE_SIGNED 5 NEG

            #def issigned = istype signed_type
            #def ISSIGNED N = MAKE_BOOL (issigned N)
            #def sign N = type (value N)
            #def SIGN N = #if issigned N #then sign N #else SIGN_ERROR
            #def sign_value N = value (value N)
            #def VALUE N = #if issigned N #then sign_value N #else SIGN_ERROR
            #def sign_iszero N = value (EQUAL (sign_value N) 0)
            """)
        }

        parser.parseLine("#def SIGN_ISZERO N = #if issigned N #then MAKE_BOOL (sign_iszero N) #else SIGN_ERROR")
        "SIGN_ISZERO +0" assertSameResult "TRUE"
        "SIGN_ISZERO -0" assertSameResult "TRUE"
        "SIGN_ISZERO +2" assertSameResult "FALSE"
        "SIGN_ISZERO -2" assertSameResult "FALSE"
        "SIGN_ISZERO 0" assertSameResult "SIGN_ERROR"
        "SIGN_ISZERO 2" assertSameResult "SIGN_ERROR"

        parser.parseLine("#def SIGN_SUCC N = #IF SIGN_ISZERO N #THEN +1 #ELSE (#IF SIGN N #THEN MAKE_SIGNED (SUCC (sign_value N)) POS #ELSE MAKE_SIGNED (PRED (sign_value N)) NEG)")
        "SIGN_SUCC +0" assertSameResult "+1"
        "SIGN_SUCC -0" assertSameResult "+1"
        "SIGN_SUCC +2" assertSameResult "+3"
        "SIGN_SUCC -2" assertSameResult "-1"

        parser.parseLine("#def SIGN_PRED N = #IF SIGN_ISZERO N #THEN -1 #ELSE (#IF SIGN N #THEN MAKE_SIGNED (PRED (sign_value N)) POS #ELSE MAKE_SIGNED (SUCC (sign_value N)) NEG)")
        "SIGN_PRED +0" assertSameResult "-1"
        "SIGN_PRED -0" assertSameResult "-1"
        "SIGN_PRED +2" assertSameResult "+1"
        "SIGN_PRED -2" assertSameResult "-3"
    }

    @Test fun `2iii`() {
        with(parser) {
            parseLines("""
            #def signed_type = five
            #def SIGN_ERROR = MAKE_ERROR signed_type
            #def POS = TRUE
            #def NEG = FALSE
            #def MAKE_SIGNED N SIGN = make_obj signed_type (make_obj SIGN N)

            #def +0 = MAKE_SIGNED 0 POS
            #def -0 = MAKE_SIGNED 0 NEG
            #def +1 = MAKE_SIGNED 1 POS
            #def -1 = MAKE_SIGNED 1 NEG
            #def +2 = MAKE_SIGNED 2 POS
            #def -2 = MAKE_SIGNED 2 NEG
            #def +3 = MAKE_SIGNED 3 POS
            #def -3 = MAKE_SIGNED 3 NEG
            #def +4 = MAKE_SIGNED 4 POS
            #def -4 = MAKE_SIGNED 4 NEG
            #def +5 = MAKE_SIGNED 5 POS
            #def -5 = MAKE_SIGNED 5 NEG

            #def issigned = istype signed_type
            #def ISSIGNED N = MAKE_BOOL (issigned N)
            #def sign N = type (value N)
            #def SIGN N = #if issigned N #then sign N #else SIGN_ERROR
            #def sign_value N = value (value N)
            #def VALUE N = #if issigned N #then sign_value N #else SIGN_ERROR
            #def sign_iszero N = value (EQUAL (sign_value N) 0)

            #def SIGN_ISZERO N = #if issigned N #then MAKE_BOOL (sign_iszero N) #else SIGN_ERROR
            #def SIGN_SUCC N = #IF SIGN_ISZERO N #THEN +1 #ELSE (#IF SIGN N #THEN MAKE_SIGNED (SUCC (sign_value N)) POS #ELSE MAKE_SIGNED (PRED (sign_value N)) NEG)
            #def SIGN_PRED N = #IF SIGN_ISZERO N #THEN -1 #ELSE (#IF SIGN N #THEN MAKE_SIGNED (PRED (sign_value N)) POS #ELSE MAKE_SIGNED (SUCC (sign_value N)) NEG)
            """)
        }

        parser.parseLine("#def SIGN_+ X Y = " +
                "#if and (issigned X) (issigned Y) " +
                "#then (" +
                 "#if iszero (value (sign_value X)) " +
                 "#then Y " +
                 "#else (" +
                  "#if iszero (value (sign_value Y)) " +
                  "#then X " +
                  "#else (" +
                   "#if and (value (sign X)) (value (sign Y)) " +
                   "#then MAKE_SIGNED (+ (sign_value X) (sign_value Y)) POS " +
                   "#else (" +
                    "#if not (or (value (sign X)) (value (sign Y))) " +
                    "#then MAKE_SIGNED (+ (sign_value X) (sign_value Y)) NEG " +
                    "#else (" +
                     "#if value (sign X) " +
                     "#then (" +
                      "#if greater (value (sign_value X)) (value (sign_value Y)) " +
                      "#then MAKE_SIGNED (- (sign_value X) (sign_value Y)) POS " +
                      "#else MAKE_SIGNED (- (sign_value Y) (sign_value X)) NEG" +
                     ") " +
                     "#else (" +
                      "#if greater (value (sign_value X)) (value (sign_value Y)) " +
                      "#then MAKE_SIGNED (- (sign_value X) (sign_value Y)) NEG " +
                      "#else MAKE_SIGNED (- (sign_value Y) (sign_value X)) POS" +
                     ")" +
                "))))) " +
                "#else SIGN_ERROR")


        "SIGN_+ +1 0" assertSameResult "SIGN_ERROR"
        "SIGN_+ false -2" assertSameResult "SIGN_ERROR"
        "SIGN_+ +0 -2" assertSameResult "-2"
        "SIGN_+ +1 -0" assertSameResult "+1"
        "SIGN_+ +1 +2" assertSameResult "+3"
        "SIGN_+ -1 -2" assertSameResult "-3"
        "SIGN_+ +2 -1" assertSameResult "+1"
        "SIGN_+ +1 -2" assertSameResult "-1"
        "SIGN_+ -2 +1" assertSameResult "-1"
        "SIGN_+ -1 +2" assertSameResult "+1"
    }
}