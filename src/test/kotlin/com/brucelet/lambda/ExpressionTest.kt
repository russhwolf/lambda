package com.brucelet.lambda

import org.junit.Assert.*
import org.junit.Test

class ExpressionTest {
    @Test fun strings() {
        assertEquals("A", Name("A").toString())
        assertEquals("λA.B", Function("A", "B").toString())
        assertEquals("(A B)", Application("A", "B").toString())
        assertEquals("λA.λB.λF.((F A) B)", Function("A", Function("B", Function("F", Application(Application("F", "A"), "B")))).toString())
    }

    @Test fun parseExpression() {
        assertEquals(Name("A"), "A".parseExpression())
        assertEquals(Function("A", "B"), "λA.B".parseExpression())
        assertEquals(Application("A", "B"), "(A B)".parseExpression())
        assertEquals(Function("A", Function("B", Function("F", Application(Application("F", "A"), "B")))), "λA.λB.λF.((F A) B)".parseExpression())
    }

    @Test fun substitute() {
        assertEquals(Name("B"), Name("A").substitute(from = "A", to = "B"))
        assertEquals(Function("A", "C"), Function("A", "B").substitute(from = "B", to = "C"))
        assertEquals(Function("C", "B"), Function("A", "B").substitute(from = "A", to = "C"))
        assertEquals(Application("C", "B"), Application("A", "B").substitute(from = "A", to = "C"))
        assertEquals(Application(Function("C", "D"), "B"), Application("A", "B").substitute(Name("A"), Function("C", "D")))
    }

    @Test fun isBound() {
        assertFalse("A" isBoundIn "A")
        assertTrue("A" isBoundIn Function("A", "A"))
        assertTrue("F" isBoundIn Function("F", Application("F", Function("X", "X"))))
        assertFalse("F" isBoundIn Application("F", Function("X", "X")))
    }

    @Test fun isFree() {
        assertTrue("A" isFreeIn "A")
        assertFalse("A" isFreeIn Function("A", "A"))
        assertFalse("F" isFreeIn Function("F", Application("F", Function("X", "X"))))
        assertTrue("F" isFreeIn Application("F", Function("X", "X")))
    }

    @Test fun reduce() {
        assertEquals(Name("B"), Application(Function("A", "A"), "B").reduceOnce())
    }
}
