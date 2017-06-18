package com.brucelet.lambda

import org.junit.After
import org.junit.Before

open class BaseParserTest {
    lateinit var parser: Parser

    val outputLines = mutableListOf<String>()

    @Before fun setupParser() {
        parser = Parser { outputLines.add(it) }
        parser.initialize()
    }

    @After fun tearDownParser() {
        outputLines.clear()
    }

    open fun Parser.initialize() {}

    infix fun String.assertResult(that: String) {
        parser.parseLine(this)
        outputLines.last() assertEquals that
    }

    infix fun String.assertSameResult(that: String) {
        parser.parseLine(this)
        parser.parseLine(that)
        outputLines[outputLines.lastIndex - 1] assertEquals outputLines.last()
    }

    infix fun String.assertNotSameResult(that: String) {
        parser.parseLine(this)
        parser.parseLine(that)
        outputLines[outputLines.lastIndex - 1] assertNotEquals outputLines.last()
    }
}