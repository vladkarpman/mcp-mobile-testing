package mcp.testing.config

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Configuration interface for app-specific test settings.
 *
 * Implement this interface to configure the MCP testing framework
 * for your specific application.
 *
 * Example implementation:
 * ```kotlin
 * object MyAppConfig : AppTestConfig {
 *     override val packageName = "com.example.myapp"
 *     override val appName = "My App"
 *     override val defaultTimeout = 60.seconds
 *     override val defaultPollInterval = 500.milliseconds
 * }
 * ```
 */
interface AppTestConfig {
    /**
     * The package name (Android) or bundle ID (iOS) of the app.
     * Used for launching and terminating the app.
     */
    val packageName: String

    /**
     * Human-readable name of the app.
     * Used in test reports and logging.
     */
    val appName: String

    /**
     * Default timeout for wait operations.
     * Individual tests can override this.
     */
    val defaultTimeout: Duration
        get() = 60.seconds

    /**
     * Default polling interval for wait operations.
     */
    val defaultPollInterval: Duration
        get() = 500.milliseconds

    /**
     * Directory to save screenshots.
     * Relative to project root or absolute path.
     */
    val screenshotDirectory: String
        get() = "test-screenshots"

    /**
     * Whether to capture screenshots on test failure.
     */
    val captureScreenshotOnFailure: Boolean
        get() = true

    /**
     * Maximum time to wait for app launch.
     */
    val appLaunchTimeout: Duration
        get() = 30.seconds
}

/**
 * Holder for the current test configuration.
 * Set this before running tests.
 */
object TestConfigHolder {
    private var _config: AppTestConfig? = null

    val config: AppTestConfig
        get() = _config ?: error(
            "AppTestConfig not set. Call TestConfigHolder.setConfig() before running tests."
        )

    fun setConfig(config: AppTestConfig) {
        _config = config
    }

    fun isConfigured(): Boolean = _config != null
}
