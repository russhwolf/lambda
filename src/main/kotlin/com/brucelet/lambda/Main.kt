package com.brucelet.lambda

fun main(vararg args: kotlin.String) {
    val lines: List<String> = parseInput(args)
    val parser = generateParser()

    for (line in lines) {
        try {
            parser.parseLine(line)
        } catch (e: IllegalArgumentException) {
            System.err.println(e.message)
        }
    }
}

private fun parseInput(args: Array<out String>): List<String> = when {
    args.isEmpty() -> {
        val lines = mutableListOf<String>()
        println("insert commands (blank to finish)")
        while (true) {
            val line = readLine()
            if (line.isNullOrEmpty()) break
            lines.add(line ?: break)
        }
        lines
    }
    else -> args.asList()
}

private fun generateParser(): Parser = Parser().apply {
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