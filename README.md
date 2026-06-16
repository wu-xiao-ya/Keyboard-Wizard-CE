# 按键精灵社区版（Keyboard Wizard CE）

This repository is the Community Edition and unofficial port of Modern KeyWizard, published as 按键精灵社区版（Keyboard Wizard CE） for `Minecraft 1.21.1 + NeoForge`.

- Branch: `ce/neoforge-1.21.1`
- Mod id: `keyboard_wizard_ce`
- Current author: WuXiaoYa
- Original authors: MrNerdy42, Tapio
- Upstream project license: `AGPL-3.0-only`
- Upstream Keyboard Wizard license: `MIT`
- Target: Minecraft `1.21.1` + NeoForge `21.1.219+`
- Modification date: `2026-06-16`

This branch is maintained as a CE port and is not the original upstream project.

## Current branch features

- F7 opens Keyboard Wizard CE directly in-game.
- Main keyboard, numpad, and auxiliary key layouts are available from the CE screen.
- The vanilla Controls screen has a bottom-right toggle button for Keyboard Wizard CE.
- The toggle button is rendered from the packaged GUI texture to avoid missing-texture atlas lookups.
- The CE screen uses the packaged community background texture.
- CE identity is published under `keyboard_wizard_ce`.

## Build

Use Java 21.

```powershell
.\gradlew.bat assemble
```

For offline local verification with a prepared NeoForge cache:

```powershell
$env:NEOFORGE_LOCAL_MAVEN='<path-to-local-neoforge-cache>'
.\gradlew.bat assemble --offline
```

The release jar is written to `build/libs/keyboard-wizard-ce-neoforge-1.21.1-2.1.0.jar`; the matching source jar is `build/libs/keyboard-wizard-ce-neoforge-1.21.1-2.1.0-sources.jar`.

Latest verified branch build: [GitHub Actions run 27619621187](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27619621187).

See `CHANGELOG.md` for the CE port change summary.

If you distribute a built jar, you must also distribute the matching source code for that jar.
