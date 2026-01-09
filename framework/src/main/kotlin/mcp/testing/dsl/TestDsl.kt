package mcp.testing.dsl

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * DSL marker to ensure type-safe DSL building.
 */
@DslMarker
annotation class TestDslMarker

/**
 * Represents a single test specification.
 *
 * Example usage:
 * ```kotlin
 * val myTest = test("Login flow") {
 *     description = "Verify user can log in"
 *     timeout = 60.seconds
 *
 *     step("Launch app") {
 *         launchApp()
 *     }
 *
 *     step("Enter credentials") {
 *         tap("Email field")
 *         type("user@example.com")
 *     }
 * }
 * ```
 */
@TestDslMarker
class TestSpec(val name: String) {
    var description: String = ""
    var tags: List<String> = emptyList()
    var timeout: Duration = 60.seconds

    internal val steps = mutableListOf<TestStep>()
    internal var beforeEach: (suspend TestContext.() -> Unit)? = null
    internal var afterEach: (suspend TestContext.() -> Unit)? = null

    /**
     * Setup to run before each step.
     */
    fun beforeEach(block: suspend TestContext.() -> Unit) {
        beforeEach = block
    }

    /**
     * Cleanup to run after each step.
     */
    fun afterEach(block: suspend TestContext.() -> Unit) {
        afterEach = block
    }

    /**
     * Define a test step with a name and action block.
     */
    fun step(name: String, block: suspend TestContext.() -> Unit) {
        steps.add(TestStep(name, block))
    }
}

/**
 * Represents a single step within a test.
 */
data class TestStep(
    val name: String,
    val action: suspend TestContext.() -> Unit
)

/**
 * DSL entry point for defining a single test.
 *
 * @param name The name of the test
 * @param block Configuration block for the test
 * @return Configured TestSpec
 */
fun test(name: String, block: TestSpec.() -> Unit): TestSpec {
    return TestSpec(name).apply(block)
}

/**
 * Represents a test suite containing multiple tests.
 *
 * Example usage:
 * ```kotlin
 * val loginSuite = testSuite("Authentication") {
 *     description = "All authentication-related tests"
 *
 *     beforeAll {
 *         terminateApp()
 *     }
 *
 *     test("Valid login") {
 *         step("...") { ... }
 *     }
 *
 *     test("Invalid login") {
 *         step("...") { ... }
 *     }
 * }
 * ```
 */
@TestDslMarker
class TestSuiteSpec(val name: String) {
    var description: String = ""
    internal val tests = mutableListOf<TestSpec>()
    internal var beforeAll: (suspend TestContext.() -> Unit)? = null
    internal var afterAll: (suspend TestContext.() -> Unit)? = null

    /**
     * Setup to run once before all tests in the suite.
     */
    fun beforeAll(block: suspend TestContext.() -> Unit) {
        beforeAll = block
    }

    /**
     * Cleanup to run once after all tests in the suite.
     */
    fun afterAll(block: suspend TestContext.() -> Unit) {
        afterAll = block
    }

    /**
     * Add a test to the suite.
     */
    fun test(name: String, block: TestSpec.() -> Unit) {
        tests.add(TestSpec(name).apply(block))
    }
}

/**
 * DSL entry point for defining a test suite.
 *
 * @param name The name of the suite
 * @param block Configuration block for the suite
 * @return Configured TestSuiteSpec
 */
fun testSuite(name: String, block: TestSuiteSpec.() -> Unit): TestSuiteSpec {
    return TestSuiteSpec(name).apply(block)
}

// ========== Result Classes ==========

/**
 * Result of a single test step.
 */
data class StepResult(
    val stepName: String,
    val passed: Boolean,
    val duration: Long,
    val error: String? = null,
    var screenshotPath: String? = null
)

/**
 * Result of a single test.
 */
data class TestResult(
    val testName: String,
    val passed: Boolean,
    val stepResults: List<StepResult>,
    val error: String? = null,
    val duration: Long = 0
)

/**
 * Result of a test suite.
 */
data class SuiteResult(
    val suiteName: String,
    val testResults: List<TestResult>,
    val passed: Boolean,
    val duration: Long = 0
) {
    val passedCount: Int get() = testResults.count { it.passed }
    val failedCount: Int get() = testResults.count { !it.passed }
}
