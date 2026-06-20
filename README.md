# Keyboard Wizard CE

Keyboard Wizard CE is a community-maintained distribution of Modern KeyWizard / Keyboard Wizard for multiple Minecraft loader targets.

The `main` branch is the repository landing page and documentation branch. Version-specific source code is maintained on separate branches.

## Supported Targets

| Target | Source branch | Java | Latest branch build | Baseline release |
| --- | --- | --- | --- | --- |
| Minecraft 1.12.2 + Forge / Cleanroom | `ce/forge-1.12.2` | 8 | [Actions run 27872807776](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27872807776) | - |
| Minecraft 1.16.5 + Forge | `ce/forge-1.16.5` | 8 | [Actions run 27818845763](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27818845763) | - |
| Minecraft 1.18.2 + Forge | `ce/forge-1.18.2` | 17 | [Actions run 27818850228](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27818850228) | - |
| Minecraft 1.20.1 + Fabric | `ce/fabric-1.20.1` | 17 | [Actions run 27875789463](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27875789463) | - |
| Minecraft 1.20.1 + Forge | `ce/forge-1.20.1` | 17 | [Actions run 27646830595](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27646830595) | [`forge-1.20.1-v1.0.1`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/forge-1.20.1-v1.0.1) |
| Minecraft 1.21.1 + Fabric | `ce/fabric-1.21.1` | 21 | [Actions run 27876181334](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27876181334) | - |
| Minecraft 1.21.1 + NeoForge | `ce/neoforge-1.21.1` | 21 | [Actions run 27646836029](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27646836029) | [`neoforge-1.21.1-v1.0.1`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-1.21.1-v1.0.1) |
| Minecraft 26.1.2 + Fabric | `ce/fabric-26.1.2` | 25 | [Actions run 27876944618](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27876944618) | - |
| Minecraft 26.1.2 + NeoForge | `ce/neoforge-26.1.2` | 25 | [Actions run 27646857844](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27646857844) | [`neoforge-26.1.2-v1.0.1`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-26.1.2-v1.0.1) |
| Minecraft 26.2 + Fabric | `ce/fabric-26.2` | 25 | [Actions run 27876944805](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27876944805) | - |
| Minecraft 26.2 + NeoForge | `ce/neoforge-26.2` | 25 | [Actions run 27647213392](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27647213392) | [`neoforge-26.2-v1.0.0`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-26.2-v1.0.0) |

Old Fabric 1.16.5 and 1.18.2 branches were never released and are no longer maintained. Fabric support starts at Minecraft 1.20.1.

## Current Features

- Mod id: `keyboard_wizard_ce`.
- Current CE version line: `1.0.x`.
- F7 opens Keyboard Wizard CE in game.
- Main keyboard, numpad, and auxiliary key layouts.
- Filtering by mod/category, key binding name search, and reverse lookup by physical key.
- Left-side key binding rows show binding name, assigned key, and owning mod/category.
- Controls screen toggle button for switching between vanilla Controls and Keyboard Wizard CE.
- CE background texture and bundled texture button icons.

## License And Credits

This community edition follows the upstream Modern KeyWizard AGPL-3.0-only obligations. If you publish a binary jar, provide the matching source code for that exact build.

See [LICENSE.md](LICENSE.md), [COPYING.md](COPYING.md), and [NOTICE.md](NOTICE.md).

- Modern KeyWizard upstream: https://github.com/VulpesStella/ModernKeyWizard
- Keyboard Wizard upstream: https://github.com/Fuzss/keyboardwizard
- Legacy reference: https://github.com/MrNerdy42/keywizard-legacy
- Art assets: WI_8614_ice
