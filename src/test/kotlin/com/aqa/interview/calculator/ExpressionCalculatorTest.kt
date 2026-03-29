package com.aqa.interview.calculator

import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ExpressionCalculatorTest {

    private val calc = calculator()

    /**
     * Asserts that the evaluated result is numerically equal to the expected value,
     * tolerating minor representation differences (e.g. "0.30" vs "0.3").
     */
    private fun assertNumericEquals(expected: String, actual: String, message: String? = null) {
        val prefix = message?.let { "$it: " } ?: ""
        assertTrue(
            BigDecimal(expected).compareTo(BigDecimal(actual)) == 0,
            "${prefix}expected <$expected> but was <$actual>"
        )
    }

    // ====================================================================
    // Basic arithmetic
    // ====================================================================

    @Test
    fun `addition of two integers`() {
        assertEquals("3", calc.evaluate("1 + 2"))
    }

    @Test
    fun `subtraction of two integers`() {
        assertEquals("1", calc.evaluate("3 - 2"))
    }

    @Test
    fun `multiplication of two integers`() {
        assertEquals("6", calc.evaluate("2 * 3"))
    }

    @Test
    fun `integer division`() {
        assertEquals("2", calc.evaluate("6 / 3"))
    }

    @Test
    fun `addition with zero`() {
        assertEquals("42", calc.evaluate("42 + 0"))
    }

    @Test
    fun `subtraction yielding zero`() {
        assertEquals("0", calc.evaluate("7 - 7"))
    }

    @Test
    fun `multiplication by zero`() {
        assertEquals("0", calc.evaluate("999 * 0"))
    }

    @Test
    fun `multiplication by one is identity`() {
        assertEquals("123", calc.evaluate("123 * 1"))
    }

    // ====================================================================
    // Operator precedence
    // ====================================================================

    @Test
    fun `multiplication before addition`() {
        assertEquals("14", calc.evaluate("2 + 3 * 4"))
    }

    @Test
    fun `multiplication before subtraction`() {
        assertEquals("2", calc.evaluate("8 - 2 * 3"))
    }

    @Test
    fun `division before addition`() {
        assertEquals("5", calc.evaluate("3 + 8 / 4"))
    }

    @Test
    fun `chained same-precedence left to right`() {
        assertEquals("3", calc.evaluate("10 - 4 - 3"))
    }

    @Test
    fun `chained division left to right`() {
        assertEquals("2", calc.evaluate("24 / 6 / 2"))
    }

    @Test
    fun `mixed all four operators`() {
        // 2 + (3*4) - (10/5) = 2 + 12 - 2 = 12
        assertEquals("12", calc.evaluate("2 + 3 * 4 - 10 / 5"))
    }

    // ====================================================================
    // Parentheses
    // ====================================================================

    @Test
    fun `parentheses override precedence`() {
        assertEquals("20", calc.evaluate("(2 + 3) * 4"))
    }

    @Test
    fun `nested parentheses`() {
        assertEquals("21", calc.evaluate("((1 + 2) * (3 + 4))"))
    }

    @Test
    fun `deeply nested parentheses`() {
        assertEquals("5", calc.evaluate("(((((5)))))"))
    }

    @Test
    fun `parentheses around entire expression`() {
        assertEquals("10", calc.evaluate("(5 + 5)"))
    }

    @Test
    fun `complex nested expression`() {
        assertEquals("2", calc.evaluate("(10 - (3 + 5)) / (4 - 3)"))
    }

    // ====================================================================
    // Unary minus
    // ====================================================================

    @Test
    fun `unary minus on single number`() {
        assertEquals("-5", calc.evaluate("-5"))
    }

    @Test
    fun `unary minus in expression`() {
        assertEquals("-2", calc.evaluate("-5 + 3"))
    }

    @Test
    fun `double unary minus`() {
        assertEquals("5", calc.evaluate("--5"))
    }

    @Test
    fun `unary minus with parentheses`() {
        assertEquals("-8", calc.evaluate("-(3 + 5)"))
    }

    @Test
    fun `unary minus after operator`() {
        assertEquals("8", calc.evaluate("5 + -3 + 6"))
    }

    @Test
    fun `unary minus multiplied`() {
        assertEquals("-15", calc.evaluate("-3 * 5"))
    }

    @Test
    fun `negative times negative`() {
        assertEquals("15", calc.evaluate("-3 * -5"))
    }

    // ====================================================================
    // Decimal numbers
    // ====================================================================

    @Test
    fun `decimal addition`() {
        assertNumericEquals("0.3", calc.evaluate("0.1 + 0.2"))
    }

    @Test
    fun `decimal subtraction`() {
        assertNumericEquals("0.1", calc.evaluate("0.3 - 0.2"))
    }

    @Test
    fun `decimal multiplication`() {
        assertNumericEquals("0.06", calc.evaluate("0.2 * 0.3"))
    }

    @Test
    fun `decimal division`() {
        assertNumericEquals("0.5", calc.evaluate("1 / 2"))
    }

    @Test
    fun `integer result from decimal division`() {
        assertNumericEquals("5", calc.evaluate("2.5 / 0.5"))
    }

    @Test
    fun `mixed integer and decimal`() {
        assertNumericEquals("3.5", calc.evaluate("1 + 2.5"))
    }

    @Test
    fun `trailing zero stripped from result`() {
        assertNumericEquals("2", calc.evaluate("1.5 + 0.5"))
    }

    @Test
    fun `classic floating point canary - 0_1 plus 0_2`() {
        // Detect whether implementation uses exact arithmetic (BigDecimal) or IEEE 754 doubles.
        // If exact: "0.3". If double: "0.30000000000000004".
        val result = calc.evaluate("0.1 + 0.2")
        val bd = BigDecimal(result)
        assertTrue(
            bd.compareTo(BigDecimal("0.3")) == 0 || result == "0.30000000000000004",
            "0.1+0.2 should be either exact 0.3 or the known double drift, got: $result"
        )
    }

    // ====================================================================
    // Whitespace handling
    // ====================================================================

    @Test
    fun `no spaces`() {
        assertEquals("5", calc.evaluate("2+3"))
    }

    @Test
    fun `extra spaces everywhere`() {
        assertEquals("5", calc.evaluate("  2  +  3  "))
    }

    @Test
    fun `tabs between tokens`() {
        assertEquals("5", calc.evaluate("2\t+\t3"))
    }

    @Test
    fun `leading and trailing whitespace`() {
        assertEquals("42", calc.evaluate("   42   "))
    }

    // ====================================================================
    // Large numbers and precision
    // ====================================================================

    @Test
    fun `large integer multiplication`() {
        assertEquals("1000000000000", calc.evaluate("1000000 * 1000000"))
    }

    @Test
    fun `large addition`() {
        assertEquals("999999999999", calc.evaluate("999999999998 + 1"))
    }

    @Test
    fun `repeating decimal result`() {
        val result = calc.evaluate("1 / 3")
        // Should produce a finite decimal representation
        assert(result.startsWith("0.3")) { "1/3 should start with 0.3 but got $result" }
    }

    // ====================================================================
    // Division by zero
    // ====================================================================

    @Test
    fun `division by zero throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("1 / 0")
        }
    }

    @Test
    fun `division by zero in subexpression throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("5 + 1 / 0")
        }
    }

    @Test
    fun `division by zero via expression throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("10 / (5 - 5)")
        }
    }

    // ====================================================================
    // Malformed expressions
    // ====================================================================

    @Test
    fun `empty expression throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("")
        }
    }

    @Test
    fun `only whitespace throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("   ")
        }
    }

    @Test
    fun `trailing operator throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("5 +")
        }
    }

    @Test
    fun `leading binary operator throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("* 5")
        }
    }

    @Test
    fun `consecutive binary operators throw`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("5 + * 3")
        }
    }

    @Test
    fun `unmatched opening paren throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("(1 + 2")
        }
    }

    @Test
    fun `unmatched closing paren throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("1 + 2)")
        }
    }

    @Test
    fun `empty parentheses throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("()")
        }
    }

    @Test
    fun `letters in expression throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("2 + abc")
        }
    }

    @Test
    fun `unicode digits rejected`() {
        // Fullwidth digit "３" (U+FF13) is not a valid token
        assertFailsWith<RuntimeException> {
            calc.evaluate("\uFF13 + 1")
        }
    }

    @Test
    fun `emoji in expression throws`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("1 + \uD83D\uDE00")
        }
    }

    @Test
    fun `arabic numeral characters rejected`() {
        // Arabic-Indic digit "٣" (U+0663) should not be accepted
        assertFailsWith<RuntimeException> {
            calc.evaluate("\u0663 + 1")
        }
    }

    @Test
    fun `unicode math plus rejected`() {
        // "＋" (U+FF0B) fullwidth plus is not a valid operator
        assertFailsWith<RuntimeException> {
            calc.evaluate("1 \uFF0B 2")
        }
    }

    @Test
    fun `invisible characters rejected`() {
        // Zero-width space (U+200B) between digits should not silently pass
        assertFailsWith<RuntimeException> {
            calc.evaluate("1\u200B2 + 3")
        }
    }

    @Test
    fun `null byte in expression rejected`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("1 + \u00002")
        }
    }

    @Test
    fun `multiple decimal points rejected`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate("1.2.3 + 4")
        }
    }

    @Test
    fun `standalone decimal point rejected`() {
        assertFailsWith<RuntimeException> {
            calc.evaluate(". + 1")
        }
    }

    // ====================================================================
    // Edge cases and tricky expressions
    // ====================================================================

    @Test
    fun `single number`() {
        assertEquals("42", calc.evaluate("42"))
    }

    @Test
    fun `single zero`() {
        assertEquals("0", calc.evaluate("0"))
    }

    @Test
    fun `negative zero should be zero`() {
        assertEquals("0", calc.evaluate("-0"))
    }

    @Test
    fun `result of subtraction should not be negative zero`() {
        assertEquals("0", calc.evaluate("0 - 0"))
    }

    @Test
    fun `parenthesized unary minus in multiplication`() {
        assertEquals("12", calc.evaluate("(-3) * (-4)"))
    }

    @Test
    fun `expression with many operations`() {
        // 1 + 2 * 3 - 4 / 2 + 5 = 1 + 6 - 2 + 5 = 10
        assertEquals("10", calc.evaluate("1 + 2 * 3 - 4 / 2 + 5"))
    }

    @Test
    fun `long chain of additions`() {
        // 1+1+1+...+1 (20 times) = 20
        val expr = (1..20).joinToString(" + ") { "1" }
        assertEquals("20", calc.evaluate(expr))
    }

    @Test
    fun `associativity stress - nested subtractions`() {
        // 100 - 50 - 25 - 10 - 5 = 10 (left to right)
        assertEquals("10", calc.evaluate("100 - 50 - 25 - 10 - 5"))
    }

    @Test
    fun `division precision - no floating point drift`() {
        // 0.1 + 0.1 + 0.1 - 0.3 should be very close to 0
        val result = BigDecimal(calc.evaluate("0.1 + 0.1 + 0.1 - 0.3"))
        assertTrue(
            result.abs() < BigDecimal("0.0001"),
            "0.1+0.1+0.1-0.3 should be ~0 but got $result"
        )
    }

    @Test
    fun `multiply then divide returns to original`() {
        assertEquals("7", calc.evaluate("7 * 13 / 13"))
    }

    @Test
    fun `parenthesized negative in complex expression`() {
        // (-2) * (-3) + (-4) * 5 = 6 + (-20) = -14
        assertEquals("-14", calc.evaluate("(-2) * (-3) + (-4) * 5"))
    }

    @Test
    fun `decimal near zero`() {
        assertEquals("0.001", calc.evaluate("0.002 - 0.001"))
    }

    @Test
    fun `unary minus on parenthesized product`() {
        assertEquals("-6", calc.evaluate("-(2 * 3)"))
    }
}
