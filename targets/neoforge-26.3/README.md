# 按键精灵社区版（Keyboard Wizard CE）

Modern KeyWizard Community Edition / unofficial port, published as 按键精灵社区版（Keyboard Wizard CE）, for Minecraft 26.3 on NeoForge.

- Branch: `ce/neoforge-26.3`
- Mod id: `keyboard_wizard_community_edition`
- CE version: `1.1.0`
- Current author: WuXiaoYa
- Original authors: MrNerdy42, Tapio
- Art assets: WI_8614_ice
- Java: `25`
- Target: Minecraft `26.3` + NeoForge `26.3`
- Minecraft dependency: `[26.3,26.4)`
- NeoForge dependency: `[26.3,)`
- Modification date: `2026-09-27`
- Original project license: `AGPL-3.0-only`
- Upstream Keyboard Wizard license: `MIT`

This branch is maintained as a Community Edition port of the stable Minecraft 26.3 release. It is not the upstream project. Snapshot, pre-release, and release-candidate builds of Minecraft 26.3 are not supported.

NeoForge has not published a non-beta 26.3 build yet. The development dependency is the newest 26.3 NeoForge build, `26.3.0.23-beta`, while the mod metadata accepts any NeoForge `26.3` or newer so a later stable NeoForge build can load the same jar.

## Current branch features

- Includes localization for the NeoForge key category id `key.category.keyboard_wizard_community_edition.bindings`.
- F7 opens Keyboard Wizard CE directly in-game.
- Main keyboard, numpad, and auxiliary key layouts are available from the CE screen.
- The vanilla Controls screen has a bottom-right toggle button for Keyboard Wizard CE.
- The toggle button is rendered from the packaged GUI texture to avoid missing-texture atlas lookups.
- The CE screen uses the packaged community background texture.
- CE identity is published under `keyboard_wizard_community_edition`.
