# MCP Testing Rules & Best Practices

Follow these rules to write reliable, maintainable tests.

---

## Rule 1: Use Screen Constants

Define element identifiers in screen objects, not inline strings.

```kotlin
// Bad
tap("Login")
tap("Email")
tap("Password")

// Good
tap(LoginScreen.LOGIN_BUTTON)
tap(LoginScreen.EMAIL_FIELD)
tap(LoginScreen.PASSWORD_FIELD)
```

**Why:** Single source of truth. If UI text changes, update one place.

---

## Rule 2: Add Waits After Actions

UI needs time to respond. Always add waits after taps.

```kotlin
// Bad - may fail if UI hasn't updated
tap(button)
tap(nextButton)

// Good - gives UI time to respond
tap(button)
wait(1.seconds)
tap(nextButton)

// Better - wait for specific element
tap(button)
waitFor(nextButton, timeout = 5.seconds)
tap(nextButton)
```

**Recommended waits:**
- After tap: `wait(500.milliseconds)` to `wait(1.seconds)`
- After screen transition: `waitFor(element)` or `waitForScreen()`
- After data load: `waitForScreen("Data loaded")`

---

## Rule 3: Handle Optional Elements

Dialogs, popups, and banners may or may not appear. Use `ifPresent`.

```kotlin
// Bad - fails if popup not shown
tap(PermissionDialog.ALLOW)

// Good - only taps if visible
ifPresent(PermissionDialog.ALLOW) {
    tap(PermissionDialog.ALLOW)
}

// Good - handle multiple optional elements
ifPresent("Skip") { tap("Skip") }
ifPresent("Not now") { tap("Not now") }
ifPresent("Allow") { tap("Allow") }
```

---

## Rule 4: Set Appropriate Timeouts

Different operations need different timeouts.

| Operation | Timeout |
|-----------|---------|
| Quick UI check | 10-30 seconds |
| Screen navigation | 30-60 seconds |
| Data loading | 60-120 seconds |
| AI generation | 3-5 minutes |
| Video processing | 5-10 minutes |

```kotlin
// Quick smoke test
test("Verify home loads") {
    timeout = 30.seconds
    // ...
}

// AI image generation
test("Generate AI avatar") {
    timeout = 5.minutes  // Generation takes time
    // ...
}
```

---

## Rule 5: Capture Screenshots at Key Points

Screenshots help debug failures and document test execution.

```kotlin
test("Complete purchase") {
    step("Add item to cart") {
        tap(ProductScreen.ADD_TO_CART)
        captureScreenshot("item_added")  // Before proceeding
    }

    step("Checkout") {
        tap(CartScreen.CHECKOUT)
        waitForScreen("Payment screen")
        captureScreenshot("payment_screen")  // Key verification point
    }

    step("Confirm purchase") {
        tap(PaymentScreen.PAY_BUTTON)
        waitFor("Success", "Order confirmed")
        captureScreenshot("purchase_complete")  // Final result
    }
}
```

**When to capture:**
- End of each test (for documentation)
- Before/after critical actions
- When verifying important screens
- On test failure (automatic)

---

## Rule 6: Use Descriptive Step Names

Step names appear in test reports. Make them meaningful.

```kotlin
// Bad - unclear what's happening
step("Step 1") { ... }
step("Step 2") { ... }
step("Step 3") { ... }

// Good - clear intent
step("Open login screen") { ... }
step("Enter user credentials") { ... }
step("Submit and verify success") { ... }
```

---

## Rule 7: Structure Tests with Setup/Action/Verify

Each test should follow this pattern:

```kotlin
test("Feature works correctly") {
    // SETUP - Get to the right state
    step("Navigate to feature") {
        tap(HomeScreen.FEATURE_BUTTON)
        waitFor(FeatureScreen.MAIN_CONTENT)
    }

    // ACTION - Do the thing being tested
    step("Perform the action") {
        tap(FeatureScreen.ACTION_BUTTON)
        type("input data")
        tap(FeatureScreen.SUBMIT)
    }

    // VERIFY - Check the result
    step("Verify result") {
        waitForScreen("Success state")
        verifyScreen(FeatureScreen.SUCCESS_EXPECTATION)
        captureScreenshot("test_result")
    }
}
```

---

## Rule 8: Use beforeAll/afterAll for Suite Setup

Don't repeat setup in every test.

```kotlin
val featureTestSuite = testSuite("Feature Tests") {

    // Runs ONCE before all tests
    beforeAll {
        terminateApp()
        launchApp()
        wait(3.seconds)

        // Complete onboarding if needed
        ifPresent("Continue") {
            completeOnboarding()
        }
    }

    // Runs ONCE after all tests
    afterAll {
        terminateApp()
    }

    test("Test 1") { ... }
    test("Test 2") { ... }
    test("Test 3") { ... }
}
```

---

## Rule 9: Tag Tests for Filtering

Use tags to categorize and filter tests.

```kotlin
test("Critical login flow") {
    tags = listOf("smoke", "critical", "auth")
    // ...
}

test("Edge case handling") {
    tags = listOf("regression", "edge-case")
    // ...
}
```

**Suggested tags:**
- `smoke` - Quick verification tests
- `critical` - Must-pass tests
- `regression` - Full coverage tests
- Feature names: `auth`, `checkout`, `profile`

---

## Rule 10: Extract Reusable Helpers

Common flows should be helper functions.

```kotlin
// helpers/AuthHelpers.kt
suspend fun TestContext.login(email: String, password: String) {
    tap(LoginScreen.EMAIL_FIELD)
    type(email)
    tap(LoginScreen.PASSWORD_FIELD)
    type(password)
    tap(LoginScreen.LOGIN_BUTTON)
    waitForScreen("Home screen", timeout = 10.seconds)
}

suspend fun TestContext.logout() {
    tap(HomeScreen.PROFILE)
    tap(ProfileScreen.LOGOUT)
    waitFor(LoginScreen.LOGIN_BUTTON)
}

// Use in tests
test("User can view profile") {
    step("Login") {
        login("user@example.com", "password")
    }

    step("View profile") {
        tap(HomeScreen.PROFILE)
        verifyScreen(ProfileScreen.EXPECTATION)
    }

    step("Logout") {
        logout()
    }
}
```

---

## Checklist Before Running Tests

- [ ] Screen constants match actual UI text
- [ ] Timeouts are appropriate for operations
- [ ] Optional elements handled with `ifPresent`
- [ ] Screenshots at key verification points
- [ ] Step names are descriptive
- [ ] Device is connected and app is installed
