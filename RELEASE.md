# 按键精灵社区版（Keyboard Wizard CE） Release Checklist

- Branch: `ce/neoforge-26.1.2`
- Target: `Minecraft 26.1.2` + `NeoForge 26.1.2.76`
- Java: `25`
- Build command: `.\gradlew.bat assemble`
- Release jar: `build/libs/keyboard-wizard-ce-neoforge-26.1.2-2.1.0.jar`
- Source jar: `build/libs/keyboard-wizard-ce-neoforge-26.1.2-2.1.0-sources.jar`
- Checksum file: `build/libs/SHA256SUMS.txt`

Before publishing:

1. Confirm `README.md`, `NOTICE.md`, `LICENSE.md`, and `COPYING.md` are present.
2. Build from a clean checkout.
3. Generate SHA-256 checksums for the release jar and source jar.
4. Publish the release jar, matching source jar, and checksum file together.
5. Mark the release as 按键精灵社区版（Keyboard Wizard CE）, a Community Edition / unofficial port.
