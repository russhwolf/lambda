package com.brucelet.lambda

class FunctionProvider(namesToFunctions: Map<Name, Expression> = mapOf()) {
    private val namesToFunctions = namesToFunctions.toMutableMap()

    fun registerFunction(name: String, function: Expression) {
        namesToFunctions.put(Name(name), function)
    }

    fun Expression.replaceNamesWithFunctions(): Expression {
        var prev: Expression? = null
        var next = this
        while (next != prev) {
            prev = next
            next = namesToFunctions.keys.fold(prev) {
                expression, name ->
                expression.substitute(name, namesToFunctions.getValue(name))
            }
        }
        return next
    }

    fun Expression.replaceFunctionsWithNames(): Expression {
        var prev: Expression? = null
        var next = this
        while (next != prev) {
            prev = next
            next = namesToFunctions.values.fold(prev) {
                expression, function ->
                expression.substitute(function, namesToFunctions.keys.first { namesToFunctions[it] == function })
            }
        }
        return next
    }
}
