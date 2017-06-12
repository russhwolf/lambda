package com.brucelet.lambda

class Parser(val output: (String) -> Unit = ::println) {
    private val functionProvider = FunctionProvider()

    fun parseLine(line: String) = with(functionProvider) {
        if (line.startsWith("#")) {
            line.parseKeywords()
        } else {
            output(line.parseExpression().replaceNamesWithFunctions().reduce().replaceFunctionsWithNames().toString())
        }
    }

    fun parseLines(lines: String) = lines.trimIndent().split("\n").forEach { parseLine(it) }

    private fun String.parseKeywords() {
        val stop = { message: String -> throw IllegalArgumentException("$message in '$this'") }

        if (startsWith("#def ")) {
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

            val equalsIndex = tokens.indexOf("=")
            if (equalsIndex < 0) stop("Missing '='")
            val name = tokens[1]
            val params = (2..equalsIndex - 1).map { tokens[it] }.fold("") { params, a -> params + "λ$a." }
            val body = (equalsIndex + 1..tokens.lastIndex).map { tokens[it] }.reduce { a, b -> "$a $b" }
            val function = if (equalsIndex + 1 == tokens.lastIndex) "$params$body" else "$params($body)"
            functionProvider.registerFunction(name, function.parseExpression())
        } else {
            stop("Invalid keyword")
        }
    }
}