# 按键精灵社区版（Keyboard Wizard CE） Release Checklist

- Branch: `ce/neoforge-26.2`
- Target: `Minecraft 26.2` + `NeoForge 26.2.0.0-beta`
- Java: `25`
- Build command: `.\gradlew.bat assemble`
- Bundle command: `.\gradlew.bat releaseBundle`
- Release jar: `build/libs/keyboard-wizard-ce-neoforge-26.2-1.0.0.jar`
- Source jar: `build/libs/keyboard-wizard-ce-neoforge-26.2-1.0.0-sources.jar`
- Release bundle: `build/release/keyboard-wizard-ce-neoforge-26.2-1.0.0-release.zip`
- Checksum file: `build/release/SHA256SUMS.txt`
- Changelog: `CHANGELOG.md`
- Release tag pattern: `neoforge-26.2-v*`

Before publishing:

1. Confirm `README.md`, `NOTICE.md`, `LICENSE.md`, and `COPYING.md` are present.
2. Build from a clean checkout.
3. Run `.\gradlew.bat releaseBundle`.
4. Publish the release bundle, or publish the release jar, matching source jar, and checksum file together.
5. Mark the release as 按键精灵社区版（Keyboard Wizard CE）, a Community Edition / unofficial port.

GitHub Release:

```powershell
git remote set-url origin <your-fork-or-ce-repo-url>
git push -u origin ce/neoforge-26.2
git tag neoforge-26.2-v1.0.0
git push origin neoforge-26.2-v1.0.0
```

The `Release CE` workflow builds the bundle and uploads the jars, release zip, and checksum file to the GitHub Release.
