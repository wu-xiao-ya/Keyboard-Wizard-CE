# Changelog

## 1.0.0 CE - 2026-06-17

- Added the NeoForge 26.2 branch based on the existing 26.1.2 CE implementation.
- Targeted Minecraft `26.2` with NeoForge `26.2.0.0-beta`.
- Kept the existing CE feature set: direct F7 opening, main / numpad / auxiliary layouts, CE background texture, Controls screen toggle button, and `keyboard_wizard_ce` metadata.
- Restarted the Community Edition version line at 1.0.0.
- Published this branch under release tag neoforge-26.2-v1.0.0.
- Preserved the current CE feature set and metadata under keyboard_wizard_ce.

## Unreleased branch build - 2026-06-16

- Added the in-game F7 path that opens Keyboard Wizard CE directly.
- Added main keyboard, numpad, and auxiliary key layout switching.
- Added the packaged CE background texture.
- Added the vanilla Controls screen bottom-right toggle button.
- Rendered the toggle button from the packaged GUI texture instead of a missing sprite-atlas lookup.
- Changed CE identity and metadata to `keyboard_wizard_ce`.
- Latest verified Actions build: `27646123159`.

## 2.1.0 CE - 2026-06-15

- Ported 按键精灵社区版（Keyboard Wizard CE） to Minecraft 26.1.2 + NeoForge 26.1.2.76.
- Migrated GUI rendering, input events, key mapping access, and resource identifiers to the 26.1.2 API shape.
- Added NeoForge mod metadata and changed the CE mod id to `keyboard_wizard_ce`.
- Added AGPL license files, CE notice, release checklist, and GitHub Actions build workflow.
- Bundled README, NOTICE, COPYING, and LICENSE files into the release jar.
- Published release artifacts as `keyboard-wizard-ce-neoforge-26.1.2-2.1.0`.





