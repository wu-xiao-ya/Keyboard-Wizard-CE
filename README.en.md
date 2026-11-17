[简体中文](README.md) | **English**

# Keyboard Wizard CE

Keyboard Wizard CE is a community-maintained edition of Modern KeyWizard and Keyboard Wizard for Minecraft Java Edition. It lets you find, inspect, and change key bindings on an interactive keyboard.

[Download v1.1.1](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/v1.1.1) · [Shared source](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/tree/codex/shared-core) · [Release history](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases)

## Supported Minecraft versions

Version 1.1.1 ships 33 builds across Fabric, Forge, and NeoForge. Fabric covers 24 official releases from 1.20 through 26.3.

| Loader | Minecraft versions | Java |
| --- | --- | --- |
| Fabric | 1.20, 1.20.1, 1.20.2, 1.20.3, 1.20.4 | 17 |
| Fabric | 1.20.5, 1.20.6, 1.21, 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.7, 1.21.8, 1.21.9, 1.21.10, 1.21.11 | 21 |
| Fabric | 26.1, 26.1.1, 26.1.2, 26.2, 26.3 | 25 |
| Forge | 1.12.2, 1.16.5 | 8 |
| Forge | 1.18.2, 1.20.1 | 17 |
| NeoForge | 1.21.1, 1.21.4 | 21 |
| NeoForge | 26.1.2, 26.2, 26.3 | 25 |

## Installation and upgrade

1. In the release assets, download the `-1.1.1.jar` that matches your Minecraft version and loader. For Fabric, also install Fabric API for your Minecraft version.
2. When upgrading, remove the old Keyboard Wizard CE JAR from your `mods` folder, then place the new JAR there.
3. Existing key bindings use the same saved option keys. If a custom resource pack overrides the mod's assets, move them from `assets/keyboard_wizard_ce/` to `assets/keyboard_wizard_community_edition/`. Update any modpack references to use the full mod ID, `keyboard_wizard_community_edition`.

The release also includes a matching `-sources.jar` for each build and a `SHA256SUMS.txt` file.

## Features

- Press **F7** in game or use the toggle button on the vanilla Controls screen to open Keyboard Wizard CE.
- View the main keyboard, numpad, and auxiliary key layouts.
- Filter bindings by mod or category, search by binding name, or look up bindings by physical key.
- Inspect each binding's name, assigned key, and owning mod or category in the side panel.
- All 33 builds use the silver-gray pixel theme and its keyboard icons.

## Source and history

Current development takes place on `codex/shared-core`, where all 33 targets build from the same shared code. See the [build workflow](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/blob/codex/shared-core/.github/workflows/shared-core.yml) for the target matrix.

Earlier builds were maintained on version-specific branches. The old Fabric 1.16.5 and 1.18.2 branches were never released. The [historical changelog](CHANGELOG.md) records the earlier branch builds, including the Fabric 26.3 snapshot builds.

## License and credits

Keyboard Wizard CE is licensed under AGPL-3.0-only. If you distribute a binary JAR, provide its matching source code. See [LICENSE.md](LICENSE.md), [COPYING.md](COPYING.md), and [NOTICE.md](NOTICE.md).

- [Modern KeyWizard upstream](https://github.com/VulpesStella/ModernKeyWizard)
- [Keyboard Wizard upstream](https://github.com/Fuzss/keyboardwizard)
- [Keyboard Wizard legacy reference](https://github.com/MrNerdy42/keywizard-legacy)
- Art assets by WI_8614_ice
