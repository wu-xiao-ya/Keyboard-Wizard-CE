# 按键精灵社区版（Keyboard Wizard CE）

This branch is the Community Edition / unofficial port of Modern KeyWizard, published as 按键精灵社区版（Keyboard Wizard CE） for Minecraft 1.20.1 + Forge.

- Branch: `ce/forge-1.20.1`
- Mod id: `keyboard_wizard_ce`
- Authors: MrNerdy42, Tapio, wu-xiao-ya
- Upstream license: AGPL-3.0-only
- Upstream project: Keyboard Wizard (MIT)
- Modified on: `2026-06-15`

## Build

Use Java 17.

```powershell
.\gradlew.bat build --warning-mode all
```

The release jar is written to `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0.jar`; the matching source jar is `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0-sources.jar`.

See `CHANGELOG.md` for the CE port change summary.

When distributing a jar, please also provide the matching source code for that build.
