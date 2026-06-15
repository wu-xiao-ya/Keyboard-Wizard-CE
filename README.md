# Modern KeyWizard CE

This branch is the Community Edition / unofficial port of Modern KeyWizard for Minecraft 1.20.1 + Forge.

- Branch: `ce/forge-1.20.1`
- Upstream license: AGPL-3.0-only
- Upstream project: Keyboard Wizard (MIT)
- Modified on: `2026-06-15`

## Build

Use Java 17.

```powershell
.\gradlew.bat build --warning-mode all
```

The release jar is written to `build/libs/mkw-forge-1.20.1-2.1.0.jar`; the matching source jar is `build/libs/mkw-forge-1.20.1-2.1.0-sources.jar`.

When distributing a jar, please also provide the matching source code for that build.
