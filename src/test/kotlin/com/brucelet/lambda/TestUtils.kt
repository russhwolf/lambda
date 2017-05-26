package com.brucelet.lambda

import org.junit.Assert

fun assertParseAndReduceEquals(a: String, b: String) = Assert.assertEquals(a.parseAndReduce(), b.parseAndReduce())
