# Modern KeyWizard CE

Modern KeyWizard Community Edition / unofficial port for `Minecraft 26.1.2` on `NeoForge 26.1.2.76`.

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

The release jar is written to `build/libs/mkw-2.1.0-neoforge.26.1.2.jar`; the matching source jar is `build/libs/mkw-2.1.0-neoforge.26.1.2-sources.jar`.

When distributing a release jar, provide the corresponding source for that jar together with the binary.
