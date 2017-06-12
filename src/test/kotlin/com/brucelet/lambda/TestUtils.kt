package com.brucelet.lambda

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals

infix fun <T> T.assertEquals(value: T): Unit = assertEquals(value, this)
infix fun <T> T.assertNotEquals(value: T): Unit = assertNotEquals(value, this)
