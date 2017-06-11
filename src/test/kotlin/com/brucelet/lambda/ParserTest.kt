package com.brucelet.lambda

import org.junit.Before
import org.junit.Test

class ParserTest {
    lateinit var parser: Parser

    val outputLines = mutableListOf<String>()

    @Before fun setupParser() {
        outputLines.clear()
        parser = Parser { outputLines.add(it) }
    }

    @Test fun defineAndParse() {
        parser.parseLines("""
                #def identity x = x
                #def self_apply s = s s
                identity a
                self_apply a
                """.trimIndent())

        outputLines.size.assertEquals(2)
        outputLines[0].assertEquals("a")
        outputLines[1].assertEquals("(a a)")
    }
}
