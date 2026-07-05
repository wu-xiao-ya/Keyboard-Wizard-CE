# Keyboard Wizard CE

Keyboard Wizard CE is the community edition branch of Modern KeyWizard for Minecraft 1.21.2 on Fabric.

- Branch: `ce/fabric-1.21.2`
- Mod id: `keyboard_wizard_ce`
- Mod name: `Keyboard Wizard CE`
- Maven group: `io.github.wu_xiao_ya.keyboard_wizard_ce.fabric`
- Archive base name: `keyboard-wizard-ce-fabric-1.21.2`
- Original authors: MrNerdy42, Tapio
- Art assets: WI_8614_ice
- Target: Minecraft 1.21.2 + Fabric
- Java: 21

This branch keeps the shared internal API layer used by the modern Fabric ports: `core/binding`, `bridge/binding`, and `core/layout`. It includes the F7 entry, Controls screen icon entry, layout switching, search, key-based filtering, help tooltip, CE background, and bundled CE icon assets.

## Support matrix

| Loader | Minecraft | Java | Branch | Mapping layer |
| --- | --- | --- | --- | --- |
| Fabric | 1.21.2 | 21 | `ce/fabric-1.21.2` | Yarn `1.21.2+build.1` |

## Validation target

Build with `./gradlew build releaseBundle --no-daemon --stacktrace --warning-mode all`.
