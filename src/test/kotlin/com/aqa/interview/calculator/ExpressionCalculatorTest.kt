package com.aqa.interview.calculator

import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionCalculatorTest {

    private val calculator = calculator()

    @Test
    fun `addition of two integers`() {
        assertEquals("3", calculator.evaluate("1 + 2"))
    }
}
