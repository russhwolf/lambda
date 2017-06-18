package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionTest {
    @Test fun strings() {
        assertEquals("A", Name("A").toString())
        assertEquals("λA.B", Function("A", "B").toString())
        assertEquals("(A B)", Application("A", "B").toString())
        assertEquals("λA.λB.λF.((F A) B)", Function("A", Function("B", Function("F", Application(Application("F", "A"), "B")))).toString())
        assertEquals("(A (((B C) ((D E) F)) G))", Application("A", Application(Application(Application("B", "C"), Application(Application("D", "E"), "F")), "G")).toString())
    }

    @Test fun parseExpression() {
        fun String.assertParsesTo(expression: Expression) = assertEquals(expression, parseExpression())
        "A".assertParsesTo(Name("A"))
        "λA.B".assertParsesTo(Function("A", "B"))
        "^A.B".assertParsesTo(Function("A", "B"))
        "(A B)".assertParsesTo(Application("A", "B"))
        "λA.λB.λF.((F A) B)".assertParsesTo(Function("A", Function("B", Function("F", Application(Application("F", "A"), "B")))))
        "A B C D".assertParsesTo(Application(Application(Application("A", "B"), "C"), "D"))
        "A (B C)".assertParsesTo(Application("A", Application("B", "C")))
        "(A B C)".assertParsesTo(Application(Application("A", "B"), "C"))
        "A (B C D)".assertParsesTo(Application("A", Application(Application("B", "C"), "D")))
        "(A (B C D))".assertParsesTo(Application("A", Application(Application("B", "C"), "D")))
        "λA.(B C D)".assertParsesTo(Function("A", Application(Application("B", "C"), "D")))
        "λA.B C D".assertParsesTo(Application(Application(Function("A", "B"), "C"), "D"))
        "A (B C D) (E F G)".assertParsesTo(Application(Application("A", Application(Application("B", "C"), "D")), Application(Application("E", "F"), "G")))
        "A (B C (D E F) G)".assertParsesTo(Application("A", Application(Application(Application("B", "C"), Application(Application("D", "E"), "F")), "G")))
    }

    @Test fun substitute() {
        assertEquals(Name("B"), Name("A").substitute(from = "A", to = "B"))
        assertEquals(Function("A", "C"), Function("A", "B").substitute(from = "B", to = "C"))
        assertEquals(Function("A", "B"), Function("A", "B").substitute(from = "A", to = "C"))
        assertEquals(Application("C", "B"), Application("A", "B").substitute(from = "A", to = "C"))
        assertEquals(Application(Function("C", "D"), "B"), Application("A", "B").substitute(Name("A"), Function("C", "D")))
        assertEquals(Application("A", "B"), Application(Function("C", "D"), "B").substitute(Function("C", "D"), Name("A")))
    }

    @Test fun reduce() {
        assertEquals(Name("B"), Application(Function("A", "A"), "B").reduceOnce())
    }
}
