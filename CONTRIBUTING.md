# Contributing

Thanks for helping improve xslint-jetbrains.

## Build and test

Requires **JDK 21** and **Node.js**. The Gradle wrapper pins everything else.

```bash
./gradlew build            # compile, test, and assemble the plugin
./gradlew runIde           # try it in a sandbox IDE
./gradlew detekt           # Kotlin static analysis
./gradlew verifyPlugin     # the JetBrains Plugin Verifier
```

## Guidelines

- Keep the plugin a **thin adapter**. The linting lives in
  [xslint](https://github.com/xslint/xslint) and the server in
  [xslint-lsp](https://github.com/xslint/xslint-lsp); logic belongs upstream,
  not here.
- Every source file carries an SPDX header — run `reuse lint` to check.
- Open a pull request; CI (build, coverage, detekt, plugin verifier, REUSE,
  and the meta-linters) must be green before it merges.
