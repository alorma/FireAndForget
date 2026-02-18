# FireAndForget

<div>
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Badge Android" />
  <img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Badge iOS" />
  <img src="https://img.shields.io/badge/Platform-JVM-8A2BE2.svg?logo=openjdk" alt="Badge JVM" />
  <img src="https://img.shields.io/badge/Platform-WASM%20%2F%20JS-yellow.svg?logo=javascript" alt="Badge wasm/JS" />
</div>

[![Build](https://github.com/alorma/FireAndForget/actions/workflows/main.yml/badge.svg)](https://github.com/alorma/FireAndForget/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.alorma.fire-and-forget/core.svg)](https://central.sonatype.com/namespace/com.github.alorma.fire-and-forget)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-blue.svg?logo=kotlin)

<a href="https://www.buymeacoffee.com/alorma" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-orange.png" alt="Buy Me A Coffee" height="41" width="174"></a>

A lightweight Kotlin Multiplatform library that helps you execute code once on first access, with flexible state persistence options. Use it in your Kotlin Multiplatform projects or natively in Android, iOS, JVM, or JavaScript applications. Now includes `CounterFireAndForget` for executing code a fixed number of times!

## Overview

FireAndForget is a simple yet powerful utility for managing one-time operations in your applications. Whether you're building a Kotlin Multiplatform project or a native Android/iOS/JVM/JS app, FireAndForget provides a consistent API for one-time executions. It's perfect for scenarios like:

- 🎓 First-time user onboarding flows
- 📢 One-time feature announcements
- 🔢 Limited-time feature announcements (show N times with `CounterFireAndForget`)
- ⚙️ Initial setup operations
- 📚 Tutorial or walkthrough displays
- 🚀 Feature flag management with automatic reset

## Features

- ✅ **Execute Once**: Code runs only on first access
- 🔢 **Counter-Based Execution**: Execute code N times before disabling with `CounterFireAndForget`
- 🔄 **Flexible State Management**: Choose your own persistence strategy (in-memory, shared preferences, data store, etc.)
- 🎯 **Simple API**: Three intuitive methods for complete control
- 🌐 **True Multiplatform**: Works on Android (API 21+), iOS, Web (JS & WASM), Desktop (JVM)
- 📱 **Use Anywhere**: Kotlin Multiplatform projects or native platform-specific code (Android, iOS, JVM, JS)
- 🏃 **Runner Pattern**: Delegates state persistence to customizable runner implementations

## Installation

### Kotlin Multiplatform

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

### Native Android (Gradle)

Add the dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
  // Core library - required
  implementation("com.alorma.fireandforget:core:$version")

  // Optional: multiplatform-settings implementation
  implementation("com.alorma.fireandforget:multiplatform-settings:$version")
}
```

### iOS (CocoaPods/SPM)

The library can be consumed as a Kotlin/Native framework in your iOS project. Add it to your shared Kotlin module and export it to iOS.

### JVM / Desktop

```kotlin
dependencies {
  implementation("com.alorma.fireandforget:core:$version")
  implementation("com.alorma.fireandforget:multiplatform-settings:$version")
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

2. **CounterFireAndForget** (Abstract Class, extends FireAndForget)
   - Executes code a fixed number of times before disabling
   - Accepts a `counter` parameter for the number of allowed executions
   - Counter decrements on each `isEnabled()` call
   - Perfect for limited-time features or trial functionality

3. **FireAndForgetRunner** (Abstract Class)
   - Defines the persistence contract with three methods:
     - `isEnabled()`: Check if the flag should execute
     - `disable()`: Mark the flag as executed
     - `reset()`: Reset the flag to allow re-execution
   - Also provides counter methods for `CounterFireAndForget`:
     - `isEnabledCounter()`: Check and decrement counter
     - `getCounter()`: Retrieve current counter value
     - `setCounter()`: Store counter value
     - `resetCounter()`: Reset counter to initial value

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
import com.alorma.fireandforget.multiplatform.settings.SettingsFireAndForgetRunner
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
  private val counterMap = mutableMapOf<String, Int>()

  override fun checkEnabled(fireAndForget: FireAndForget): Boolean {
    return map[fireAndForget.name] ?: fireAndForget.defaultValue
  }

  override fun disable(fireAndForget: FireAndForget) {
    map[fireAndForget.name] = false
  }

  override fun reset(fireAndForget: FireAndForget) {
    map.remove(fireAndForget.name)
  }

  override fun getCounter(counterFireAndForget: CounterFireAndForget): Int? {
    return counterMap[counterFireAndForget.name]
  }

  override fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int) {
    counterMap[counterFireAndForget.name] = value
  }
}
```

#### Option C: Custom Runner

Implement `FireAndForgetRunner` with your preferred storage solution (Room, DataStore, SQLDelight, etc.):

```kotlin
class DataStoreRunner(
  private val dataStore: DataStore<Preferences>
) : FireAndForgetRunner() {
  override fun checkEnabled(fireAndForget: FireAndForget): Boolean {
    // Your DataStore implementation
  }

  override fun disable(fireAndForget: FireAndForget) {
    // Your DataStore implementation
  }

  override fun reset(fireAndForget: FireAndForget) {
    // Your DataStore implementation
  }

  override fun getCounter(counterFireAndForget: CounterFireAndForget): Int? {
    // Your DataStore implementation for counter
  }

  override fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int) {
    // Your DataStore implementation for counter
  }
}
```

### Step 3: Use in Your Code

```kotlin
fun showAppContent() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val onboarding = OnboardingFlag(runner)

  if (onboarding.isEnabled()) {
    // This will only show once
    showOnboardingScreen(
      onComplete = {
        onboarding.disable() // Mark as completed
      }
    )
  } else {
    showMainScreen()
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
  val autoDisable: Boolean = false,
)
```

#### Constructor Parameters

- `fireAndForgetRunner`: The runner implementation that handles state persistence
- `name`: Unique identifier for this flag (used as storage key)
- `defaultValue`: Initial state (default: `true` = enabled)
- `autoDisable`: When `true`, automatically disables the flag on first call to `isEnabled()` (default: `false`)

#### Methods

- `isEnabled(): Boolean` - Returns `true` if the code should execute
- `disable()` - Marks the flag as executed (disables it)
- `reset()` - Resets the flag back to `defaultValue` (allows re-execution)

### CounterFireAndForget Class

```kotlin
abstract class CounterFireAndForget(
  fireAndForgetRunner: FireAndForgetRunner,
  name: String,
  val counter: Int,
) : FireAndForget(...)
```

#### Constructor Parameters

- `fireAndForgetRunner`: The runner implementation that handles state persistence
- `name`: Unique identifier for this flag (used as storage key)
- `counter`: Number of times `isEnabled()` will return `true` before returning `false`

#### Methods

- `isEnabled(): Boolean` - Returns `true` if counter > 0, decrements counter on each call
- `reset()` - Resets the counter back to initial value

#### Behavior

```kotlin
val feature = CounterFireAndForget(runner, "feature", counter = 3)
feature.isEnabled() // Call 1: true (counter: 3 → 2)
feature.isEnabled() // Call 2: true (counter: 2 → 1)
feature.isEnabled() // Call 3: true (counter: 1 → 0)
feature.isEnabled() // Call 4+: false (counter: 0)
feature.reset()     // Resets counter to 3
```

### FireAndForgetRunner Abstract Class

```kotlin
abstract class FireAndForgetRunner {
  fun isEnabled(fireAndForget: FireAndForget): Boolean
  protected abstract fun checkEnabled(fireAndForget: FireAndForget): Boolean
  abstract fun disable(fireAndForget: FireAndForget)
  abstract fun reset(fireAndForget: FireAndForget)
  
