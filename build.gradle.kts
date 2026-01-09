// MCP Mobile UI Testing Framework
// https://github.com/vladkarpman/mcp-mobile-testing

plugins {
    kotlin("jvm") version "2.1.0" apply false
}

allprojects {
    group = "com.github.vladkarpman"
    version = "1.0.0"

    repositories {
        mavenCentral()
        google()
    }
}
