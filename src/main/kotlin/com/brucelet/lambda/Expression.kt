package com.brucelet.lambda

sealed class Expression {
    abstract fun substitute(from: Name, to: Expression): Expression
    fun substitute(from: String, to: String) = substitute(Name(from), Name(to))
    fun substituteIfFree(from: Name, to: Expression): Expression
            = if (from isFreeIn this) substitute(from, to) else this

    fun substituteIfFree(from: String, to: Expression): Expression = substituteIfFree(Name(from), to)

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

private class InvalidSyntaxException(string: String) : IllegalArgumentException("Invalid expression: $string")

data class Name(private val label: String) : Expression() {
    override fun substitute(from: Name, to: Expression) = if (this == from) to else this
    override fun reduceOnce() = this
    override fun canReduce() = false

    override fun toString() = label

    infix fun isBoundIn(expression: Expression): Boolean = when (expression) {
        is Name -> false
        is Function -> this == expression.name || this isBoundIn expression.body
        is Application -> this isBoundIn expression.function || this isBoundIn expression.argument
    }

    infix fun isFreeIn(expression: Expression): Boolean = when (expression) {
        is Name -> this == expression
        is Function -> this != expression.name && this isFreeIn expression.body
        is Application -> this isFreeIn expression.function || this isFreeIn expression.argument
    }

    infix fun isFreeIn(name: String) = this isFreeIn Name(name)
    infix fun isBoundIn(name: String) = this isBoundIn Name(name)
}

data class Function(val name: Name, val body: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Name, to: Expression) = if (name == from && to is Name) {
        Function(to, body.substitute(from, to))
    } else if (name != from) {
        Function(name, body.substitute(from, to))
    } else {
        this
    }
    override fun reduceOnce(): Expression = Function(name, body.reduceOnce())
    override fun canReduce(): Boolean = body.canReduce()

    override fun toString() = "λ$name.$body"
}

data class Application(val function: Expression, val argument: Expression) : Expression() {
    constructor(name: String, body: Expression) : this(Name(name), body)
    constructor(name: Expression, body: String) : this(name, Name(body))
    constructor(name: String, body: String) : this(Name(name), Name(body))

    override fun substitute(from: Name, to: Expression) = Application(function.substitute(from, to), argument.substitute(from, to))
    override fun reduceOnce(): Expression = if (argument.canReduce()) {
        Application(function, argument.reduceOnce())
    } else if (function.canReduce()) {
        Application(function.reduceOnce(), argument)
    } else when (function) {
        is Function -> function.body.substitute(from = function.name, to = argument)
        else -> this
    }

    override fun canReduce(): Boolean {
        return function.canReduce() || argument.canReduce() || function is Function
    }

    override fun toString() = "($function $argument)"
}

fun String.parseExpression(): Expression {
    val stop = { throw InvalidSyntaxException(this) }

    fun String.parseName(): Name {
        // Make sure we have no illegal characters anywhere
        if (contains('(') || contains('(') || contains(' ') || contains('.') || contains('λ')) stop()
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
fun String.substitute(from: String, to: String) = parseExpression().substitute(from, to).toString()

infix fun String.isFreeIn(expression: Expression) = Name(this) isFreeIn expression
infix fun String.isFreeIn(name: String) = Name(this) isFreeIn name
infix fun String.isBoundIn(expression: Expression) = Name(this) isBoundIn expression
infix fun String.isBoundIn(name: String) = Name(this) isBoundIn name


