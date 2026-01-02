# FireAndForget

<div>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Badge iOS" />
  <img src="https://img.shields.io/badge/Platform-JVM-8A2BE2.svg?logo=openjdk" alt="Badge JVM" />
  <img src="https://img.shields.io/badge/Platform-WASM%20%2F%20JS-yellow.svg?logo=javascript" alt="Badge wasm/JS" />
</div>

[![Build](https://github.com/alorma/FireAndForget/actions/workflows/main.yml/badge.svg)](https://github.com/alorma/FireAndForget/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.alorma.fireandforget/core.svg)](https://central.sonatype.com/namespace/com.alorma.fireandforget)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-blue.svg?logo=kotlin)

<a href="https://www.buymeacoffee.com/alorma" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>

A lightweight Kotlin Multiplatform library for Compose Multiplatform that helps you execute code once on first access, with flexible state persistence options.

## Overview

FireAndForget is a simple yet powerful utility for managing one-time operations in your Kotlin Multiplatform applications. It's perfect for scenarios like:

- 🎓 First-time user onboarding flows
- 📢 One-time feature announcements
- ⚙️ Initial setup operations
- 📚 Tutorial or walkthrough displays
- 🚀 Feature flag management with automatic reset

## Features

- ✅ **Execute Once**: Code runs only on first access
- 🔄 **Flexible State Management**: Choose your own persistence strategy (in-memory, shared preferences, data store, etc.)
- 🎯 **Simple API**: Three intuitive methods for complete control
- 🌐 **True Multiplatform**: Works on Android (API 21+), iOS, Web (JS & WASM), Desktop (JVM)
- 🧩 **Compose-Friendly**: Designed for seamless integration with Compose Multiplatform
- 🏃 **Runner Pattern**: Delegates state persistence to customizable runner implementations

## Installation

Add the dependency to your `commonMain` source set:

```kotlin
kotlin {
  sourceSets {
    commonMain.dependencies {
      // Core library - required
      implementation("com.alorma.fireandforget:core:$version")

      // Optional: multiplatform-settings implementation
      implementation("com.alorma.fireandforget:multiplatform-settings:$version")
    }
  }
}
```

Check the latest version on [Maven Central](https://central.sonatype.com/namespace/com.alorma.fireandforget).

## Architecture

FireAndForget uses a **Runner Pattern** that separates the flag logic from state persistence:

### Core Components

1. **FireAndForget** (Abstract Class)
   - Represents a one-time flag with a unique name
   - Requires a `FireAndForgetRunner` implementation for state persistence
   - Each instance needs a unique `name` to identify its state

2. **FireAndForgetRunner** (Abstract Class)
   - Defines the persistence contract with three methods:
     - `isEnabled()`: Check if the flag should execute
     - `disable()`: Mark the flag as executed
     - `reset()`: Reset the flag to allow re-execution

This pattern allows you to choose or create your own state persistence strategy.

## Usage

### Step 1: Create a Concrete Implementation

First, create a concrete class that extends `FireAndForget`:

```kotlin
class OnboardingFlag(
  runner: FireAndForgetRunner,
) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "user_onboarding",
  defaultValue = true // Default: enabled (will execute)
)
```

### Step 2: Choose or Create a Runner

#### Option A: Use the multiplatform-settings Runner (Recommended)

The library provides a ready-to-use implementation using [russhwolf/multiplatform-settings](https://github.com/russhwolf/multiplatform-settings):

```kotlin
import com.alorma.fireandforget.mukltiplatform.settings.SettingsFireAndForgetRunner
import com.russhwolf.settings.Settings

val settings = Settings()
val runner = SettingsFireAndForgetRunner(settings)
val onboardingFlag = OnboardingFlag(runner)
```

This persists state across app restarts using platform-specific storage:
- **Android**: SharedPreferences
- **iOS**: NSUserDefaults
- **JVM**: java.util.prefs.Preferences
- **JS**: localStorage

#### Option B: In-Memory Runner (State lost on restart)

For temporary state that doesn't need to persist:

```kotlin
class InMemoryRunner : FireAndForgetRunner() {
  private val map = mutableMapOf<String, Boolean>()

  override fun isEnabled(fireAndForget: FireAndForget): Boolean {
    return map[fireAndForget.name] ?: fireAndForget.defaultValue
  }

  override fun disable(fireAndForget: FireAndForget) {
    map[fireAndForget.name] = false
  }

  override fun reset(fireAndForget: FireAndForget) {
    map.remove(fireAndForget.name)
  }
}
```

#### Option C: Custom Runner

Implement `FireAndForgetRunner` with your preferred storage solution (Room, DataStore, SQLDelight, etc.):

```kotlin
class DataStoreRunner(
  private val dataStore: DataStore<Preferences>
) : FireAndForgetRunner() {
  override fun isEnabled(fireAndForget: FireAndForget): Boolean {
    // Your DataStore implementation
  }

  override fun disable(fireAndForget: FireAndForget) {
    // Your DataStore implementation
  }

  override fun reset(fireAndForget: FireAndForget) {
    // Your DataStore implementation
  }
}
```

### Step 3: Use in Your Compose Code

```kotlin
@Composable
fun App() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val onboarding = OnboardingFlag(runner)

  if (onboarding.isEnabled()) {
    // This will only show once
    OnboardingScreen(
      onComplete = {
        onboarding.disable() // Mark as completed
      }
    )
  } else {
    MainScreen()
  }
}
```

## API Reference

### FireAndForget Class

```kotlin
abstract class FireAndForget(
  val fireAndForgetRunner: FireAndForgetRunner,
  val name: String,
  val defaultValue: Boolean = true,
)
```

#### Constructor Parameters

- `fireAndForgetRunner`: The runner implementation that handles state persistence
- `name`: Unique identifier for this flag (used as storage key)
- `defaultValue`: Initial state (default: `true` = enabled)

#### Methods

- `isEnabled(): Boolean` - Returns `true` if the code should execute
- `disable()` - Marks the flag as executed (disables it)
- `reset()` - Resets the flag back to `defaultValue` (allows re-execution)

### FireAndForgetRunner Abstract Class

```kotlin
abstract class FireAndForgetRunner {
  abstract fun isEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
}
```

## Usage Examples

### Basic One-Time Execution

```kotlin
class WelcomeMessage(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "welcome_message"
)

@Composable
fun HomeScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val welcomeFlag = remember { WelcomeMessage(runner) }

  if (welcomeFlag.isEnabled()) {
    WelcomeDialog(
      onDismiss = { welcomeFlag.disable() }
    )
  }
}
```

### Feature Announcement

```kotlin
class NewFeatureAnnouncement(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "feature_announcement_v2"
)

@Composable
fun MainScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val announcement = remember { NewFeatureAnnouncement(runner) }

  LaunchedEffect(Unit) {
    if (announcement.isEnabled()) {
      // Show announcement
      showSnackbar("Check out our new feature!")
      announcement.disable()
    }
  }
}
```

### Tutorial with Reset Option

```kotlin
class AppTutorial(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "app_tutorial"
)

@Composable
fun SettingsScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val tutorial = remember { AppTutorial(runner) }

  Button(
    onClick = {
      tutorial.reset() // Allow tutorial to run again
      navigateToTutorial()
    }
  ) {
    Text("Restart Tutorial")
  }
}
```

### Multiple Flags with Shared Runner

```kotlin
@Composable
fun App() {
  val runner = remember { SettingsFireAndForgetRunner(Settings()) }

  // Multiple flags can share the same runner
  val onboarding = remember { OnboardingFlag(runner) }
  val tutorial = remember { TutorialFlag(runner) }
  val featureAnnouncement = remember { FeatureAnnouncementFlag(runner) }

  when {
    onboarding.isEnabled() -> OnboardingScreen { onboarding.disable() }
    tutorial.isEnabled() -> TutorialScreen { tutorial.disable() }
    else -> MainScreen()
  }
}
```

### Non-Compose Usage

FireAndForget works outside of Compose too:

```kotlin
class FirstRunSetup(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "first_run_setup"
)

fun initializeApp() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val setup = FirstRunSetup(runner)

  if (setup.isEnabled()) {
    // Perform first-run initialization
    initializeDatabase()
    downloadInitialData()
    setup.disable()
  }
}
```

## Project Structure

This repository contains:

- **core** - The main FireAndForget library implementation
- **multiplatform-settings** - A ready-to-use runner implementation using multiplatform-settings
- **samples/shared** - Shared Compose UI code demonstrating library usage
- **samples/androidApp** - Android sample application
- **samples/desktopApp** - Desktop (JVM) sample application
- **build-logic** - Gradle convention plugins for build configuration

## Building the Project

### Build Library Modules

```bash
# Build core library
./gradlew :core:build

# Build multiplatform-settings runner
./gradlew :multiplatform-settings:build

# Build everything
./gradlew build
```

### Run Sample Applications

```bash
# Android sample
./gradlew :samples:androidApp:assembleDebug

# Desktop sample
./gradlew :samples:desktopApp:run
```

### Run Tests

```bash
# Run all tests across all platforms
./gradlew allTests

# Run platform-specific tests
./gradlew jvmTest
./gradlew jsTest
./gradlew iosSimulatorArm64Test
```

## Requirements

- **Kotlin**: 2.3.0+
- **Android**: API 21+ (Android 5.0 Lollipop)
- **iOS**: 13.0+
- **JVM**: 17+
- **Compose Multiplatform**: 1.9.3+

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Learn More

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings)
