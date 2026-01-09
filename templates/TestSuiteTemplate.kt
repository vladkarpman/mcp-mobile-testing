package your.app.tests.suites

import mcp.testing.dsl.*
import your.app.tests.screens.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.minutes

/**
 * {FeatureName} test suite.
 *
 * HOW TO USE:
 * 1. Copy this file to your project's suites/ directory
 * 2. Rename file and val to match your feature
 * 3. Update imports to your screen objects
 * 4. Replace placeholder tests with actual test logic
 *
 * RUN:
 * Ask Claude Code: "Run the {feature} test suite from tests/mcp/myapp/suites/{FileName}.kt"
 */
val featureNameTestSuite = testSuite("Feature Name") {
    description = "Tests for {feature} functionality"

    // ========================================
    // Suite Setup & Teardown
    // ========================================

    beforeAll {
        // Runs ONCE before all tests
        terminateApp()
        launchApp()
        wait(3.seconds)

        // Handle onboarding if needed
        ifPresent("Continue") {
            // completeOnboarding()  // Use your helper
        }
    }

    afterAll {
        // Runs ONCE after all tests
        terminateApp()
    }

    // ========================================
    // Test: Happy Path / Smoke Test
    // ========================================
    test("Feature works with valid input") {
        description = "Verify main feature flow succeeds"
        tags = listOf("smoke", "critical", "feature-name")
        timeout = 60.seconds

        step("Navigate to feature") {
            tap(HomeScreen.FEATURE_BUTTON)
            waitFor(FeatureScreen.TITLE, timeout = 5.seconds)
        }

        step("Perform main action") {
            tap(FeatureScreen.INPUT_FIELD)
            type("valid input")
            tap(FeatureScreen.SUBMIT_BUTTON)
        }

        step("Verify success") {
            waitForScreen("Success state shown", timeout = 10.seconds)
            verifyScreen(FeatureScreen.SUCCESS_EXPECTATION)
            captureScreenshot("feature_success")
        }
    }

    // ========================================
    // Test: Error Handling
    // ========================================
    test("Feature shows error for invalid input") {
        description = "Verify error handling works correctly"
        tags = listOf("feature-name", "error-handling")
        timeout = 30.seconds

        step("Navigate to feature") {
            tap(HomeScreen.FEATURE_BUTTON)
            waitFor(FeatureScreen.TITLE)
        }

        step("Enter invalid data") {
            tap(FeatureScreen.INPUT_FIELD)
            type("invalid!")
            tap(FeatureScreen.SUBMIT_BUTTON)
        }

        step("Verify error shown") {
            waitFor("Error", "Invalid", timeout = 5.seconds)
            verifyScreen(FeatureScreen.ERROR_EXPECTATION)
            captureScreenshot("feature_error")
        }
    }

    // ========================================
    // Test: Edge Case
    // ========================================
    test("Feature handles empty input") {
        description = "Verify empty input is handled gracefully"
        tags = listOf("feature-name", "edge-case")
        timeout = 30.seconds

        step("Navigate and submit empty") {
            tap(HomeScreen.FEATURE_BUTTON)
            waitFor(FeatureScreen.TITLE)
            // Don't type anything
            tap(FeatureScreen.SUBMIT_BUTTON)
        }

        step("Verify validation") {
            waitFor("Required", "Cannot be empty", timeout = 3.seconds)
            captureScreenshot("feature_validation")
        }
    }

    // ========================================
    // Test: Navigation / Back Behavior
    // ========================================
    test("Can navigate back from feature") {
        description = "Verify back navigation works"
        tags = listOf("feature-name", "navigation")
        timeout = 30.seconds

        step("Navigate to feature") {
            tap(HomeScreen.FEATURE_BUTTON)
            waitFor(FeatureScreen.TITLE)
        }

        step("Press back") {
            pressButton(DeviceButton.BACK)
            wait(1.seconds)
        }

        step("Verify returned to home") {
            verifyScreenContains(HomeScreen.TITLE)
        }
    }
}

// ========================================
// Standalone Smoke Test
// ========================================

/**
 * Quick smoke test that can be run independently.
 */
val featureNameSmokeTest = test("Feature smoke test") {
    description = "Quick verification of feature"
    tags = listOf("smoke")
    timeout = 30.seconds

    step("Launch and navigate") {
        launchApp()
        wait(3.seconds)
        tap(HomeScreen.FEATURE_BUTTON)
    }

    step("Verify feature screen") {
        waitFor(FeatureScreen.TITLE, timeout = 5.seconds)
        verifyScreen(FeatureScreen.EXPECTATION)
    }
}
