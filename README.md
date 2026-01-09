# MCP Mobile Testing Framework

A **Kotlin DSL** for writing mobile UI tests powered by **Claude Code** and **mobile-mcp**.

Works with **any Android or iOS app**.

## Features

- **Kotlin DSL** - Type-safe, readable test definitions with IDE autocomplete
- **AI-Powered Verification** - Claude analyzes screenshots against natural language expectations
- **Natural Language Selectors** - Find elements by description, not brittle IDs
- **Cross-Platform** - Works with Android devices and iOS simulators

## Installation

### Gradle (via JitPack)

**settings.gradle.kts:**
```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

**build.gradle.kts:**
```kotlin
dependencies {
    implementation("com.github.vladkarpman:mcp-mobile-testing:1.0.0")
}
```

## Quick Start

### 1. Define Screen Elements

```kotlin
object LoginScreen {
    const val EMAIL_FIELD = "Email"
    const val PASSWORD_FIELD = "Password"
    const val LOGIN_BUTTON = "Login"

    const val EXPECTATION = """
        Login screen showing:
        - Email input field
        - Password input field
        - Login button
    """
}
```

### 2. Write Tests

```kotlin
val loginTestSuite = testSuite("Login") {
    beforeAll {
        terminateApp()
        launchApp()
        wait(3.seconds)
    }

    test("User can login") {
        timeout = 60.seconds

        step("Enter credentials") {
            tap(LoginScreen.EMAIL_FIELD)
            type("user@example.com")
            tap(LoginScreen.PASSWORD_FIELD)
            type("password123")
        }

        step("Submit and verify") {
            tap(LoginScreen.LOGIN_BUTTON)
            waitForScreen("Home screen")
            verifyScreen(HomeScreen.EXPECTATION)
            captureScreenshot("login_success")
        }
    }
}
```

### 3. Run with Claude Code

```
Run the login test suite from tests/mcp/myapp/suites/LoginTestSuite.kt
```

## DSL Reference

### Actions

| Action | Usage |
|--------|-------|
| `tap(element)` | `tap("Continue")` |
| `type(text)` | `type("hello")` |
| `swipe(direction)` | `swipe(SwipeDirection.UP)` |
| `pressButton(button)` | `pressButton(DeviceButton.BACK)` |
| `wait(duration)` | `wait(2.seconds)` |
| `launchApp()` | Launch the configured app |
| `terminateApp()` | Force stop the app |

### Assertions

| Assertion | Usage |
|-----------|-------|
| `verifyScreen(expectation)` | AI verifies screenshot matches description |
| `verifyScreenContains(*elements)` | Check specific elements are present |
| `verifyNoElement(element)` | Check element is NOT present |

### Flow Control

| Control | Usage |
|---------|-------|
| `waitFor(element)` | Wait for element to appear |
| `waitForScreen(expectation)` | Wait for screen state |
| `ifPresent(element) { }` | Execute only if element visible |
| `retryOnFailure { }` | Retry block on failure |
| `repeat(n) { }` | Repeat actions n times |

## Documentation

- [GUIDE.md](docs/GUIDE.md) - Step-by-step tutorial
- [RULES.md](docs/RULES.md) - Best practices and rules

## Templates

Copy-paste templates available in `/templates/`:

- `ScreenTemplate.kt` - Screen element definitions
- `TestSuiteTemplate.kt` - Test suite structure
- `HelperTemplate.kt` - Reusable helper functions

## Requirements

- **Claude Code CLI** with mobile-mcp configured
- **Device**: Android (via adb) or iOS simulator
- **JDK 17+** for Kotlin compilation

## How It Works

```
Your Test DSL  →  Claude Code  →  mobile-mcp  →  Device
     ↓                ↓              ↓
tap("Login")   interprets DSL    executes actions
verifyScreen   takes screenshot   returns results
               analyzes with AI
```

1. Claude Code reads your Kotlin DSL test file
2. Interprets each action (`tap`, `swipe`, `verifyScreen`)
3. Executes via mobile-mcp tools
4. AI analyzes screenshots for verification
5. Reports pass/fail with details

## License

MIT License
