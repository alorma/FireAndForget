# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

FireAndForget is a lightweight Kotlin Multiplatform library for Compose Multiplatform that executes code once on first access, with optional auto-toggling behavior. The library is designed for scenarios like first-time user onboarding, one-time feature announcements, and tutorial displays.

## Module Structure

This is a multi-module Kotlin Multiplatform project organized as follows:

- **core** - The main FireAndForget library implementation. Contains the core `FireAndForget` abstract class and `FireAndForgetRunner` interface.
- **multiplatform-settings** - A `FireAndForgetRunner` implementation using the `multiplatform-settings` library for persistence across platforms.
- **samples/shared** - Shared Compose UI code for sample applications.
- **samples/androidApp** - Android sample application.
- **samples/desktopApp** - Desktop (JVM) sample application.
- **build-logic** - Gradle convention plugins for consistent build configuration across modules.

## Build Commands

### Build Library Modules

```bash
# Build all library modules
./gradlew :core:build
./gradlew :multiplatform-settings:build

# Build everything
./gradlew build
```

### Build and Run Sample Applications

```bash
# Android (debug build)
./gradlew :samples:androidApp:assembleDebug

# Desktop application
./gradlew :samples:desktopApp:build
./gradlew :samples:desktopApp:run
```

### Testing

```bash
# Run all tests across all platforms
./gradlew allTests

# Run tests for specific platforms
./gradlew jvmTest                    # JVM tests
./gradlew jsTest                     # JavaScript tests
./gradlew iosSimulatorArm64Test      # iOS Simulator tests
./gradlew iosX64Test                 # iOS x64 tests

# Run Android instrumentation tests
./gradlew connectedDebugAndroidTest  # On connected devices
```

### Verification

```bash
# Run all verification tasks (tests + lint)
./gradlew check

# Lint checks
./gradlew lint
./gradlew lintDebug
```

### Publishing

```bash
# Publish to Maven Local
./gradlew publishToMavenLocal

# Publish to Maven Central (requires credentials)
./gradlew publish
```

## Architecture and Design

### Core Architecture

The library follows a **Runner pattern** where:

1. **FireAndForget** (core/src/commonMain/kotlin/com/alorma/fireandforget/FireAndForget.kt:3) - Abstract class representing a one-time flag with a unique name. Each instance requires:
   - `fireAndForgetRunner`: The implementation that handles state persistence
   - `name`: A unique identifier for this flag
   - `defaultValue`: Initial state (default: true means enabled)

2. **FireAndForgetRunner** (core/src/commonMain/kotlin/com/alorma/fireandforget/FireAndForgetRunner.kt:3) - Abstract class defining the persistence contract:
   - `isEnabled()`: Check if the flag should execute
   - `disable()`: Mark the flag as executed
   - `reset()`: Reset the flag to allow re-execution

### Implementation Pattern

To use FireAndForget, you must:
1. Create a concrete implementation of `FireAndForget` with your specific runner
2. Pass the runner instance, unique name, and optional default value
3. Call `isEnabled()` to check if code should execute
4. Call `disable()` after execution (or have your runner handle this automatically)

Example from the codebase (samples/shared):
- `SettingsFireAndForgetRunner` uses the multiplatform-settings library to persist state across app restarts
- `InMemoryFireAndForgetRunner` stores state in memory only (lost on app restart)

### Platform Targets

The library supports all Kotlin Multiplatform targets (configured in build-logic/convention/src/main/kotlin/FireAndForgetLibraryConventionPlugin.kt):
- **Android**: API 21+ (minSdk), compiled against API 36
- **JVM**: Desktop applications (JVM 17)
- **iOS**: iosX64, iosArm64, iosSimulatorArm64
- **JavaScript**: Browser (IR backend)
- **WASM**: wasmJs with browser support

### Convention Plugin System

The project uses Gradle convention plugins (build-logic/convention) to standardize configuration:

- **fireAndForget.library**: Custom plugin that applies to all library modules. It:
  - Configures Kotlin Multiplatform with all supported targets
  - Sets up Android library configuration with required SDK versions
  - Configures Maven publishing with signing
  - **Enforces unique namespaces**: Each module MUST override the default namespace in its build.gradle.kts or the build will fail

When adding a new library module, you must:
```kotlin
kotlin {
  androidLibrary {
    namespace = libs.versions.namespace.get() + ".your.unique.suffix"
  }
}
```

### Dependency Management

Dependencies are managed via Gradle version catalogs (gradle/libs.versions.toml):
- Library versions are centralized
- Shared namespace prefix: `com.alorma.fireandforget`
- Use type-safe project accessors (e.g., `projects.core`)

## Key Implementation Details

- The library is state-management agnostic - you provide your own `FireAndForgetRunner` implementation
- State persistence is delegated to the runner, allowing different strategies (in-memory, shared preferences, data store, etc.)
- The `name` parameter is critical - it's used as the key for persisting state
- Each `FireAndForget` instance represents a unique flag/feature
