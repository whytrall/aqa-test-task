# AQA Candidate Template

This project is a Kotlin/Gradle/JUnit scaffold for writing tests against a calculator API.
Your tests will be executed remotely through the interview CI service — they are **not expected to pass locally**.

## Getting Started

1. Open this project in your IDE (IntelliJ IDEA recommended).
2. The calculator API is in `src/main/kotlin/`. Look at `ExpressionCalculator` for the contract.

## Writing Tests

Obtain a calculator instance using the `calculator()` helper and call `evaluate()`:

```kotlin
import com.aqa.interview.calculator.calculator
import kotlin.test.Test
import kotlin.test.assertEquals

class MyTest {
    private val calculator = calculator()

    @Test
    fun `basic addition`() {
        assertEquals("3", calculator.evaluate("1 + 2"))
    }
}
```

`evaluate()` accepts an arithmetic expression string and returns the result as a plain decimal string.

Supported operations: `+`, `-`, `*`, `/`, parentheses, unary minus, and decimals.

## Running Your Tests

Tests run remotely through the CI service. To submit your work:

1. Write your tests under `src/test/kotlin/`.
2. Commit your changes on the configured branch.
3. Push to your GitHub repository.
4. Trigger a run from the interview CI service.

The CI service will execute `./gradlew test` against your code and stream the results back in real time.

## Local Execution

Running tests locally will fail with an error — this is by design.
The calculator implementation is only available in the remote CI environment.
