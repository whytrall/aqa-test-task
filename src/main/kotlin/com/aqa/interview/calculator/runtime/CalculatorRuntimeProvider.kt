package com.aqa.interview.calculator.runtime

import com.aqa.interview.calculator.ExpressionCalculator

fun interface CalculatorRuntimeProvider {
    fun createCalculator(): ExpressionCalculator
}
