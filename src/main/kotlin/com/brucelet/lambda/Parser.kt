package com.brucelet.lambda

class Parser(val output: (String) -> Unit = ::println) {
    private val functionProvider = FunctionProvider()

    fun parseLine(line: String) = with(functionProvider) {
        if (line.isNullOrBlank()) {
            return
        }

        val trimmedLine = line
                .replace(Regex("^\\s+"), "")
                .replace(Regex("\\s+"), " ")
        if (trimmedLine.contains("#")) {
            trimmedLine.parseKeywords()
        } else {
            output(trimmedLine.parseExpression().replaceNamesWithFunctions().reduce().replaceFunctionsWithNames().toString())
        }
    }

    fun parseLines(lines: String) = lines.split("\n").forEach { parseLine(it) }

    private fun String.parseKeywords(): Unit = with(tokenize()) {
        val stop = { message: String -> throw IllegalArgumentException("$message in '$this'") }

        if (contains("#if") || contains("#then") || contains("#else")) {
            if (!(contains("#if") && contains("#then") && contains("#else"))) {
                stop("Incomplete conditional expression")
            }
            val ifIndex = indexOf("#if")
            val thenIndex = indexOf("#then")
            val elseIndex = indexOf("#else")
            if (ifIndex > thenIndex || thenIndex > elseIndex || ifIndex + 1 == thenIndex || thenIndex + 1 == elseIndex || elseIndex == lastIndex) {
                stop("Invalid conditional expression")
            }
            val start = subList(0, ifIndex).joinToString(" ")
            val ifBody = rejoin(ifIndex + 1, thenIndex)
            val thenBody = rejoin(thenIndex + 1, elseIndex)
            val elseBody = rejoin(elseIndex + 1, size)
//            val conditional = "$start cond $thenBody $elseBody $ifBody"
            val conditional = "$start $ifBody $thenBody $elseBody"
            parseLine(conditional)
        } else if (startsWith("#def") || startsWith("#rec")) {
            val equalsIndex = indexOf("=")
            if (equalsIndex < 0) stop("Missing '='")
            val name = get(1)
            val params = if (equalsIndex <= 2) "" else subList(2, equalsIndex).joinToString(separator = ".λ", prefix = "λ", postfix = ".")
            val body = rejoin(equalsIndex + 1, size)
            if (startsWith("#def")) {
                val function = "$params$body"
                functionProvider.registerFunction(name, function.parseExpression())
            } else if (startsWith("#rec")) {
                val recursiveBody = body.parseExpression().substitute(name, "f").toString()
                val recursiveDef = "#def $name = recursive λf.$params$recursiveBody"
                recursiveDef.parseKeywords()
            }
        } else {
            stop("Invalid keyword")
        }
    }

    private fun String.tokenize(): List<String> {
        val tokens = mutableListOf<String>()
        var currentToken = ""
        var depth = 0
        for (c in toCharArray()) {
            when (c) {
                ' ' -> if (depth == 0) {
                    tokens += currentToken
                    currentToken = ""
                }
                '(' -> depth++
                ')' -> depth--
            }
            if (!(depth == 0 && c == ' ')) {
                currentToken += c
            }
        }
        tokens += (currentToken)
        return tokens
    }

    private fun List<String>.rejoin(startIndex: Int = 0, endIndex: Int = size): String =
            subList(startIndex, endIndex).joinToString(" ").let { if (startIndex + 1 == endIndex) it else "($it)" }
}