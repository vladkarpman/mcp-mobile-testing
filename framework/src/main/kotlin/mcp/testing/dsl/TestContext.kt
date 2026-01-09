package mcp.testing.dsl

import mcp.testing.config.AppTestConfig
import mcp.testing.config.TestConfigHolder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Context for test execution providing all available actions and assertions.
 *
 * This class is designed to be interpreted by Claude Code which translates
 * DSL method calls into mobile-mcp tool invocations.
 *
 * ## Actions
 * - [tap] - Tap on an element by description or coordinates
 * - [doubleTap] - Double tap on an element
 * - [longPress] - Long press on an element
 * - [swipe] - Swipe in a direction
 * - [type] - Type text into focused element
 * - [pressButton] - Press device button (BACK, HOME, etc.)
 *
 * ## Assertions
 * - [verifyScreen] - AI-powered screenshot verification
 * - [verifyScreenContains] - Check for specific elements
 * - [verifyNoElement] - Assert element is NOT present
 *
 * ## Flow Control
 * - [waitFor] - Wait for element to appear
 * - [waitForScreen] - Wait for screen state
 * - [ifPresent] - Conditional execution if element exists
 * - [retryOnFailure] - Retry block on failure
 *
 * ## Device Control
 * - [launchApp] - Launch the app
 * - [terminateApp] - Force stop the app
 * - [setOrientation] - Change device orientation
 * - [captureScreenshot] - Take and name a screenshot
 */
