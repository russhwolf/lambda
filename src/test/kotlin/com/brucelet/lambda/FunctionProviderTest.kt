package com.brucelet.lambda

import org.junit.Before
import org.junit.Test

class FunctionProviderTest {
    lateinit var functionProvider: FunctionProvider

    @Before fun setupProvider() {
        functionProvider = FunctionProvider(mapOf(
                "identity" to "λx.x",
                "self_apply" to "λs.(s s)"
        ))
    }

    @Test fun replaceNamesWithFunctions() {
        with(functionProvider) {
            "identity".replaceNamesWithFunctions().assertEquals("λx.x")
        }
    }

    @Test fun replaceFunctionsWithNames() {
        with(functionProvider) {
            "λx.x".replaceFunctionsWithNames().assertEquals("identity")
        }
    }

    @Test fun registersNewFunctions() {
        val newExpression = "λfunc.λarg.(func arg)"
        val newName = "apply"
        with(functionProvider) {
            newName.replaceNamesWithFunctions().assertEquals(newName)
            registerFunction(newName, newExpression)
            newName.replaceNamesWithFunctions().assertEquals(newExpression)
            newExpression.replaceFunctionsWithNames().assertEquals(newName)
        }
    }

    @Test fun replaceParseAndReduce() {
        val expression = "(identity self_apply)"
        with(functionProvider) {
            expression.replaceNamesWithFunctions().parseAndReduce().toString().replaceFunctionsWithNames().assertEquals("self_apply")
        }
    }
}