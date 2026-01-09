package your.app.tests.screens

/**
 * {ScreenName} screen elements.
 *
 * HOW TO USE:
 * 1. Copy this file to your project's screens/ directory
 * 2. Rename file and object to match your screen
 * 3. Replace element constants with actual UI text/labels
 * 4. Update EXPECTATION with screen description
 *
 * TIPS:
 * - Use exact text visible in the UI
 * - Group related elements with comments
 * - Keep EXPECTATION concise but descriptive
 */
object ScreenNameScreen {

    // ========== Navigation ==========

    /** Back button in top bar */
    const val BACK_BUTTON = "Back"

    /** Close/dismiss button */
    const val CLOSE_BUTTON = "Close"

    // ========== Primary Actions ==========

    /** Main action button */
    const val PRIMARY_BUTTON = "Submit"

    /** Secondary action */
    const val SECONDARY_BUTTON = "Cancel"

    // ========== Input Fields ==========

    /** Text input field */
    const val INPUT_FIELD = "Enter text"

    /** Search field */
    const val SEARCH_FIELD = "Search"

    // ========== Content ==========

    /** Title text */
    const val TITLE = "Screen Title"

    /** List item */
    const val LIST_ITEM = "Item"

    // ========== Screen Expectations ==========

    /**
     * Natural language description for AI verification.
     * Be specific about key elements and layout.
     */
    const val EXPECTATION = """
        {ScreenName} screen showing:
        - Title at top: "{Screen Title}"
        - Input field for user entry
        - Primary action button
        - Navigation back button
    """

    /**
     * Alternative state expectation (e.g., loading, empty, error)
     */
    const val LOADING_EXPECTATION = """
        {ScreenName} screen in loading state:
        - Loading indicator visible
        - Content area empty or dimmed
    """

    const val ERROR_EXPECTATION = """
        {ScreenName} screen showing error:
        - Error message visible
        - Retry button available
    """
}

// ========== Optional: Helper Extensions ==========

/*
import mcp.testing.dsl.TestContext
import kotlin.time.Duration.Companion.seconds

/**
 * Navigate to this screen from home.
 */
suspend fun TestContext.goToScreenName() {
    tap(HomeScreen.SOME_BUTTON)
    waitFor(ScreenNameScreen.TITLE, timeout = 5.seconds)
}

/**
 * Fill form on this screen.
 */
suspend fun TestContext.fillScreenNameForm(value: String) {
    tap(ScreenNameScreen.INPUT_FIELD)
    type(value)
    tap(ScreenNameScreen.PRIMARY_BUTTON)
}
*/