@TestDslMarker
class TestContext(
    val deviceId: String,
    private val config: AppTestConfig = TestConfigHolder.config
) {
    /**
     * The app's package name from config.
     */
    val appPackage: String get() = config.packageName

    // ========== Actions ==========

    /**
     * Tap on an element identified by natural language description.
     *
     * Claude Code translates this to:
     * 1. mobile_list_elements_on_screen or mobile_find to locate element
     * 2. mobile_click_on_screen_at_coordinates with found coordinates
     *
     * @param description Natural language description (e.g., "Continue button", "Settings icon")
     */
    suspend fun tap(description: String) {
        // Implemented by Claude Code at runtime
        // Uses: mobile_list_elements_on_screen + mobile_click_on_screen_at_coordinates
    }

    /**
     * Tap at specific screen coordinates.
     *
     * @param x X coordinate in pixels
     * @param y Y coordinate in pixels
     */
    suspend fun tap(x: Int, y: Int) {
        // Implemented by Claude Code: mobile_click_on_screen_at_coordinates
    }

    /**
     * Double tap on an element.
     *
     * @param description Element description
     */
    suspend fun doubleTap(description: String) {
        // Implemented by Claude Code: mobile_double_tap_on_screen
    }

    /**
     * Double tap at coordinates.
     */
    suspend fun doubleTap(x: Int, y: Int) {
        // Implemented by Claude Code: mobile_double_tap_on_screen
    }

    /**
     * Long press on an element.
     *
     * @param description Element description
     * @param duration How long to press (default 500ms)
     */
    suspend fun longPress(description: String, duration: Duration = 500.milliseconds) {
        // Implemented by Claude Code: mobile_long_press_on_screen_at_coordinates
    }

    /**
     * Type text into the currently focused input field.
     *
     * @param text Text to type
     * @param submit If true, press Enter after typing
     */
    suspend fun type(text: String, submit: Boolean = false) {
        // Implemented by Claude Code: mobile_type_keys
    }

    /**
     * Swipe in a direction from center of screen.
     *
     * @param direction Direction to swipe
     * @param distance Optional swipe distance in pixels
     */
    suspend fun swipe(direction: SwipeDirection, distance: Int? = null) {
        // Implemented by Claude Code: mobile_swipe_on_screen
    }

    /**
     * Swipe from one point to another.
     *
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     */
    suspend fun swipeFrom(startX: Int, startY: Int, endX: Int, endY: Int) {
        // Custom swipe using coordinates
    }

    /**
     * Press a device button.
     *
     * @param button The button to press
     */
    suspend fun pressButton(button: DeviceButton) {
        // Implemented by Claude Code: mobile_press_button
    }

    // ========== Assertions ==========

    /**
     * AI-powered screen verification.
     *
     * Claude Code will:
     * 1. Take a screenshot via mobile_take_screenshot
     * 2. Analyze the screenshot against the expectation
     * 3. Return match result with confidence
     *
     * @param expectation Natural language description of expected screen state
     * @param strictness How strictly to match (default NORMAL)
     * @return Verification result
     *
     * Example:
     * ```kotlin
     * verifyScreen("Home screen with 4 bottom tabs: Home, AI Image, AI Video, Me")
     * verifyScreen("Loading indicator visible", strictness = LENIENT)
     * ```
     */
    suspend fun verifyScreen(
        expectation: String,
        strictness: VerificationStrictness = VerificationStrictness.NORMAL
    ): ScreenVerificationResult {
        // Implemented by Claude Code: mobile_take_screenshot + AI analysis
        return ScreenVerificationResult(matches = true, confidence = 100, details = "")
    }

    /**
     * Verify that specific elements are present on screen.
     *
     * First checks accessibility tree, falls back to AI verification.
     *
     * @param elements Element descriptions to find
     * @throws AssertionError if any element is not found
     */
    suspend fun verifyScreenContains(vararg elements: String) {
        // Check each element via mobile_list_elements_on_screen
        // Fallback to AI verification if not found in accessibility tree
    }

    /**
     * Verify that an element is NOT present on screen.
     *
     * @param description Element that should not be present
     * @throws AssertionError if element is found
     */
    suspend fun verifyNoElement(description: String) {
        // Check mobile_list_elements_on_screen
    }

    // ========== Flow Control ==========

    /**
     * Wait for an element to appear on screen.
     *
     * Polls the screen periodically until the element is found or timeout.
     *
     * @param description Element description to wait for
     * @param timeout Maximum time to wait (default from config)
     * @param pollInterval How often to check (default from config)
     * @throws TimeoutException if element doesn't appear within timeout
     */
    suspend fun waitFor(
        description: String,
        timeout: Duration = config.defaultTimeout,
        pollInterval: Duration = config.defaultPollInterval
    ) {
        // Poll mobile_list_elements_on_screen until found
    }

    /**
     * Wait for any of the specified elements to appear.
     *
     * @param descriptions Multiple element descriptions
     * @param timeout Maximum time to wait
     */
    suspend fun waitFor(
        vararg descriptions: String,
        timeout: Duration = config.defaultTimeout
    ) {
        // Poll until any element is found
    }

    /**
     * Wait for screen to match an expectation.
     *
     * @param expectation Screen state expectation
     * @param timeout Maximum time to wait
     */
    suspend fun waitForScreen(expectation: String, timeout: Duration = config.defaultTimeout) {
        // Poll with verifyScreen until matches
    }

    /**
     * Execute block only if element is present on screen.
     *
     * @param description Element to check for
     * @param block Code to execute if element is present
     */
    suspend fun ifPresent(description: String, block: suspend TestContext.() -> Unit) {
        // Check mobile_list_elements_on_screen, execute if found
    }

    /**
     * Retry a block on failure.
     *
     * @param maxAttempts Maximum number of attempts
     * @param delayBetweenAttempts Delay between retries
     * @param block Code to retry
     */
    suspend fun retryOnFailure(
        maxAttempts: Int = 3,
        delayBetweenAttempts: Duration = 1.seconds,
        block: suspend TestContext.() -> Unit
    ) {
        // Retry logic
    }

    // ========== Screenshots ==========

    /**
     * Capture a screenshot with a name.
     *
     * @param name Name for the screenshot
     * @return Screenshot data
     */
    suspend fun captureScreenshot(name: String): ScreenshotCapture {
        // Implemented by Claude Code: mobile_take_screenshot
        return ScreenshotCapture(name)
    }

    /**
     * Save screenshot to a file.
     *
     * @param path Path to save the screenshot
     */
    suspend fun saveScreenshot(path: String) {
        // Implemented by Claude Code: mobile_save_screenshot
    }

    // ========== Device Operations ==========

    /**
     * Launch the app.
     * Uses the package name from the configured [AppTestConfig].
     */
    suspend fun launchApp() {
        // Implemented by Claude Code: mobile_launch_app with config.packageName
    }

    /**
     * Launch a specific app by package name.
     *
     * @param packageName Package to launch
     */
    suspend fun launchApp(packageName: String) {
        // Implemented by Claude Code: mobile_launch_app
    }

    /**
     * Force stop the app.
     * Uses the package name from the configured [AppTestConfig].
     */
    suspend fun terminateApp() {
        // Implemented by Claude Code: mobile_terminate_app with config.packageName
    }

    /**
     * Force stop a specific app by package name.
     *
     * @param packageName Package to terminate
     */
    suspend fun terminateApp(packageName: String) {
        // Implemented by Claude Code: mobile_terminate_app
    }

    /**
     * Open a URL in the browser.
     *
     * @param url URL to open
     */
    suspend fun openUrl(url: String) {
        // Implemented by Claude Code: mobile_open_url
    }

    /**
     * Set device orientation.
     *
     * @param orientation Target orientation
     */
    suspend fun setOrientation(orientation: Orientation) {
        // Implemented by Claude Code: mobile_set_orientation
    }

    /**
     * Wait for a specified duration.
     *
     * @param duration Time to wait
     */
    suspend fun wait(duration: Duration) {
        kotlinx.coroutines.delay(duration)
    }

    /**
     * Repeat a block of actions.
     */
    suspend fun repeat(times: Int, block: suspend TestContext.() -> Unit) {
        kotlin.repeat(times) {
            block()
        }
    }
}

// ========== Enums ==========

enum class SwipeDirection {
    UP, DOWN, LEFT, RIGHT
}

enum class DeviceButton(val mcpName: String) {
    BACK("BACK"),
    HOME("HOME"),
    VOLUME_UP("VOLUME_UP"),
    VOLUME_DOWN("VOLUME_DOWN"),
    ENTER("ENTER")
}

enum class Orientation {
    PORTRAIT, LANDSCAPE
}

enum class VerificationStrictness {
    /** Exact match required */
    STRICT,
    /** Key elements must be present, minor differences OK */
    NORMAL,
    /** General structure should match */
    LENIENT
}

// ========== Data Classes ==========

data class ScreenVerificationResult(
    val matches: Boolean,
    val confidence: Int,  // 0-100
    val details: String,
    val suggestions: List<String> = emptyList()
)

data class ScreenshotCapture(
    var name: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class TimeoutException(message: String) : Exception(message)
