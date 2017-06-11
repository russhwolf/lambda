package com.brucelet.lambda

class FunctionProvider(namesToFunctions: Map<String, String> = mapOf()) {
    private val namesToFunctions = namesToFunctions.toMutableMap()
    private val functionsToNames = namesToFunctions.toList().map { Pair(it.second, it.first) }.toMap().toMutableMap()

    fun registerFunction(name: String, function: String) {
        namesToFunctions.put(name, function)
        functionsToNames.put(function, name)
    }

    fun registerProvider(provider: FunctionProvider) = provider.namesToFunctions.keys.forEach {
        registerFunction(it, provider.namesToFunctions.getValue(it))
    }

    fun String.replaceNamesWithFunctions(): String = namesToFunctions.keys.fold(this) {
        expression, name ->
        expression.replace(name, namesToFunctions.getValue(name))
    }

    fun String.replaceFunctionsWithNames(): String = functionsToNames.keys.fold(this) {
        expression, function ->
        expression.replace(function, functionsToNames.getValue(function))
    }
}
