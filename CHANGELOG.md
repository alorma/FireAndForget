# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Initial release of FireAndForget library
- Core module with `FireAndForget` abstract class and `FireAndForgetRunner` interface
- `multiplatform-settings` module with ready-to-use runner implementation
- Support for Android (API 21+), iOS, Desktop (JVM), Web (JS & WASM)
- Sample applications demonstrating library usage
- Comprehensive documentation and README

### Changed

### Deprecated

### Removed

### Fixed

### Security

---

## How to Update This Changelog

When making changes, add them under the `[Unreleased]` section in the appropriate category:

- **Added** for new features
- **Changed** for changes in existing functionality
- **Deprecated** for soon-to-be removed features
- **Removed** for now removed features
- **Fixed** for any bug fixes
- **Security** in case of vulnerabilities

When releasing a new version:
1. Create a new section with the version number and date
2. Move items from `[Unreleased]` to the new version section
3. Add a comparison link at the bottom of the file
