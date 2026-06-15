# 按键精灵社区版（Keyboard Wizard CE） Release Checklist

- Branch: `ce/forge-1.20.1`
- Target: `Minecraft 1.20.1` + `Forge 47.4.20`
- Java: `17`
- Build command: `.\gradlew.bat build --warning-mode all`
- Release jar: `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0.jar`
- Source jar: `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0-sources.jar`
- Checksum file: `build/libs/SHA256SUMS.txt`
- Changelog: `CHANGELOG.md`

Before publishing:

1. Confirm `README.md`, `NOTICE.md`, `LICENSE.md`, and `COPYING.md` are present.
2. Build from a clean checkout.
3. Generate SHA-256 checksums for the release jar and source jar.
4. Publish the release jar, matching source jar, and checksum file together.
5. Mark the release as 按键精灵社区版（Keyboard Wizard CE）, a Community Edition / unofficial port.
