# Keyboard Wizard CE Release Checklist

Build the shared-core repository's NeoForge 26.2 target with Java 25 and NeoForge 26.2.0.0-beta. From `targets/neoforge-26.2`, run `bash ./gradlew assemble releaseBundle` on Linux or `.\gradlew.bat assemble releaseBundle` on Windows.

- Binary JAR `build/libs/keyboard-wizard-ce-neoforge-26.2-1.0.1.jar`
- Source JAR `build/libs/keyboard-wizard-ce-neoforge-26.2-1.0.1-sources.jar`
- Release bundle `build/release/keyboard-wizard-ce-neoforge-26.2-1.0.1-release.zip`
- Checksums `build/release/SHA256SUMS.txt`

From the repository root, run `python tools/verify-target.py targets/neoforge-26.2`. Distribute the release bundle, or distribute the binary JAR with its corresponding source JAR and checksums.
