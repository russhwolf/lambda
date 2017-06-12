package com.brucelet.lambda

import org.junit.Test

class ParserTest : BaseParserTest() {

    override fun Parser.initialize() {
        parseLines("""
        #def identity x = x
        #def self_apply s = s s
        """)
    }

    @Test fun defineAndParse() {
        "identity a" assertResult "a"
        "self_apply a" assertResult "(a a)"
        outputLines.size.assertEquals(2)
    }
}