  // Counter support for CounterFireAndForget
  fun isEnabledCounter(counterFireAndForget: CounterFireAndForget): Boolean
  protected abstract fun getCounter(counterFireAndForget: CounterFireAndForget): Int?
  protected abstract fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int)
  fun resetCounter(counterFireAndForget: CounterFireAndForget)
}
```

**Implementation Note**: When creating a custom runner, you must override `checkEnabled()` instead of `isEnabled()`. The `isEnabled()` method is final and handles the `autoDisable` logic internally, ensuring it cannot be bypassed by runner implementations. You must also implement `getCounter()` and `setCounter()` to support `CounterFireAndForget`.

## Usage Examples

### Basic One-Time Execution

```kotlin
class WelcomeMessage(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "welcome_message"
)

fun showHomeScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val welcomeFlag = WelcomeMessage(runner)

  if (welcomeFlag.isEnabled()) {
    showWelcomeDialog(
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

fun showMainScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val announcement = NewFeatureAnnouncement(runner)

  if (announcement.isEnabled()) {
    // Show announcement
    displayMessage("Check out our new feature!")
    announcement.disable()
  }
}
```

### Tutorial with Reset Option

```kotlin
class AppTutorial(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "app_tutorial"
)

fun handleRestartTutorial() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val tutorial = AppTutorial(runner)

  // Allow tutorial to run again
  tutorial.reset()
  navigateToTutorial()
}
```

### Multiple Flags with Shared Runner

```kotlin
fun showApp() {
  val runner = SettingsFireAndForgetRunner(Settings())

  // Multiple flags can share the same runner
  val onboarding = OnboardingFlag(runner)
  val tutorial = TutorialFlag(runner)
  val featureAnnouncement = FeatureAnnouncementFlag(runner)

  when {
    onboarding.isEnabled() -> showOnboardingScreen { onboarding.disable() }
    tutorial.isEnabled() -> showTutorialScreen { tutorial.disable() }
    else -> showMainScreen()
  }
}
```

### First-Run Setup

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

### Auto-Disable Feature

Use `autoDisable = true` to automatically disable the flag on first access without manually calling `disable()`:

```kotlin
class QuickTip(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "quick_tip",
  autoDisable = true  // Automatically disables after first isEnabled() call
)

