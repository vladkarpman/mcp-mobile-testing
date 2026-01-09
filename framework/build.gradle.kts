plugins {
    kotlin("jvm") version "2.1.0"
    `maven-publish`
}

group = "com.github.vladkarpman"
version = "1.0.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

kotlin {
    jvmToolchain(17)
}

// Publishing configuration for JitPack
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])

            groupId = "com.github.vladkarpman"
            artifactId = "mcp-mobile-testing"
            version = project.version.toString()

            pom {
                name.set("MCP Mobile Testing Framework")
                description.set("Kotlin DSL for mobile UI testing with Claude Code and mobile-mcp")
                url.set("https://github.com/vladkarpman/mcp-mobile-testing")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
            }
        }
    }
}

// Required for JitPack
tasks.register("sourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}
