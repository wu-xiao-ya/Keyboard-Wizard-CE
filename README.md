# 按键精灵社区版（Keyboard Wizard CE）

This repository is the Community Edition and unofficial port of Modern KeyWizard, published as 按键精灵社区版（Keyboard Wizard CE） for `Minecraft 1.21.1 + NeoForge`.

- Branch: `ce/neoforge-1.21.1`
- Upstream project license: `AGPL-3.0-only`
- Upstream Keyboard Wizard license: `MIT`
- Modification date: `2026-06-15`

This branch is maintained as a CE port and is not the original upstream project.

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

If you distribute a built jar, you must also distribute the matching source code for that jar.
