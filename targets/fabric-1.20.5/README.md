# Keyboard Wizard CE

Keyboard Wizard CE is the community edition of Modern KeyWizard for Minecraft 1.20.5 on Fabric.

- Adapter baseline: `origin/ce/fabric-1.20.5` at `642c6d5`
- Mod id: `keyboard_wizard_ce`
- Mod name: `Keyboard Wizard CE`
- Maven group: `io.github.wu_xiao_ya.keyboard_wizard_ce.fabric`
- Archive base name: `keyboard-wizard-ce-fabric-1.20.5`
- Original authors: MrNerdy42, Tapio
- Art assets: WI_8614_ice
- Target: Minecraft 1.20.5 + Fabric
- Java: 21

This target uses the repository’s shared search, binding classification, keyboard geometry, and resources. It includes the F7 entry, Controls screen icon entry, layout switching, search, key-based filtering, help tooltip, CE background, and bundled CE icon assets.

## Support matrix

| Loader | Minecraft | Java | Adapter baseline | Mapping layer |
| --- | --- | --- | --- | --- |
| Fabric | 1.20.5 | 21 | `ce/fabric-1.20.5` | Yarn `1.20.5+build.1` |

## Validation target

Build with `./gradlew build releaseBundle --no-daemon --stacktrace --warning-mode all`.
