# 按键精灵社区版（Keyboard Wizard CE）

This branch is the Community Edition / unofficial port of Modern KeyWizard, published as 按键精灵社区版（Keyboard Wizard CE） for Minecraft 1.20.1 + Forge.

- Branch: `ce/forge-1.20.1`
- Mod id: `keyboard_wizard_ce`
- CE version: `1.0.0`
- Current author: WuXiaoYa
- Original authors: MrNerdy42, Tapio
- Upstream license: AGPL-3.0-only
- Upstream project: Keyboard Wizard (MIT)
- Target: Minecraft `1.20.1` + Forge `47.4.10+`
- Modified on: `2026-06-17`

## Current branch features

- F7 opens Keyboard Wizard CE directly in-game.
- Main keyboard, numpad, and auxiliary key layouts are available from the CE screen.
- The vanilla Controls screen has a bottom-right toggle button for Keyboard Wizard CE.
- The CE screen uses the packaged community background texture.
- CE identity is published under `keyboard_wizard_ce`.

## Build

Use Java 17.

```powershell
.\gradlew.bat build --warning-mode all
```

The release jar is written to `build/libs/keyboard-wizard-ce-forge-1.20.1-1.0.0.jar`; the matching source jar is `build/libs/keyboard-wizard-ce-forge-1.20.1-1.0.0-sources.jar`.

Release tag: [`forge-1.20.1-v1.0.0`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/forge-1.20.1-v1.0.0).

See `CHANGELOG.md` for the CE port change summary.

When distributing a jar, please also provide the matching source code for that build.


