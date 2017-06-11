package com.brucelet.lambda

fun main(vararg args: kotlin.String) {
    val functionProvider = FunctionProvider(mapOf(
            "identity" to "λx.x",
            "self_apply" to "λs.(s s)",
            "apply" to "λfunc.λarg.(func arg)",
            "select_first" to "λfirst.λsecond.first",
            "select_second" to "λfirst.λsecond.second",
            "make_pair" to "λfirst.λsecond.λfunc.((func first) second)"
    ))

    val lines: List<String> = if (args.isEmpty()) {
        val lines = mutableListOf<String>()
        println("insert commands (blank to finish)")
        while (true) {
            val line = readLine()
            if (line.isNullOrEmpty()) break
            lines.add(line ?: break)
        }
        lines
    } else {
        args.asList()
    }

    with(functionProvider) {
        for (line in lines) {
            try {
                println(line.replaceNamesWithFunctions().parseAndReduce().toString().replaceFunctionsWithNames())
            } catch (e: IllegalArgumentException) {
                System.err.println(e.message)
            }
        }
    }
}