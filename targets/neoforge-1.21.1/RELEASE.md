# Keyboard Wizard CE Release Checklist

Build the shared-core repository's NeoForge 1.21.1 target with Java 21 and NeoForge 21.1.219. From `targets/neoforge-1.21.1`, run `bash ./gradlew assemble releaseBundle` on Linux or `.\gradlew.bat assemble releaseBundle` on Windows.

- Binary JAR `build/libs/keyboard-wizard-ce-neoforge-1.21.1-1.1.0.jar`
- Source JAR `build/libs/keyboard-wizard-ce-neoforge-1.21.1-1.1.0-sources.jar`
- Release bundle `build/release/keyboard-wizard-ce-neoforge-1.21.1-1.1.0-release.zip`
- Checksums `build/release/SHA256SUMS.txt`

From the repository root, run `python tools/verify-target.py targets/neoforge-1.21.1`. Distribute the release bundle, or distribute the binary JAR with its corresponding source JAR and checksums.
