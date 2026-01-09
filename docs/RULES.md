# MCP Testing Rules & Best Practices

Follow these rules to write reliable, maintainable YAML tests.

---

## Rule 1: Add Waits After Actions

UI needs time to respond. Always add waits after taps.

```yaml
# Bad - may fail if UI hasn't updated
- tap: "Submit"
- tap: "Next"

# Good - gives UI time to respond
- tap: "Submit"
- wait: 1s
- tap: "Next"

# Better - wait for specific element
- tap: "Submit"
- wait_for: "Next"
- tap: "Next"
```

**Recommended waits:**
- After tap: `wait: 500ms` to `wait: 1s`
- After screen transition: `wait_for: "element"`
- After data load: `wait_for_screen: "Data loaded"`

---

## Rule 2: Handle Optional Elements

Dialogs, popups, and banners may or may not appear. Use `if_present`.

```yaml
# Bad - fails if popup not shown
- tap: "Allow"

# Good - only taps if visible
- if_present: "Allow"
  then:
    - tap: "Allow"

# Good - handle multiple optional elements
- if_present: "Skip"
  then:
    - tap: "Skip"
- if_present: "Not now"
  then:
    - tap: "Not now"
- if_present: "Allow"
  then:
    - tap: "Allow"
```

---

## Rule 3: Set Appropriate Timeouts

Different operations need different timeouts.

| Operation | Timeout |
|-----------|---------|
| Quick UI check | 10-30 seconds |
| Screen navigation | 30-60 seconds |
| Data loading | 60-120 seconds |
| AI generation | 3-5 minutes |
| Video processing | 5-10 minutes |

```yaml
# Quick smoke test
- name: Verify home loads
  timeout: 30s
  steps:
    - verify_screen: "Home screen visible"

# AI image generation
- name: Generate AI avatar
  timeout: 5m  # Generation takes time
  steps:
    - tap: "Generate"
    - wait_for_screen: "Avatar generated"
```

---

## Rule 4: Capture Screenshots at Key Points

Screenshots help debug failures and document test execution.

```yaml
- name: Complete purchase
  steps:
    # Before proceeding
    - tap: "Add to Cart"
    - screenshot: item_added

    # Key verification point
    - tap: "Checkout"
    - wait_for_screen: "Payment screen"
    - screenshot: payment_screen

    # Final result
    - tap: "Pay"
    - wait_for: "Order confirmed"
    - screenshot: purchase_complete
```

**When to capture:**
- End of each test (for documentation)
- Before/after critical actions
- When verifying important screens

---

## Rule 5: Use Descriptive Test Names

Test names appear in reports. Make them meaningful.

```yaml
# Bad - unclear what's happening
tests:
  - name: Test 1
  - name: Test 2

# Good - clear intent
tests:
  - name: User can login with valid credentials
  - name: Invalid password shows error message
```

---

## Rule 6: Structure Tests with Setup/Action/Verify

Each test should follow this pattern:

```yaml
- name: Feature works correctly
  steps:
    # SETUP - Get to the right state
    - tap: "Feature"
    - wait_for: "Feature Screen"

    # ACTION - Do the thing being tested
    - tap: "Action Button"
    - type: "input data"
    - tap: "Submit"

    # VERIFY - Check the result
    - wait_for_screen: "Success state"
    - verify_screen: "Expected result shown"
    - screenshot: test_result
```

---

## Rule 7: Use setup/teardown for Suite Setup

Don't repeat setup in every test.

```yaml
config:
  app: com.myapp

# Runs ONCE before all tests
setup:
  - terminate_app
  - launch_app
  - wait: 3s
  # Handle onboarding
  - if_present: "Continue"
    then:
      - tap: "Continue"

# Runs ONCE after all tests
teardown:
  - terminate_app

tests:
  - name: Test 1
    steps: [...]
  - name: Test 2
    steps: [...]
```

---

## Rule 8: Tag Tests for Filtering

Use tags to categorize and filter tests.

```yaml
tests:
  - name: Critical login flow
    tags: [smoke, critical, auth]
    steps: [...]

  - name: Edge case handling
    tags: [regression, edge-case]
    steps: [...]
```

**Suggested tags:**
- `smoke` - Quick verification tests
- `critical` - Must-pass tests
- `regression` - Full coverage tests
- Feature names: `auth`, `checkout`, `profile`

---

## Rule 9: Use Descriptive Verifications

Be specific about what you expect to see.

```yaml
# Bad - too vague
- verify_screen: "Screen looks good"

# Good - specific elements
- verify_screen: "Login form with email field, password field, and blue Login button"

# Good - with strictness for flexible matching
- verify_screen:
    expectation: "Home screen with navigation tabs"
    strictness: lenient
```

---

## Rule 10: Keep Tests Independent

Each test should work regardless of order.

```yaml
# Bad - Test 2 depends on Test 1
tests:
  - name: Login
    steps:
      - tap: "Login"
      - type: "user@example.com"
      # Stays logged in...

  - name: View Profile
    steps:
      # Assumes already logged in!
      - tap: "Profile"

# Good - Each test is self-contained
tests:
  - name: Login
    steps:
      - terminate_app
      - launch_app
      - wait: 3s
      - tap: "Login"
      - type: "user@example.com"

  - name: View Profile
    steps:
      - terminate_app
      - launch_app
      - wait: 3s
      # Login first
      - tap: "Login"
      - type: "user@example.com"
      # Then view profile
      - tap: "Profile"
```

---

## Checklist Before Running Tests

- [ ] Test names are descriptive
- [ ] Timeouts are appropriate for operations
- [ ] Optional elements handled with `if_present`
- [ ] Screenshots at key verification points
- [ ] Waits after actions that trigger UI changes
- [ ] Device is connected and app is installed
