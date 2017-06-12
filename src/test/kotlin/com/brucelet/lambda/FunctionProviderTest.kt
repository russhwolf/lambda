package com.brucelet.lambda

import org.junit.Before
import org.junit.Test

class FunctionProviderTest {
    lateinit var functionProvider: FunctionProvider

    @Before fun setupProvider() {
        functionProvider = FunctionProvider(mapOf(
                Name("identity") to "λx.x".parseExpression(),
                Name("self_apply") to "λs.(s s)".parseExpression()
        ))
    }

    @Test fun replaceNamesWithFunctions() {
        with(functionProvider) {
            "identity".parseExpression().replaceNamesWithFunctions().toString() assertEquals "λx.x"
        }
    }

    @Test fun replaceFunctionsWithNames() {
        with(functionProvider) {
            "λx.x".parseExpression().replaceFunctionsWithNames().toString() assertEquals "identity"
        }
    }

    @Test fun registersNewFunctions() {
        val newExpression = "λfunc.λarg.(func arg)".parseExpression()
        val newName = "apply"
        with(functionProvider) {
            newName.parseExpression().replaceNamesWithFunctions() assertEquals newName.parseExpression()
            registerFunction(newName, newExpression)
            newName.parseExpression().replaceNamesWithFunctions() assertEquals newExpression
            newExpression.replaceFunctionsWithNames() assertEquals newName.parseExpression()
        }
    }

    @Test fun replaceParseAndReduce() {
        val expression = "(identity self_apply)"
        with(functionProvider) {
            expression.parseExpression().replaceNamesWithFunctions().reduce().replaceFunctionsWithNames().toString() assertEquals "self_apply"
        }
    }
}