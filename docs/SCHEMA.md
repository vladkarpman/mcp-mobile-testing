# MCP Test YAML Schema

Complete reference for writing YAML-based mobile UI tests.

## File Structure

```yaml
# Required: Test file metadata
config:
  app: <package_name>           # Required: Android package or iOS bundle ID
  device: <device_id>           # Optional: auto-detect if omitted

# Optional: Runs once before all tests
setup:
  - <action>

# Optional: Runs once after all tests
teardown:
  - <action>

# Required: List of test cases
tests:
  - name: <test_name>           # Required
    description: <text>         # Optional
    timeout: <duration>         # Optional: default 60s
    tags: [tag1, tag2]          # Optional
    steps:
      - <action>
```

## Actions

### Basic Actions

```yaml
# Tap element by description
- tap: "Button text"

# Tap by coordinates
- tap: [100, 200]

# Double tap
- double_tap: "Element"

# Long press (500ms default)
- long_press: "Element"

# Long press with custom duration
- long_press:
    element: "Element"
    duration: 1000              # Duration in ms

# Type text into focused field
- type: "Hello world"

# Type with submit (press enter after)
- type:
    text: "Hello"
    submit: true

# Swipe direction: up/down/left/right
- swipe: up

# Swipe with custom distance
- swipe:
    direction: up
    distance: 500               # Distance in pixels

# Press device button: back/home/volume_up/volume_down/enter
- press: back

# Wait for duration
- wait: 2s                      # Supports: Ns, Nms, Nm
```

### App Control

```yaml
# Launch configured app
- launch_app

# Launch specific app
- launch_app: "com.other.app"

# Stop configured app
- terminate_app

# Stop specific app
- terminate_app: "com.other.app"

# Open URL in browser
- open_url: "https://example.com"

# Set orientation: portrait/landscape
- set_orientation: landscape
```

### Assertions

```yaml
# AI-powered screen verification
- verify_screen: "Description of expected screen state"

# Screen verification with strictness
- verify_screen:
    expectation: "Screen description"
    strictness: lenient         # strict/normal/lenient

# Verify multiple elements present
- verify_contains:
    - "Element 1"
    - "Element 2"

# Verify element is NOT present
- verify_no_element: "Element that should not exist"
```

### Flow Control

```yaml
# Wait for element to appear
- wait_for: "Element"

# Wait for element with timeout
- wait_for:
    element: "Element"
    timeout: 10s

# Wait for any of multiple elements
- wait_for_any:
    - "Element A"
    - "Element B"

# Wait for screen state
- wait_for_screen: "Expected state"

# Conditional: execute if element present
- if_present: "Element"
  then:
    - tap: "Element"
    - wait: 1s

# Retry on failure
- retry:
    attempts: 3
    delay: 1s
    steps:
      - tap: "Flaky button"

# Repeat actions N times
- repeat:
    times: 3
    steps:
      - swipe: up
      - wait: 500ms
```

### Screenshots

```yaml
# Capture screenshot with name
- screenshot: "login_success"

# Save screenshot to specific path
- save_screenshot: "/path/to/file.png"
```

## Duration Format

- `5s` - 5 seconds
- `500ms` - 500 milliseconds
- `2m` - 2 minutes

## Complete Example

```yaml
config:
  app: com.example.myapp

setup:
  - terminate_app
  - launch_app
  - wait: 3s

teardown:
  - terminate_app

tests:
  - name: User can login successfully
    description: Verify login flow with valid credentials
    timeout: 60s
    tags: [smoke, auth]
    steps:
      - tap: "Email"
      - type: "user@example.com"
      - tap: "Password"
      - type: "secret123"
      - tap: "Login"
      - wait_for:
          element: "Welcome"
          timeout: 10s
      - verify_screen: "Home screen after successful login"
      - screenshot: login_success

  - name: Handle optional popup
    steps:
      - if_present: "Rate this app"
        then:
          - tap: "Not now"
      - verify_screen: "Main content visible"
```

## File Naming Convention

- Use `.test.yaml` extension: `login.test.yaml`, `onboarding.test.yaml`
- Place in `tests/` or `tests/mcp/` directory
- Use descriptive names: `user-registration.test.yaml`, `checkout-flow.test.yaml`
