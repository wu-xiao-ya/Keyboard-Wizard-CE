# 按键精灵社区版（Keyboard Wizard CE） Release Checklist

- Branch: `ce/forge-1.20.1`
- Target: `Minecraft 1.20.1` + `Forge 47.4.20`
- Java: `17`
- Build command: `.\gradlew.bat build --warning-mode all`
- Release jar: `build/libs/mkw-forge-1.20.1-2.1.0.jar`
- Source jar: `build/libs/mkw-forge-1.20.1-2.1.0-sources.jar`

Before publishing:

1. Confirm `README.md`, `NOTICE.md`, `LICENSE.md`, and `COPYING.md` are present.
2. Build from a clean checkout.
3. Publish the release jar and the matching source jar together.
4. Mark the release as 按键精灵社区版（Keyboard Wizard CE）, a Community Edition / unofficial port.
