# Keyboard Wizard CE Release Checklist

- Branch: `ce/forge-1.18.2`
- Target: `Minecraft 1.18.2` + `Forge 40.2.21+`
- Java: `17`
- Build command: `./gradlew build --warning-mode all`
- Bundle command: `./gradlew releaseBundle`
- Release jar: `build/libs/keyboard-wizard-ce-forge-1.18.2-1.0.1.jar`
- Source jar: `build/libs/keyboard-wizard-ce-forge-1.18.2-1.0.1-sources.jar`
- Release bundle: `build/release/keyboard-wizard-ce-forge-1.18.2-1.0.1-release.zip`
- Checksum file: `build/release/SHA256SUMS.txt`
- Changelog: `CHANGELOG.md`
- Release notes: `RELEASE_NOTES.md`
- Release tag pattern: `forge-1.18.2-v*`