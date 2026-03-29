package com.aqa.interview.calculator

/**
 * Evaluates arithmetic expressions and returns their result as a string.
 *
 * Obtain an instance via [calculator].
 */
interface ExpressionCalculator {

    /**
     * Evaluates [expression] and returns the result as a plain decimal string.
     *
     * Supported operations: `+`, `-`, `*`, `/`, parentheses, unary minus, and decimals.
     *
     * @throws RuntimeException if the expression is malformed or division by zero occurs.
     */
    fun evaluate(expression: String): String
}
