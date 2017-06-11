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
    parseLine("#def identity x = x")
    parseLine("#def self_apply s = s s")
    parseLine("#def apply func arg = func arg")
    parseLine("#def select_first first second = first")
    parseLine("#def select_second first second = second")
    parseLine("#def make_pair e1 e2 c = c e1 e2")
}