package com.aqa.interview.calculator.runtime

import com.aqa.interview.calculator.ExpressionCalculator
import java.util.ServiceLoader

object CalculatorRuntime {
    fun createCalculator(): ExpressionCalculator {
        val provider = ServiceLoader.load(CalculatorRuntimeProvider::class.java)
            .findFirst()
            .orElseThrow {
                IllegalStateException("The calculator is not available in this environment. Run your tests through the CI service.")
            }

        return provider.createCalculator()
    }
}
