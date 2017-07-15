package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberGeneratorTest {
    @Test fun makeNumberStringTest() {
        assertEquals("two", makeNumberString(2))
        assertEquals("fifteen", makeNumberString(15))
        assertEquals("thirty", makeNumberString(30))
        assertEquals("twenty_three", makeNumberString(23))
        assertEquals("two_hundred", makeNumberString(200))
        assertEquals("three_hundred_and_fourteen", makeNumberString(314))
        assertEquals("six_hundred_and_twenty_eight", makeNumberString(628))
    }
}