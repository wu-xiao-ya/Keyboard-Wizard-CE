# Changelog

## Localization fix - 2026-06-17

- Added key.category.keyboard_wizard_ce.bindings on all maintained branches so the Keyboard Wizard CE category name renders correctly in Controls screens.
- Latest verified Actions builds: Forge 1.20.1 27646830595, NeoForge 1.21.1 27646836029, NeoForge 26.1.2 27646857844, NeoForge 26.2 27646882891.


## 26.2 preview branch - 2026-06-17

- Added `ce/neoforge-26.2` for Minecraft 26.2 + NeoForge 26.2.0.0-beta.
- Ported the 26.1.2 CE feature set to the 26.2 screen API changes.
- Verified GitHub Actions build `27646123159`.
- Release is pending until runtime testing is complete.

## 1.0.0 CE - 2026-06-17

- Restarted the Community Edition version line at `1.0.0`.
- Updated supported release tags to:
  - `forge-1.20.1-v1.0.0`
  - `neoforge-1.21.1-v1.0.0`
  - `neoforge-26.1.2-v1.0.0`
- Kept the existing CE feature set: direct F7 opening, main / numpad / auxiliary layouts, CE background texture, Controls screen toggle button, and `keyboard_wizard_ce` metadata.

## Unreleased branch builds - 2026-06-16

- Added a repository status update for all maintained CE branches.
- Documented the current branch build artifacts from GitHub Actions separately from the v2.1.0 release tags.
- Documented the current CE feature set: direct F7 opening, main / numpad / auxiliary layout switching, CE background texture, and Controls screen toggle button.
- Documented recent branch fixes:
  - Forge 1.20.1 now registers the Controls screen toggle through the Forge screen init event.
  - NeoForge 1.21.1 and 26.1.2 now render the Controls screen toggle from packaged GUI textures instead of missing atlas sprites.
  - CE mod identity and metadata are aligned on `keyboard_wizard_ce`.

## 2.1.0 CE - 2026-06-15

- Published 按键精灵社区版（Keyboard Wizard CE） as a community-maintained CE distribution.
- Added version branches for:
  - Minecraft 1.20.1 + Forge
  - Minecraft 1.21.1 + NeoForge
  - Target 26.1.2 + NeoForge
- Added GitHub Actions build and release workflows for each version branch.
- Added AGPL license files, CE notices, release documentation, and SHA-256 release checksums.
- Bundled README, NOTICE, COPYING, and LICENSE files into published jars.

