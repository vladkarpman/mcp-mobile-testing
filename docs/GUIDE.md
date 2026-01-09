# How to Create MCP UI Tests

A step-by-step guide for writing mobile UI tests using the MCP Testing Framework.

## Prerequisites

Before you begin:
- [ ] Device connected (`adb devices` for Android)
- [ ] App installed on device
- [ ] Claude Code CLI running with `mobile-mcp` configured

---

## Step A: Define Screen Elements

**What:** Create constants for UI elements you'll interact with.

**Where:** Create a file in `tests/mcp/{yourapp}/screens/`

**Template:**
```kotlin
package com.example.myapp.tests.screens

object LoginScreen {
    // === Element Identifiers ===
    // Use the exact text/label visible in the UI
    const val EMAIL_FIELD = "Email"
    const val PASSWORD_FIELD = "Password"
    const val LOGIN_BUTTON = "Login"
    const val FORGOT_PASSWORD = "Forgot password"

    // === Screen Expectation ===
    // Natural language description for AI verification
    const val EXPECTATION = """
        Login screen showing:
        - Email input field at top
        - Password input field below
        - Login button
        - Forgot password link
    """
}
```

**Expected Result:** A Kotlin object with constants for each UI element.

---

## Step B: Write Test Suite

**What:** Define tests with steps that use your screen elements.

**Where:** Create a file in `tests/mcp/{yourapp}/suites/`

**Template:**
```kotlin
package com.example.myapp.tests.suites

import mcp.testing.dsl.*
import com.example.myapp.tests.screens.*
import kotlin.time.Duration.Companion.seconds

val loginTestSuite = testSuite("Login") {
    description = "Tests for login functionality"

    // Runs once before all tests
    beforeAll {
        terminateApp()
        launchApp()
        wait(3.seconds)
    }

    // Runs once after all tests
    afterAll {
        terminateApp()
    }

    test("User can login with valid credentials") {
        description = "Verify successful login flow"
        tags = listOf("smoke", "auth", "critical")
        timeout = 60.seconds

        step("Navigate to login") {
            tap("Login")
            waitFor(LoginScreen.EMAIL_FIELD)
        }

        step("Enter credentials") {
            tap(LoginScreen.EMAIL_FIELD)
            type("user@example.com")
            tap(LoginScreen.PASSWORD_FIELD)
            type("password123")
        }

        step("Submit and verify") {
            tap(LoginScreen.LOGIN_BUTTON)
            waitForScreen("Home screen after login", timeout = 10.seconds)
            verifyScreen(HomeScreen.EXPECTATION)
            captureScreenshot("login_success")
        }
    }

    test("Invalid credentials show error") {
        tags = listOf("auth", "error")
        timeout = 30.seconds

        step("Try invalid login") {
            tap(LoginScreen.EMAIL_FIELD)
            type("wrong@example.com")
            tap(LoginScreen.PASSWORD_FIELD)
            type("wrongpassword")
            tap(LoginScreen.LOGIN_BUTTON)
        }

        step("Verify error shown") {
            waitFor("Invalid credentials", "Error")
            verifyScreen("Error message displayed")
            captureScreenshot("login_error")
        }
    }
}
```

**Expected Result:** A test suite file that Claude Code can execute.

---

## Step C: Run the Test

**What:** Ask Claude Code to execute your test.

**Command Examples:**
```
Run the login test suite from tests/mcp/myapp/suites/LoginTestSuite.kt
```

```
Run the "User can login" test from LoginTestSuite.kt
```

```
Run all smoke tests in tests/mcp/myapp/suites/
```

**Expected Result:**
- Claude reads your test file
- Executes each step sequentially
- Takes screenshots as specified
- Reports pass/fail for each step
- Provides detailed error info on failure

---

## Quick Reference

### Actions
| Action | Example |
|--------|---------|
| Tap element | `tap("Continue")` |
| Tap coordinates | `tap(500, 1000)` |
| Type text | `type("hello")` |
| Swipe | `swipe(SwipeDirection.UP)` |
| Press button | `pressButton(DeviceButton.BACK)` |
| Wait | `wait(2.seconds)` |
| Launch app | `launchApp()` |
| Close app | `terminateApp()` |

### Assertions
| Assertion | Example |
|-----------|---------|
| Verify screen | `verifyScreen("Home with 4 tabs")` |
| Check elements | `verifyScreenContains("Home", "Settings")` |
| Check absence | `verifyNoElement("Error")` |

### Flow Control
| Control | Example |
|---------|---------|
| Wait for element | `waitFor("Continue", timeout = 10.seconds)` |
| Conditional | `ifPresent("Skip") { tap("Skip") }` |
| Retry | `retryOnFailure(maxAttempts = 3) { tap("Submit") }` |
| Repeat | `repeat(5) { tap("Next") }` |

---

## Example: Complete Flow

```kotlin
// 1. Define screens
object OnboardingScreen {
    const val SKIP_BUTTON = "Skip"
    const val NEXT_BUTTON = "Next"
    const val DONE_BUTTON = "Done"
}

object HomeScreen {
    const val TAB_HOME = "Home"
    const val SETTINGS = "Settings"
    const val EXPECTATION = """
        Home screen with:
        - Bottom navigation bar
        - Home tab selected
    """
}

// 2. Write test
val onboardingTest = test("Complete onboarding") {
    timeout = 120.seconds

    step("Launch app") {
        launchApp()
        wait(3.seconds)
    }

    step("Skip or complete onboarding") {
        ifPresent(OnboardingScreen.SKIP_BUTTON) {
            tap(OnboardingScreen.SKIP_BUTTON)
        }

        // Or go through pages
        repeat(3) {
            ifPresent(OnboardingScreen.NEXT_BUTTON) {
                tap(OnboardingScreen.NEXT_BUTTON)
                wait(1.seconds)
            }
        }
    }

    step("Verify home screen") {
        waitFor(HomeScreen.TAB_HOME, timeout = 10.seconds)
        verifyScreen(HomeScreen.EXPECTATION)
        captureScreenshot("onboarding_complete")
    }
}

// 3. Run: "Run the onboarding test"
```

---

## Next Steps

- Read [RULES.md](./RULES.md) for best practices
- Copy templates from `/templates/` directory
- Check examples in the repo
