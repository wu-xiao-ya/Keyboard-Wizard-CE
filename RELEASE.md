# 按键精灵社区版（Keyboard Wizard CE） Release Checklist

- Branch: `ce/forge-1.20.1`
- Target: `Minecraft 1.20.1` + `Forge 47.4.20`
- Java: `17`
- Build command: `.\gradlew.bat build --warning-mode all`
- Bundle command: `.\gradlew.bat releaseBundle`
- Release jar: `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0.jar`
- Source jar: `build/libs/keyboard-wizard-ce-forge-1.20.1-2.1.0-sources.jar`
- Release bundle: `build/release/keyboard-wizard-ce-forge-1.20.1-2.1.0-release.zip`
- Checksum file: `build/release/SHA256SUMS.txt`
- Changelog: `CHANGELOG.md`
- Release tag pattern: `forge-1.20.1-v*`

Before publishing:

1. Confirm `README.md`, `NOTICE.md`, `LICENSE.md`, and `COPYING.md` are present.
2. Build from a clean checkout.
3. Run `.\gradlew.bat releaseBundle`.
4. Publish the release bundle, or publish the release jar, matching source jar, and checksum file together.
5. Mark the release as 按键精灵社区版（Keyboard Wizard CE）, a Community Edition / unofficial port.

GitHub Release:

```powershell
git remote set-url origin <your-fork-or-ce-repo-url>
git push -u origin ce/forge-1.20.1
git tag forge-1.20.1-v2.1.0
git push origin forge-1.20.1-v2.1.0
```

The `Release CE` workflow builds the bundle and uploads the jars, release zip, and checksum file to the GitHub Release.
