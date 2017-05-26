package com.brucelet.lambda

sealed class Expression {
    abstract fun substitute(from: Name, to: Expression): Expression
    fun substitute(from: String, to: String) = substitute(Name(from), Name(to))
    fun substituteIfFree(from: Name, to: Expression): Expression
            = if (from isFreeIn this) substitute(from, to) else this

    fun substituteIfFree(from: String, to: Expression): Expression = substituteIfFree(Name(from), to)

    abstract fun reduceOnce(): Expression
    fun reduce(): Expression {
        var value: Expression = this
        var nextValue: Expression = reduceOnce()
        while (value != nextValue) {
            value = nextValue
            nextValue = value.reduceOnce()
        }
        return value
    }
}

private class InvalidSyntaxException(string: String) : IllegalArgumentException("Invalid expression: $string")

data class Name(private val label: String) : Expression() {
    override fun substitute(from: Name, to: Expression) = if (this == from) to else this
    override fun reduceOnce() = this

    override fun toString() = label

    infix fun isFreeIn(expression: Expression): Boolean = when (expression) {
        is Name -> this == expression
        is Function -> this != expression.name && this isFreeIn expression.body
        is Application -> this isFreeIn expression.function || this isFreeIn expression.argument
    }

    infix fun isFreeIn(name: String) = this isFreeIn Name(name)
}

data class Function(val name: Name, val body: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Name, to: Expression) = Function(name, body.substitute(from, to))
    override fun reduceOnce(): Expression = Function(name, body.reduceOnce())

    override fun toString() = "λ$name.$body"
}

data class Application(val function: Expression, val argument: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: Expression, body: String) : this(name, Name(body))
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Name, to: Expression) = Application(function.substitute(from, to), argument.substitute(from, to))
    override fun reduceOnce(): Expression = when (function) {
        is Name -> Application(function, argument.reduceOnce())
        is Function -> function.body.substituteIfFree(from = function.name, to = argument)
        is Application -> Application(function.reduceOnce(), argument)
    }

    override fun toString() = "($function $argument)"
}

fun String.parseExpression(): Expression {
    val stop = { throw InvalidSyntaxException(this) }

    fun String.parseName(): Name {
        // Make sure we have no illegal characters anywhere
        if (contains(Regex.fromLiteral("[ ()λ.]"))) stop()
        return Name(this)
    }

    fun String.parseFunction(): Function {
        // Find first . and split
        val dotIndex = indexOf('.')
        if (dotIndex > lastIndex - 1) stop()
        return Function(substring(1, dotIndex).parseName(), substring(dotIndex + 1).parseExpression())
    }

    fun String.parseApplication(): Application {
        // Make sure outer chars are parens
        if (last() != ')') {
            stop()
        }
        // Find a space at a valid paren depth
        var depth = 0
        var spaceIndex: Int = -1
        forEachIndexed { index, c ->
            when (c) {
                ' ' -> if (depth == 1) {
                    if (spaceIndex == -1) spaceIndex = index else stop()
                }
                '(' -> depth++
                ')' -> depth--
            }
            if (depth < 1 && index < lastIndex) stop()
        }
        if (spaceIndex == length) stop()
        val function = substring(1, spaceIndex)
        val argument = substring(spaceIndex + 1, lastIndex)
        return Application(function.parseExpression(), argument.parseExpression())
    }

    if (isEmpty()) stop()
    return when (first()) {
        '(' -> parseApplication()
        'λ' -> parseFunction()
        else -> parseName()
    }
}

fun String.parseAndReduce() = parseExpression().reduce()

infix fun String.isFreeIn(expression: Expression) = Name(this) isFreeIn expression
infix fun String.isFreeIn(name: String) = Name(this) isFreeIn name


