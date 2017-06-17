package com.brucelet.lambda

sealed class Expression {
    abstract fun substitute(from: Expression, to: Expression): Expression
    fun substitute(from: String, to: String) = substitute(Name(from), Name(to))

    abstract fun canReduce(): Boolean
    abstract fun reduceOnce(): Expression

    fun reduce(): Expression {
        var value: Expression = this
        while (value.canReduce()) {
            val nextValue = value.reduceOnce()
            if (nextValue == value) {
                break
            }
            value = nextValue
        }
        return value
    }
}

data class Name(private val label: String) : Expression() {
    override fun substitute(from: Expression, to: Expression): Expression = if (this == from) to else this

    override fun reduceOnce(): Expression = this
    override fun canReduce(): Boolean = false

    override fun toString() = label
}

data class Function(val name: Name, val body: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Expression, to: Expression) = when {
        this == from -> to
        name == from && to is Name -> Function(to, body.substitute(from, to))
        name != from -> Function(name, body.substitute(from, to))
        else -> this
    }

    override fun reduceOnce(): Expression = Function(name, body.reduceOnce())
    override fun canReduce(): Boolean = body.canReduce()

    override fun toString() = "λ$name.$body"
}

data class Application(val function: Expression, val argument: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: Expression, body: String) : this(name, Name(body))
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Expression, to: Expression) = when (from) {
        this -> to
        else -> Application(function.substitute(from, to), argument.substitute(from, to))
    }

    override fun reduceOnce(): Expression = when {
        function is Function -> function.body.substitute(function.name, argument)
        function.canReduce() -> Application(function.reduceOnce(), argument)
        argument.canReduce() -> Application(function, argument.reduceOnce())
        else -> this
    }

    override fun canReduce(): Boolean {
        return function.canReduce() || argument.canReduce() || function is Function
    }

    override fun toString() = "($function $argument)"
}

fun String.parseExpression(): Expression {
    val stop = { message: String -> throw IllegalArgumentException("$message in '$this'") }

    fun String.insertParentheses(): String {
        var out = this

        // Recurse into inner parens and add any missing
        val opens = mutableListOf<Int>()
        val closes = mutableListOf<Int>()
        var depth = 0
        for ((i, c) in out.withIndex()) {
            when (c) {
                '(' -> {
                    if (depth == 0) opens += i
                    depth++
                }
                ')' -> {
                    depth--
                    if (depth == 0) {
                        if (opens.size != closes.size + 1) stop("Unbalanced parentheses")
                        closes += i
                    }
                }
            }
        }
        if (depth != 0 || opens.size != closes.size) stop("Unbalanced parentheses")
        opens.indices.reversed().forEach {
            out = "${out.substring(0, opens[it])}${out.substring(opens[it] + 1, closes[it]).insertParentheses()}${out.substring(closes[it] + 1, out.length)}"
        }

        // Count top level spaces, and add missing parens around them
        val spaces = mutableListOf<Int>()
        for ((i, c) in out.withIndex()) {
            when (c) {
                ' ' -> if (depth == 0) spaces += i
                '(' -> depth++
                ')' -> depth--
            }
        }

        if (!spaces.isEmpty()) {
            spaces.removeAt(0)
            spaces.indices.forEach {
                out = "(${out.substring(0, spaces[it] + 2 * it)})${out.substring(spaces[it] + 2 * it, out.length)}"
            }
            out = "($out)"
        }
        return out
    }

    fun String.parseName(): Name {
        // Make sure we have no illegal characters anywhere
        toCharArray().forEach { if (!(it.isLetterOrDigit() || it == '_')) stop("Illegal character '$it'") }
        return Name(this)
    }

    fun String.parseFunction(): Function {
        // Find first . and split
        val dotIndex = indexOf('.')
        if (dotIndex > lastIndex - 1) stop("Illegal '.'")
        return Function(substring(1, dotIndex).parseName(), substring(dotIndex + 1).parseExpression())
    }

    fun String.parseApplication(): Application {
        // Make sure outer chars are parens
        if (last() != ')') {
            stop("Missing ')'")
        }
        // Find a space at a valid paren depth
        var depth = 0
        var spaceIndex: Int = -1
        forEachIndexed { index, c ->
            when (c) {
                ' ' -> if (depth == 1) {
                    if (spaceIndex == -1) spaceIndex = index else stop("Illegal space")
                }
                '(' -> depth++
                ')' -> depth--
            }
            if (depth < 1 && index < lastIndex) stop("Illegal character after final ')'")
        }
        if (spaceIndex == length) stop("Illegal final space")
        val function = substring(1, spaceIndex)
        val argument = substring(spaceIndex + 1, lastIndex)
        return Application(function.parseExpression(), argument.parseExpression())
    }

    if (isEmpty()) stop("Empty string")
    with(insertParentheses()) {
        return when (first()) {
            '(' -> parseApplication()
            'λ', '^' -> parseFunction()
            else -> parseName()
        }
    }
}


