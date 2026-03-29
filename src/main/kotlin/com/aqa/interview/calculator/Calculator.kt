package com.aqa.interview.calculator

import com.aqa.interview.calculator.runtime.CalculatorRuntime

/**
 * Returns a ready-to-use [ExpressionCalculator] instance.
 *
 * This is the recommended way to obtain the calculator in your tests.
 * The implementation is provided automatically by the CI environment.
 */
fun calculator(): ExpressionCalculator = CalculatorRuntime.createCalculator()
