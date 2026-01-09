# MCP Mobile Testing Framework

**YAML-based mobile UI tests** powered by **Claude Code** and **mobile-mcp**.

Zero dependencies. Zero compilation. Just YAML.

## Quick Start

### 1. Create a test file

```yaml
# tests/login.test.yaml
config:
  app: com.myapp.android

setup:
  - launch_app
  - wait: 3s

tests:
  - name: User can login
    steps:
      - tap: "Email"
      - type: "user@example.com"
      - tap: "Password"
      - type: "secret123"
      - tap: "Login"
      - wait_for: "Welcome"
      - verify_screen: "Home screen after successful login"
```

### 2. Run with Claude Code

```
Run the test file tests/login.test.yaml
```

That's it. No setup. No dependencies.

## Why YAML?

| Kotlin DSL | YAML |
|------------|------|
| Requires JDK, Gradle | Just a text file |
| Needs compilation | Edit and run |
| Android devs only | Anyone can write |
| Complex setup | Zero setup |

## Features

- **Zero Setup** - No JDK, Gradle, or dependencies required
- **AI-Powered Verification** - Claude analyzes screenshots against natural language expectations
- **Natural Language Selectors** - Find elements by description, not brittle IDs
- **Cross-Platform** - Works with Android devices and iOS simulators

## Schema Reference

See [docs/SCHEMA.md](docs/SCHEMA.md) for complete action reference including:

- Basic actions: `tap`, `type`, `swipe`, `press`
- App control: `launch_app`, `terminate_app`, `set_orientation`
- Assertions: `verify_screen`, `verify_contains`, `verify_no_element`
- Flow control: `wait_for`, `if_present`, `retry`, `repeat`
- Screenshots: `screenshot`, `save_screenshot`

## Examples

See [examples/](examples/) for complete test examples:

- `simple.test.yaml` - Basic test structure
- `onboarding.test.yaml` - Complex flow with conditionals and loops

## Templates

Copy-paste templates in [templates/](templates/):

- `basic.test.yaml` - Minimal test template
- `full.test.yaml` - All features demonstrated

## How It Works

```
your-test.yaml → Claude Code → mobile-mcp → Device
                 (parses)      (executes)
```

1. You write tests in YAML
2. Claude Code reads and interprets each action
3. Actions execute via mobile-mcp tools on your device
4. AI analyzes screenshots for verification
5. Results reported with pass/fail details

## Requirements

- **Claude Code CLI** with mobile-mcp configured
- **Device**: Android (via adb) or iOS simulator
- That's it!

## License

MIT
