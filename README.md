# xslint-jetbrains

[![DevOps By Rultor.com](https://www.rultor.com/b/xslint/xslint-jetbrains)](https://www.rultor.com/p/xslint/xslint-jetbrains)

[![build](https://github.com/xslint/xslint-jetbrains/actions/workflows/build.yml/badge.svg)](https://github.com/xslint/xslint-jetbrains/actions/workflows/build.yml)
[![verify](https://github.com/xslint/xslint-jetbrains/actions/workflows/verify.yml/badge.svg)](https://github.com/xslint/xslint-jetbrains/actions/workflows/verify.yml)
[![codecov](https://codecov.io/gh/xslint/xslint-jetbrains/branch/master/graph/badge.svg)](https://codecov.io/gh/xslint/xslint-jetbrains)
[![PDD status](http://www.0pdd.com/svg?name=xslint/xslint-jetbrains)](http://www.0pdd.com/p?name=xslint/xslint-jetbrains)
[![Hits-of-Code](https://hitsofcode.com/github/xslint/xslint-jetbrains)](https://hitsofcode.com/view/github/xslint/xslint-jetbrains)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/xslint/xslint-jetbrains/blob/master/LICENSE.txt)

A JetBrains IDE plugin that brings [xslint](https://github.com/xslint/xslint)'s
XSL/XSLT linting — malformed XML, invalid XPath, and stylistic defects, with
quick-fixes — to IntelliJ IDEA, WebStorm, PyCharm, and every other JetBrains
IDE. It launches the [xslint-lsp](https://github.com/xslint/xslint-lsp) language
server through [LSP4IJ](https://github.com/redhat-developer/lsp4ij).

## Requirements

- A JetBrains IDE, build 2024.2 (`242`) or newer — any edition, including the
  free IntelliJ IDEA Community.
- **Node.js** on your `PATH` — the plugin launches the bundled language server
  with it.

## Install

Search **xslint** in *Settings → Plugins → Marketplace* and install it;
[LSP4IJ](https://plugins.jetbrains.com/plugin/23257-lsp4ij) is pulled in
automatically as a required dependency.

To install a specific build instead, download the plugin `.zip` from the
[latest release](https://github.com/xslint/xslint-jetbrains/releases/latest)
and use *Settings → Plugins → ⚙ → Install Plugin from Disk…*.

Then open any `.xsl`/`.xslt` file — diagnostics and quick-fixes appear as you
type.

## How it works

The plugin bundles the [`xslint-lsp`](https://www.npmjs.com/package/xslint-lsp)
Node server and registers it with LSP4IJ for `*.xsl`/`*.xslt` files. LSP4IJ
speaks the Language Server Protocol to it, so the diagnostics and fixes are
identical to the command-line `xslint` and the VS Code extension — all three
reuse the same engine.

## Development

```bash
./gradlew buildPlugin      # build the distributable .zip (build/distributions)
./gradlew runIde           # launch a sandbox IDE with the plugin loaded
./gradlew test             # unit tests
./gradlew koverXmlReport   # coverage report (build/reports/kover)
./gradlew detekt           # Kotlin static analysis
./gradlew verifyPlugin     # the JetBrains Plugin Verifier
```

See [CONTRIBUTING.md](CONTRIBUTING.md) for more, and [RELEASING.md](RELEASING.md)
for how releases work.

## License

MIT
