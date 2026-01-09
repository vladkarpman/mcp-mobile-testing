# How to Create MCP UI Tests

Step-by-step guide to writing YAML-based mobile tests.

## Prerequisites

- Device connected (`adb devices` for Android)
- App installed on device
- Claude Code CLI running with mobile-mcp

## Step A: Create Test Config

**What:** Define which app to test.

```yaml
config:
  app: com.your.app.package    # Your app's package name
  device: emulator-5554         # Optional: auto-detects if omitted
```

**Expected Result:** Claude Code knows which app to launch/terminate.

---

## Step B: Add Setup & Teardown

**What:** Actions to run before/after all tests.

```yaml
setup:
  - terminate_app              # Kill app if running
  - launch_app                 # Start fresh
  - wait: 3s                   # Wait for app to load

teardown:
  - terminate_app              # Clean up after tests
```

**Expected Result:** Each test run starts with clean app state.

---

## Step C: Write Tests

**What:** Define test cases with steps.

```yaml
tests:
  - name: User can login
    timeout: 60s
    tags: [smoke, auth]
    steps:
      - tap: "Email"
      - type: "user@example.com"
      - tap: "Password"
      - type: "secret123"
      - tap: "Login"
      - wait_for: "Welcome"
      - verify_screen: "Home screen after login"
      - screenshot: login_success
```

**Expected Result:** A complete test file Claude Code can execute.

---

## Step D: Run the Test

**What:** Ask Claude Code to run your test.

```
Run the test file tests/login.test.yaml
```

**Expected Result:**
- Claude executes each step
- Screenshots captured
- Pass/Fail result reported

---

## Complete Example

```yaml
# tests/mcp/onboarding.test.yaml

config:
  app: com.myapp.android

setup:
  - terminate_app
  - launch_app
  - wait: 3s

teardown:
  - terminate_app

tests:
  - name: Complete onboarding
    timeout: 120s
    tags: [smoke, onboarding]
    steps:
      - verify_screen: "Welcome screen with Get Started button"

      - tap: "Get Started"
      - wait: 1s

      - tap: "Next"
      - wait: 1s

      - tap: "Next"
      - wait: 1s

      - tap: "Done"
      - wait_for: "Home"

      - verify_screen: "Home screen with main content"
      - screenshot: onboarding_complete

  - name: Skip onboarding
    steps:
      - if_present: "Skip"
        then:
          - tap: "Skip"
          - wait: 2s

      - verify_screen:
          expectation: "Home screen or onboarding"
          strictness: lenient
```

---

## Quick Reference

### Actions
| Action | Example |
|--------|---------|
| Tap element | `- tap: "Continue"` |
| Tap coordinates | `- tap: [500, 1000]` |
| Type text | `- type: "hello"` |
| Swipe | `- swipe: up` |
| Press button | `- press: back` |
| Wait | `- wait: 2s` |
| Launch app | `- launch_app` |
| Close app | `- terminate_app` |

### Assertions
| Assertion | Example |
|-----------|---------|
| Verify screen | `- verify_screen: "Home with 4 tabs"` |
| Check elements | `- verify_contains: ["Home", "Settings"]` |
| Check absence | `- verify_no_element: "Error"` |

### Flow Control
| Control | Example |
|---------|---------|
| Wait for element | `- wait_for: "Continue"` |
| Wait with timeout | `- wait_for: {element: "Done", timeout: 10s}` |
| Conditional | `- if_present: "Skip"` + `then: [...]` |
| Retry | `- retry: {attempts: 3, steps: [...]}` |
| Repeat | `- repeat: {times: 5, steps: [...]}` |

---

## Tips

### Use Descriptive Verifications

```yaml
# Bad - too vague
- verify_screen: "Screen looks good"

# Good - specific
- verify_screen: "Login form with email field, password field, and Login button"
```

### Handle Optional Elements

```yaml
# Popup might or might not appear
- if_present: "Rate this app"
  then:
    - tap: "Not now"
```

### Add Waits After Actions

```yaml
- tap: "Submit"
- wait: 1s              # Give UI time to respond
- verify_screen: "..."
```

### Use Appropriate Timeouts

```yaml
# Quick UI action
- name: Tap button
  timeout: 30s

# AI generation feature
- name: Generate avatar
  timeout: 5m
```

### Capture Screenshots at Key Points

```yaml
- screenshot: before_action
- tap: "Important button"
- wait: 2s
- screenshot: after_action
```

---

## Next Steps

- See [SCHEMA.md](SCHEMA.md) for all available actions
- Copy templates from [templates/](../templates/)
- Check [examples/](../examples/) for more patterns
