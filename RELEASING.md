# Releasing

`xslint-jetbrains` releases **automatically** when `xslint-lsp` releases — the
plugin bundles that server — and can also be released by hand.

## Automatic (the cascade)

When `xslint-lsp` publishes a new version it dispatches `xslint-lsp-released`
here. [`.github/workflows/cascade.yml`](.github/workflows/cascade.yml) bumps the
bundled `xslintLspVersion` in `gradle.properties` to that version, builds
against it, and cuts this plugin's own next patch — the latest tag + 1,
independent of the server's number — pushing the tag so `release.yml` publishes.

## Manual

Push a semver tag:

```bash
git tag 0.0.2
git push origin 0.0.2
```

The tag triggers [`release.yml`](.github/workflows/release.yml), which builds
the plugin, signs it, and publishes it to the JetBrains Marketplace with
`./gradlew publishPlugin`, stamping the version from the tag.

## Prerequisites

- `PUBLISH_TOKEN` — a JetBrains Marketplace token (plugins.jetbrains.com →
  Profile → My Tokens).
- `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD` — for `signPlugin`
  (the Marketplace prefers signed plugins).
- `DISPATCH_TOKEN` and the `master` ruleset bypass — as in the other repos, so
  the cascade's bump can push to the protected branch and its tag triggers the
  release.
