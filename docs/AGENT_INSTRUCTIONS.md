# FireAndForget: instructions for AI agents

Use this file when you are asked to **add FireAndForget to a project** (for working on the library
itself, see [AGENTS.md](../AGENTS.md)).

FireAndForget is a small Kotlin Multiplatform library (Android, iOS, Desktop JVM, JS, WasmJS) to run
code **once** (or **N times**) and remember it, e.g. onboarding, "what's new" dialogs, tutorial
hints. It does no UI and no storage by itself: state lives in a `FireAndForgetRunner`.

## 1. Dependencies

Group id: `com.github.alorma.fire-and-forget`. Latest version: see the Maven Central badge in the
README (`1.4.0` at the time of writing).

| Artifact | Contents |
|----------|----------|
| `core` | `FireAndForget`, `CounterFireAndForget`, `FireAndForgetRunner`. Always required. |
| `multiplatform-settings` | `SettingsFireAndForgetRunner`, persistent runner backed by [russhwolf/multiplatform-settings](https://github.com/russhwolf/multiplatform-settings). |

```kotlin
// KMP: commonMain. Android/JVM-only projects: use dependencies { implementation(...) }
kotlin.sourceSets.commonMain.dependencies {
  implementation("com.github.alorma.fire-and-forget:core:1.4.0")
  implementation("com.github.alorma.fire-and-forget:multiplatform-settings:1.4.0") // persistence
}
```

`multiplatform-settings` depends on the settings library as `implementation`, so the consumer must
also add `com.russhwolf:multiplatform-settings` to create a `Settings()` instance.

## 2. Core API (package `com.alorma.fireandforget`)

```kotlin
open class FireAndForget(
  val fireAndForgetRunner: FireAndForgetRunner,
  val name: String,                 // unique key; also the storage key
  val defaultValue: Boolean = true, // true = enabled (will fire)
  val autoDisable: Boolean = false, // true = first isEnabled() == true also disables it
) {
  open fun isEnabled(): Boolean
  open fun disable()
  open fun reset()                  // back to defaultValue
}

open class CounterFireAndForget(
  fireAndForgetRunner: FireAndForgetRunner,
  name: String,
  val counter: Int,                 // how many times isEnabled() returns true
) : FireAndForget
```

- Both classes are `open`, **not abstract**: instantiate directly or subclass.
- `FireAndForget`: `isEnabled()` stays `true` until `disable()` is called (or immediately after the
  first call if `autoDisable = true`).
- `CounterFireAndForget`: **every `isEnabled()` call consumes one use.** It returns `true` `counter`
  times, then `false`. `reset()` restores the counter. `disable()` does not affect it.
- Do not call `isEnabled()` repeatedly (e.g. in recomposition) on a counter or `autoDisable` flag: each
  call changes state. Call it once and keep the result (`remember { flag.isEnabled() }`).

## 3. Runners

`SettingsFireAndForgetRunner(settings: Settings)` persists across restarts (SharedPreferences,
NSUserDefaults, java.util.prefs, localStorage). Use it by default.

```kotlin
val runner = SettingsFireAndForgetRunner(Settings())
```

For tests or non-persistent state, write an in-memory runner (a sample exists in
`samples/shared/.../InMemoryFireAndForgetRunner.kt`). To support another store, extend
`FireAndForgetRunner` and implement:

```kotlin
protected abstract fun checkEnabled(fireAndForget: FireAndForget): Boolean // NOT isEnabled()
abstract fun disable(fireAndForget: FireAndForget)
abstract fun reset(fireAndForget: FireAndForget)
protected abstract fun getCounter(counterFireAndForget: CounterFireAndForget): Int? // null = never set
protected abstract fun setCounter(counterFireAndForget: CounterFireAndForget, value: Int)
```

`FireAndForgetRunner.isEnabled()` is final and handles `autoDisable`; override `checkEnabled`.

## 4. Examples

Show once:

```kotlin
val runner = SettingsFireAndForgetRunner(Settings())
val welcome = FireAndForget(runner, name = "welcome_v1")

if (welcome.isEnabled()) {
  showWelcomeDialog(onDismiss = { welcome.disable() })
}
```

Show once without a completion callback:

```kotlin
val tip = FireAndForget(runner, name = "quick_tip", autoDisable = true)
if (tip.isEnabled()) showTooltip()
```

Show the first 3 times:

```kotlin
val promo = CounterFireAndForget(runner, name = "promo", counter = 3)
if (promo.isEnabled()) showPromoBanner()
```

Show only after N visits (inverse): `isEnabled()` turns `false` once the counter is spent.

```kotlin
val visits = CounterFireAndForget(runner, name = "visits", counter = 3)
val stillCounting = visits.isEnabled()
if (!stillCounting) showRatingPrompt()
```

In Compose, compute it once per composition:

```kotlin
val showTip = remember { welcome.isEnabled() }
if (showTip) { /* UI */ }
```

Re-show something later (e.g. "restart tutorial"): `flag.reset()`.

## 5. Common mistakes

- Reusing the same `name` for two flags: they share state. Names must be unique. Bump the name
  (`"whats_new_v2"`) to re-show something after an update.
- Using an in-memory runner in production: state is lost on restart.
- Calling `isEnabled()` on a `CounterFireAndForget` or `autoDisable` flag during recomposition or in
  multiple places: it consumes uses/disables unexpectedly.
- Overriding `isEnabled()` in a custom runner: implement `checkEnabled()`.
- Expecting `disable()` to stop a `CounterFireAndForget`: it does not.
- Using the wrong Maven group: it is `com.github.alorma.fire-and-forget`, not `com.alorma.fireandforget`
  (the latter is only the Kotlin package name).

## 6. Where to look for more

- Compilable usage: `samples/shared` in this repository.
- [README](../README.md) for longer examples.
