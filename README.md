# FireAndForget

A lightweight Kotlin Multiplatform library for Compose Multiplatform that executes code once on first access, with optional auto-toggling behavior.

## Overview

FireAndForget is a simple yet powerful utility for managing one-time operations in your Compose Multiplatform applications. It's perfect for scenarios like:

- First-time user onboarding flows
- One-time feature announcements
- Initial setup operations
- Tutorial or walkthrough displays
- Feature flag management with automatic reset

## Features

- **Execute Once**: Code runs only on first access
- **Auto-Toggle Support**: Optionally reset the state after execution
- **Simple API**: Three intuitive methods for complete control
- **Multiplatform**: Works on Android, iOS, Web, Desktop (JVM), and Server
- **Compose-Friendly**: Designed for seamless integration with Compose Multiplatform

## API

### FireAndForget Class

```kotlin
class FireAndForget(autoToggle: Boolean = false)
```

#### Constructor Parameters

- `autoToggle` (Boolean, default: false) - When true, automatically resets the enabled state after execution, allowing the code to run again on next access

#### Methods

- `isEnabled(): Boolean` - Returns true if the code should execute (hasn't run yet or was reset)
- `toggle()` - Manually toggles the enabled state
- `clear()` - Resets the state to enabled (allows code to run again)

## Usage Examples

### Basic Usage - One Time Execution

```kotlin
val onboarding = FireAndForget()

@Composable
fun App() {
    if (onboarding.isEnabled()) {
        // This will only show once
        ShowOnboardingScreen()
    }
}
```

### With Auto-Toggle

```kotlin
val announcement = FireAndForget(autoToggle = true)

@Composable
fun App() {
    if (announcement.isEnabled()) {
        // This will execute, then automatically reset for next time
        ShowAnnouncement()
    }
}
```

### Manual Control

```kotlin
val tutorial = FireAndForget()

@Composable
fun App() {
    if (tutorial.isEnabled()) {
        ShowTutorial(
            onComplete = { tutorial.toggle() }, // Mark as completed
            onSkip = { tutorial.toggle() }       // Mark as skipped
        )
    }
}

// Reset to show tutorial again
Button(onClick = { tutorial.clear() }) {
    Text("Restart Tutorial")
}
```

## Project Structure

* [/composeApp](./composeApp/src) - Demo Compose Multiplatform application showcasing FireAndForget usage
* [/shared](./shared/src) - **FireAndForget library source code** - The main library implementation
  - [commonMain](./shared/src/commonMain/kotlin) - Core library code for all platforms
* [/iosApp](./iosApp/iosApp) - iOS application entry point
* [/server](./server/src/main/kotlin) - Ktor server application

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run Server

To build and run the development version of the server, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :server:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :server:run
  ```

### Build and Run Web Application

To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:
- for the Wasm target (faster, modern browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
    ```
- for the JS target (slower, supports older browsers):
  - on macOS/Linux
    ```shell
    ./gradlew :composeApp:jsBrowserDevelopmentRun
    ```
  - on Windows
    ```shell
    .\gradlew.bat :composeApp:jsBrowserDevelopmentRun
    ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).