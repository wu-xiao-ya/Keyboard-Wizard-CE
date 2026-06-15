# 按键精灵社区版（Keyboard Wizard CE）

Modern KeyWizard Community Edition / unofficial port, published as 按键精灵社区版（Keyboard Wizard CE）, for `Minecraft 26.1.2` on `NeoForge 26.1.2.76`.

- Branch: `ce/neoforge-26.1.2`
- Java: `25`
- Modification date: `2026-06-15`
- Original project license: `AGPL-3.0-only`
- Upstream Keyboard Wizard license: `MIT`

This branch is maintained as a Community Edition port. It is not the upstream project.

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

See `CHANGELOG.md` for the CE port change summary.

When distributing a release jar, provide the corresponding source for that jar together with the binary.
