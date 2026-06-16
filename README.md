# 按键精灵社区版（Keyboard Wizard CE）

Modern KeyWizard Community Edition / unofficial port, published as 按键精灵社区版（Keyboard Wizard CE）, for `Minecraft 26.1.2` on `NeoForge 26.1.2.76`.

- Branch: `ce/neoforge-26.1.2`
- Mod id: `keyboard_wizard_ce`
- Current author: WuXiaoYa
- Original authors: MrNerdy42, Tapio
- Java: `25`
- Target: Minecraft `26.1.2` + NeoForge `26.1.2.x`
- Modification date: `2026-06-16`
- Original project license: `AGPL-3.0-only`
- Upstream Keyboard Wizard license: `MIT`

This branch is maintained as a Community Edition port. It is not the upstream project.

## Current branch features

- F7 opens Keyboard Wizard CE directly in-game.
- Main keyboard, numpad, and auxiliary key layouts are available from the CE screen.
- The vanilla Controls screen has a bottom-right toggle button for Keyboard Wizard CE.
- The toggle button is rendered from the packaged GUI texture to avoid missing-texture atlas lookups.
- The CE screen uses the packaged community background texture.
- CE identity is published under `keyboard_wizard_ce`.

## Build

Use Java 25.

```powershell
.\gradlew.bat assemble
```

For offline local verification with a prepared NeoForge cache:

```powershell
$env:NEOFORGE_LOCAL_MAVEN='<path-to-local-neoforge-cache>'
.\gradlew.bat assemble --offline
```

The release jar is written to `build/libs/keyboard-wizard-ce-neoforge-26.1.2-2.1.0.jar`; the matching source jar is `build/libs/keyboard-wizard-ce-neoforge-26.1.2-2.1.0-sources.jar`.

Latest verified branch build: [GitHub Actions run 27618961660](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27618961660).

See `CHANGELOG.md` for the CE port change summary.

When distributing a release jar, provide the corresponding source for that jar together with the binary.
