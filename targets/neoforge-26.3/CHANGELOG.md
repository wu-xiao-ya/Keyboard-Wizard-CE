# Changelog

## 1.1.0

- Set the mod ID and resource namespace to `keyboard_wizard_community_edition`.
- Retained the `key.keyboard_wizard_ce.openKeyWizard` mapping so existing shortcut customizations carry forward.
- Added English and Simplified Chinese labels for the category in the new namespace.

## NeoForge 26.3 stable port - 2026-09-27

- Added `ce/neoforge-26.3` for stable Minecraft 26.3.
- Dropped the 26.3 snapshot, pre-release, and release-candidate test line.
- Targets Minecraft `[26.3,26.4)` and NeoForge `[26.3,)`.
- Builds against NeoForge `26.3.0.23-beta` because NeoForge has not published a non-beta 26.3 build yet.
- Carries the modern CE feature set: F7 entry, Controls screen toggle, main / numpad / auxiliary layouts, search, and CE assets.
