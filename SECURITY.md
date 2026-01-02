# Security Policy

## Supported Versions

We release patches for security vulnerabilities. Currently supported versions:

| Version | Supported          |
| ------- | ------------------ |
| Latest  | :white_check_mark: |
| < Latest| :x:                |

We recommend always using the latest version of FireAndForget to ensure you have the latest security patches.

## Reporting a Vulnerability

We take the security of FireAndForget seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### Please do NOT:

- Open a public GitHub issue for security vulnerabilities
- Disclose the vulnerability publicly before it has been addressed

### Please DO:

1. **Report privately via GitHub Security Advisories**:
   - Go to the [Security tab](https://github.com/alorma/FireAndForget/security/advisories) of the repository
   - Click "Report a vulnerability"
   - Fill out the form with details about the vulnerability

2. **Alternative: Email the maintainer**:
   - If you prefer not to use GitHub Security Advisories, you can open a private issue and tag it as security-related

### What to include in your report:

- A description of the vulnerability
- Steps to reproduce the issue
- Affected versions
- Potential impact
- Any suggested fixes (if you have them)

### What to expect:

- **Acknowledgment**: We will acknowledge receipt of your vulnerability report within 48 hours
- **Updates**: We will keep you informed about our progress in addressing the vulnerability
- **Resolution**: We will work to quickly resolve the issue and release a patch
- **Credit**: If you wish, we will publicly credit you for the responsible disclosure (unless you prefer to remain anonymous)

## Security Best Practices

When using FireAndForget in your application:

1. **Keep dependencies updated**: Regularly update to the latest version of FireAndForget and its dependencies
2. **Secure storage**: If using a custom `FireAndForgetRunner`, ensure your storage implementation follows security best practices:
   - Don't store sensitive data in flags unless properly encrypted
   - Use appropriate storage mechanisms for your platform (e.g., EncryptedSharedPreferences on Android)
3. **Review your runner implementation**: If implementing a custom runner, ensure it properly handles data persistence securely

## Known Security Considerations

- FireAndForget is designed for storing simple boolean flags, not sensitive data
- The library delegates storage to runner implementations - security of stored data depends on the runner used
- The included `multiplatform-settings` runner uses platform-standard storage:
  - Android: SharedPreferences (unencrypted by default)
  - iOS: NSUserDefaults (unencrypted by default)
  - JVM: java.util.prefs.Preferences
  - JS: localStorage (unencrypted)

If you need to store sensitive information, implement a custom runner with encryption or use this library only for non-sensitive flags.

## Security Updates

Security updates will be released as patch versions and announced via:
- GitHub Security Advisories
- Release notes
- CHANGELOG.md

## Questions?

If you have questions about security that don't relate to a vulnerability, please open a regular issue or discussion.

Thank you for helping keep FireAndForget and its users safe!