fun showScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val quickTip = QuickTip(runner)

  // First call: returns true and automatically disables
  if (quickTip.isEnabled()) {
    showTooltip("Here's a quick tip!")
    // No need to call quickTip.disable()
  }

  // Subsequent calls: returns false
  quickTip.isEnabled() // false
}
```

This is perfect for fire-and-forget operations where you don't have a natural completion callback to call `disable()`. The flag automatically marks itself as executed when accessed for the first time.

### Counter-Based Execution (Execute N Times)

Use `CounterFireAndForget` to execute code a specific number of times before disabling:

```kotlin
import com.alorma.fireandforget.CounterFireAndForget

class LimitedPromo(runner: FireAndForgetRunner, times: Int = 3) : CounterFireAndForget(
  fireAndForgetRunner = runner,
  name = "limited_promo",
  counter = times  // Will return true 3 times, then false
)

fun showScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val promo = LimitedPromo(runner, times = 3)

  // First 3 calls: returns true and decrements counter
  if (promo.isEnabled()) {
    showPromoBanner("Special offer!")
  }

  // After 3 calls: returns false
  // Counter state persists across app restarts
}
```

#### How Counter Works

- Counter decrements on each `isEnabled()` call
- Returns `true` while counter > 0
- Returns `false` when counter reaches 0
- Calling `reset()` restores counter to initial value
- Counter persists across app restarts (with persistent runners)

**Perfect for:**
- 🎯 Limited feature announcements (show N times)
- 📚 Tutorial hints for first N uses
- 🎁 Trial features with usage limits
- 🔔 Reminder messages (show N times before stopping)

#### Inverse Pattern: Show AFTER N Times

To show a feature only AFTER it's been accessed N times (inverse logic), use `!isEnabled()`:

```kotlin
class ShowAfterVisits(runner: FireAndForgetRunner, visits: Int = 3) : CounterFireAndForget(
  fireAndForgetRunner = runner,
  name = "show_after_visits",
  counter = visits
)

fun showScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val showAfterVisits = ShowAfterVisits(runner, visits = 3)

  // Track visits silently - returns true for first 3 visits, false after
  val stillCounting = showAfterVisits.isEnabled()
  
  // Show feature ONLY after counter reaches 0 (inverse logic)
  if (!stillCounting) {
    showAdvancedFeature("You've visited 3 times! Here's an advanced feature.")
  }
  
  // Visit 1: stillCounting = true  → feature NOT shown
  // Visit 2: stillCounting = true  → feature NOT shown  
  // Visit 3: stillCounting = true  → feature NOT shown
  // Visit 4+: stillCounting = false → feature SHOWN
}
```

**Alternative: Track and Show Once After N Visits**

```kotlin
class UnlockAfterUses(runner: FireAndForgetRunner, requiredUses: Int = 3) : CounterFireAndForget(
  fireAndForgetRunner = runner,
  name = "unlock_tracker",
  counter = requiredUses
)

class FeatureUnlocked(runner: FireAndForgetRunner) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "feature_unlocked",
  autoDisable = true  // Show once, then auto-disable
)

fun showScreen() {
  val runner = SettingsFireAndForgetRunner(Settings())
  val unlockTracker = UnlockAfterUses(runner, requiredUses = 3)
  val featureUnlocked = FeatureUnlocked(runner)
  
  // Track usage silently
  val stillTracking = unlockTracker.isEnabled()
  
  // Show unlock message once when counter reaches 0
  if (!stillTracking && featureUnlocked.isEnabled()) {
    showUnlockMessage("🎉 Premium feature unlocked after 3 uses!")
  }
}
```

**Perfect for:**
- 🎓 Unlock features after N uses (gamification)
- ⭐ Show rating prompt after user has used app N times
- 🎁 Reward users after N sessions
- 💡 Progressive feature discovery (reveal after engagement)

## Project Structure

This repository contains:

- **core** - The main FireAndForget library implementation
- **multiplatform-settings** - A ready-to-use runner implementation using multiplatform-settings
- **samples/shared** - Shared UI code demonstrating library usage
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

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Learn More

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [multiplatform-settings](https://github.com/russhwolf/multiplatform-settings)
