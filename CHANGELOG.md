# Changelog

## Unreleased branch build - 2026-06-16

- Added the in-game F7 path that opens Keyboard Wizard CE directly.
- Added main keyboard, numpad, and auxiliary key layout switching.
- Added the packaged CE background texture.
- Added the vanilla Controls screen bottom-right toggle button.
- Rendered the toggle button from the packaged GUI texture instead of a missing sprite-atlas lookup.
- Confirmed the CE mod id and metadata use `keyboard_wizard_ce`.
- Latest verified Actions build: `27619621187`.

## 2.1.0 CE - 2026-06-15

- Ported 按键精灵社区版（Keyboard Wizard CE） to Minecraft 1.21.1 + NeoForge 21.1.219.
- Migrated key mapping access and GUI code from Fabric/Yarn naming to NeoForge/Mojmap APIs.
- Added NeoForge mod metadata and changed the CE mod id to `keyboard_wizard_ce`.
- Added AGPL license files, CE notice, release checklist, and GitHub Actions build workflow.
- Bundled README, NOTICE, COPYING, and LICENSE files into the release jar.
- Published release artifacts as `keyboard-wizard-ce-neoforge-1.21.1-2.1.0`.
