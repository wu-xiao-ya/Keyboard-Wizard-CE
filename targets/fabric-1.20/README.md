# Keyboard Wizard CE

Keyboard Wizard CE is the community edition of Modern KeyWizard for Minecraft 1.20 on Fabric.

- Adapter baseline: `origin/ce/fabric-1.20.1` at `3b66dd1`
- Mod id: `keyboard_wizard_ce`
- Mod name: `Keyboard Wizard CE`
- Maven group: `io.github.wu_xiao_ya.keyboard_wizard_ce.fabric`
- Archive base name: `keyboard-wizard-ce-fabric-1.20`
- Original authors: MrNerdy42, Tapio
- Art assets: WI_8614_ice
- Target: Minecraft 1.20 + Fabric
- Java: 17

This target ports the modern CE feature set to Fabric 1.20, including the F7 entry, Controls screen icon entry, layout switching, search, key-based filtering, help tooltip, CE background, and bundled CE icon assets.

## Validation target

From this directory, run `./gradlew build releaseBundle --no-daemon --stacktrace`.
