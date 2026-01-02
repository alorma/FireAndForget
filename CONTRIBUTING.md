# Contributing to FireAndForget

First off, thank you for considering contributing to FireAndForget! It's people like you that make FireAndForget such a great tool.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
  - [Reporting Bugs](#reporting-bugs)
  - [Suggesting Enhancements](#suggesting-enhancements)
  - [Your First Code Contribution](#your-first-code-contribution)
  - [Pull Requests](#pull-requests)
- [Styleguides](#styleguides)
  - [Git Commit Messages](#git-commit-messages)
  - [Kotlin Styleguide](#kotlin-styleguide)
- [Development Setup](#development-setup)
- [Testing](#testing)

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code.

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the [existing issues](https://github.com/alorma/FireAndForget/issues) to avoid duplicates.

When you are creating a bug report, please include as many details as possible using our [bug report template](.github/ISSUE_TEMPLATE/bug_report.yml).

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please use our [feature request template](.github/ISSUE_TEMPLATE/feature_request.yml).

### Your First Code Contribution

Unsure where to begin? You can start by looking through issues labeled:

- `good first issue` - issues that should only require a few lines of code
- `help wanted` - issues that may be more involved but are great for contributors

### Pull Requests

1. **Fork the repository** and create your branch from `main`:
   ```bash
   git checkout -b feature/my-new-feature
   ```

2. **Make your changes**:
   - Write clean, maintainable code
   - Follow our styleguides
   - Add tests for new functionality
   - Update documentation as needed

3. **Test your changes**:
   ```bash
   # Run all tests
   ./gradlew allTests

   # Run specific platform tests
   ./gradlew jvmTest
   ./gradlew jsTest
   ./gradlew iosSimulatorArm64Test
   ```

4. **Build the project**:
   ```bash
   # Build all modules
   ./gradlew build

   # Build specific modules
   ./gradlew :core:build
   ./gradlew :multiplatform-settings:build
   ```

5. **Commit your changes**:
   - Write clear, concise commit messages
   - Follow the [commit message guidelines](#git-commit-messages)

6. **Push to your fork**:
   ```bash
   git push origin feature/my-new-feature
   ```

7. **Open a Pull Request**:
   - Use the [pull request template](.github/PULL_REQUEST_TEMPLATE.md)
   - Provide a clear description of the changes
   - Link any related issues
   - Ensure all CI checks pass

## Styleguides

### Git Commit Messages

- Use the present tense ("Add feature" not "Added feature")
- Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
- Limit the first line to 72 characters or less
- Reference issues and pull requests after the first line

Example:
```
Add support for custom storage keys

- Allow users to customize the storage key prefix
- Add documentation for the new API
- Update tests to cover new functionality

Closes #123
```

### Kotlin Styleguide

- Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused on a single task
- Add KDoc comments for public APIs
- Use trailing commas in multi-line declarations

Example:
```kotlin
/**
 * Creates a new FireAndForget instance with the given configuration.
 *
 * @param runner The runner implementation for state persistence
 * @param name Unique identifier for this flag
 * @param defaultValue Initial state (default: true)
 * @return A configured FireAndForget instance
 */
class MyFlag(
  runner: FireAndForgetRunner,
) : FireAndForget(
  fireAndForgetRunner = runner,
  name = "my_flag",
  defaultValue = true,
)
```

## Development Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/alorma/FireAndForget.git
   cd FireAndForget
   ```

2. **Open in IDE**:
   - We recommend IntelliJ IDEA or Android Studio
   - Open the project and let Gradle sync

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the samples**:
   ```bash
   # Android
   ./gradlew :samples:androidApp:assembleDebug

   # Desktop
   ./gradlew :samples:desktopApp:run
   ```

## Testing

### Running Tests

```bash
# All tests across all platforms
./gradlew allTests

# Platform-specific tests
./gradlew jvmTest           # JVM tests
./gradlew jsTest            # JavaScript tests
./gradlew iosSimulatorArm64Test  # iOS Simulator tests
```

### Writing Tests

- Write tests for all new functionality
- Ensure tests pass on all supported platforms
- Use descriptive test names that explain what is being tested
- Follow the AAA pattern (Arrange, Act, Assert)

Example:
```kotlin
@Test
fun `isEnabled returns true when flag has not been disabled`() {
  // Arrange
  val runner = InMemoryFireAndForgetRunner()
  val flag = TestFlag(runner)

  // Act
  val result = flag.isEnabled()

  // Assert
  assertTrue(result)
}
```

## Project Structure

- `core/` - Main library implementation
- `multiplatform-settings/` - multiplatform-settings runner implementation
- `samples/shared/` - Shared sample code
- `samples/androidApp/` - Android sample app
- `samples/desktopApp/` - Desktop sample app
- `build-logic/` - Gradle convention plugins

## Questions?

Feel free to:
- Open a [question issue](.github/ISSUE_TEMPLATE/question.yml)
- Reach out to the maintainers
- Check the [README](README.md) for more information

Thank you for contributing! 🎉
