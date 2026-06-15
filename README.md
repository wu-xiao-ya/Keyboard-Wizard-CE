# 按键精灵社区版（Keyboard Wizard CE）

Community-maintained builds of Modern KeyWizard / Keyboard Wizard for newer Minecraft mod loaders.

This `main` branch is the repository portal. Version-specific source code is maintained on separate branches.

## Supported Versions

| Minecraft / Loader | Source branch | Release tag |
| --- | --- | --- |
| Minecraft 1.20.1 + Forge | `ce/forge-1.20.1` | `forge-1.20.1-v2.1.0` |
| Minecraft 1.21.1 + NeoForge | `ce/neoforge-1.21.1` | `neoforge-1.21.1-v2.1.0` |
| Target 26.1.2 + NeoForge | `ce/neoforge-26.1.2` | `neoforge-26.1.2-v2.1.0` |

Downloads are published from GitHub Releases:

- [Forge 1.20.1 release](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/forge-1.20.1-v2.1.0)
- [NeoForge 1.21.1 release](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-1.21.1-v2.1.0)
- [NeoForge 26.1.2 release](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-26.1.2-v2.1.0)

## Branch Policy

- `main` contains repository-level documentation only.
- Each `ce/*` branch contains the full source for one supported target.
- Release tags point to the exact source commit used to build each published jar.
- Do not merge version branches into each other. Port changes deliberately between branches when needed.

## License

This community edition is distributed under AGPL-3.0-only to preserve the upstream Modern KeyWizard license obligations.

If you distribute a jar, provide the corresponding source code for that exact build. The release bundles include license and notice files, plus SHA-256 checksums.

See [LICENSE.md](LICENSE.md), [COPYING.md](COPYING.md), and [NOTICE.md](NOTICE.md).

## Upstream

- Modern KeyWizard upstream: https://github.com/VulpesStella/ModernKeyWizard
- Keyboard Wizard upstream: https://github.com/Fuzss/keyboardwizard
