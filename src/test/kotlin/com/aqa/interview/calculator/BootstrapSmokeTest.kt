package com.aqa.interview.calculator

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class BootstrapSmokeTest {

    @Test
    fun `calculator bootstrap reflects environment`() {
        val result = runCatching { calculator() }

        if (result.isSuccess) {
            // Running inside CI — the private runtime is injected.
            // Verify the calculator is actually usable.
            assertNotNull(result.getOrNull())
        } else {
            // Running locally — no runtime available.
            val exception = result.exceptionOrNull()
            assertNotNull(exception)
            assertEquals(IllegalStateException::class, exception::class)
            assertEquals(
                "The calculator is not available in this environment. Run your tests through the CI service.",
                exception.message,
            )
        }
    }
}
