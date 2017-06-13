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