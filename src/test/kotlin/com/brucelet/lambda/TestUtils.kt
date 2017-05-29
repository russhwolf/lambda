package com.brucelet.lambda

import org.junit.Assert

// TODO auto-detect substitution instead of manual
fun assertParseAndReduceEquals(a: String, b: String) = Assert.assertEquals(a.parseAndReduce(), b.parseAndReduce())

fun assertParseAndReduceEquals(a: String, b: String, from: String, to: String)
        = Assert.assertEquals(a.parseAndReduce(), b.substitute(from, to).parseAndReduce())
