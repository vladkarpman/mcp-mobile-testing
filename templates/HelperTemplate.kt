package your.app.tests.helpers

import mcp.testing.dsl.TestContext
import mcp.testing.dsl.DeviceButton
import mcp.testing.dsl.SwipeDirection
import your.app.tests.screens.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds

/**
 * {FeatureName} helper functions.
 *
 * HOW TO USE:
 * 1. Copy this file to your project's helpers/ directory
 * 2. Rename and update functions for your app's flows
 * 3. Import and use in your test suites
 *
 * TIPS:
 * - Extract common flows that appear in multiple tests
 * - Keep helpers focused on single responsibilities
 * - Use descriptive function names
 */

// ========================================
// Authentication Helpers
// ========================================

/**
 * Log in with the given credentials.
 * Navigates to login screen if not already there.
 */
suspend fun TestContext.login(email: String, password: String) {
    // Navigate to login if needed
    ifPresent(HomeScreen.LOGIN_BUTTON) {
        tap(HomeScreen.LOGIN_BUTTON)
        waitFor(LoginScreen.EMAIL_FIELD, timeout = 5.seconds)
    }

    // Enter credentials
    tap(LoginScreen.EMAIL_FIELD)
    type(email)
    tap(LoginScreen.PASSWORD_FIELD)
    type(password)

    // Submit
    tap(LoginScreen.SUBMIT_BUTTON)

    // Wait for success
    waitForScreen("Logged in successfully", timeout = 10.seconds)
}

/**
 * Log out from current session.
 */
suspend fun TestContext.logout() {
    tap(HomeScreen.PROFILE_TAB)
    wait(1.seconds)
    tap(ProfileScreen.LOGOUT_BUTTON)
    waitFor(LoginScreen.LOGIN_BUTTON, timeout = 5.seconds)
}

// ========================================
// Onboarding Helpers
// ========================================

/**
 * Complete the full onboarding/walkthrough flow.
 */
suspend fun TestContext.completeOnboarding() {
    // Go through intro pages
    repeat(5) {
        ifPresent(OnboardingScreen.NEXT_BUTTON) {
            tap(OnboardingScreen.NEXT_BUTTON)
            wait(1.seconds)
        }
        ifPresent(OnboardingScreen.CONTINUE_BUTTON) {
            tap(OnboardingScreen.CONTINUE_BUTTON)
            wait(1.seconds)
        }
    }

    // Handle permissions
    ifPresent("Allow") {
        tap("Allow")
        wait(500.milliseconds)
    }

    // Final step
    ifPresent(OnboardingScreen.GET_STARTED_BUTTON) {
        tap(OnboardingScreen.GET_STARTED_BUTTON)
    }

    // Verify home reached
    waitFor(HomeScreen.MAIN_CONTENT, timeout = 10.seconds)
}

/**
 * Skip onboarding if possible.
 */
suspend fun TestContext.skipOnboarding() {
    ifPresent(OnboardingScreen.SKIP_BUTTON) {
        tap(OnboardingScreen.SKIP_BUTTON)
        waitFor(HomeScreen.MAIN_CONTENT, timeout = 5.seconds)
    }
}

// ========================================
// Navigation Helpers
// ========================================

/**
 * Navigate to a specific tab.
 */
suspend fun TestContext.navigateToTab(tabName: String) {
    tap(tabName)
    wait(1.seconds)
}

/**
 * Go back to home screen from anywhere.
 */
suspend fun TestContext.goToHome() {
    // Try home tab first
    ifPresent(HomeScreen.TAB_HOME) {
        tap(HomeScreen.TAB_HOME)
        wait(1.seconds)
        return
    }

    // Otherwise press back repeatedly
    repeat(3) {
        pressButton(DeviceButton.BACK)
        wait(500.milliseconds)

        // Check if home reached
        ifPresent(HomeScreen.MAIN_CONTENT) {
            return
        }
    }
}

// ========================================
// Dialog/Popup Helpers
// ========================================

/**
 * Dismiss any popup or dialog that might be blocking.
 */
suspend fun TestContext.dismissPopups() {
    ifPresent("Cancel") { tap("Cancel") }
    ifPresent("Close") { tap("Close") }
    ifPresent("Not now") { tap("Not now") }
    ifPresent("Skip") { tap("Skip") }
    ifPresent("Dismiss") { tap("Dismiss") }

    // Fallback: press back
    pressButton(DeviceButton.BACK)
    wait(500.milliseconds)
}

/**
 * Accept permission dialog if shown.
 */
suspend fun TestContext.acceptPermission() {
    ifPresent("Allow") { tap("Allow") }
    ifPresent("While using") { tap("While using") }
    ifPresent("Allow all") { tap("Allow all") }
}

// ========================================
// Form Helpers
// ========================================

/**
 * Fill a text field by label.
 */
suspend fun TestContext.fillField(fieldLabel: String, value: String) {
    tap(fieldLabel)
    wait(300.milliseconds)
    type(value)
}

/**
 * Clear and fill a text field.
 */
suspend fun TestContext.clearAndFill(fieldLabel: String, value: String) {
    tap(fieldLabel)
    wait(300.milliseconds)
    // Select all and replace
    type(value)
}

// ========================================
// Scroll Helpers
// ========================================

/**
 * Scroll down to find an element.
 */
suspend fun TestContext.scrollToElement(elementDescription: String, maxScrolls: Int = 5) {
    repeat(maxScrolls) {
        ifPresent(elementDescription) {
            return
        }
        swipe(SwipeDirection.UP)
        wait(500.milliseconds)
    }
}

/**
 * Scroll to top of list/page.
 */
suspend fun TestContext.scrollToTop(scrollCount: Int = 3) {
    repeat(scrollCount) {
        swipe(SwipeDirection.DOWN)
        wait(300.milliseconds)
    }
}
