# 按键精灵社区版（Keyboard Wizard CE）

按键精灵社区版（Keyboard Wizard CE）是 Modern KeyWizard / Keyboard Wizard 的社区维护分支，用于补充多个 Minecraft 与 Forge / NeoForge / Fabric 目标的可用构建。

`main` 分支只作为仓库入口和文档页使用；各版本源码按分支独立维护。

## 支持版本

| 目标 | 源码分支 | Java | 最新分支构建 | 基线 Release |
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

旧 Fabric 1.16.5 / 1.18.2 分支未发布且不再维护；Fabric 支持从 1.20.1 起。

## 当前功能

- 模组名：按键精灵社区版（Keyboard Wizard CE）。
- Mod id：`keyboard_wizard_ce`。
- 当前 CE 版本线：`1.0.x`。
- 支持 F7 在游戏内直接打开 Keyboard Wizard CE。
- 支持主键区、小键盘、辅助键三套键位布局切换。
- 支持按模组分类、按功能键名称搜索、按按键反查绑定。
- 左侧热键列表显示热键名称、绑定按键和所属模组/分类。
- 控制设置界面提供原版 Controls 与 Keyboard Wizard CE 的切换按钮。
- CE 界面使用独立背景材质，按钮图标使用随模组打包的材质文件。

## 授权与来源

本社区版按 Modern KeyWizard 上游的 AGPL-3.0-only 义务分发。发布二进制 jar 时，需要提供对应源码。

详见 [LICENSE.md](LICENSE.md)、[COPYING.md](COPYING.md)、[NOTICE.md](NOTICE.md)。

- Modern KeyWizard 上游：https://github.com/VulpesStella/ModernKeyWizard
- Keyboard Wizard 上游：https://github.com/Fuzss/keyboardwizard
- Legacy reference：https://github.com/MrNerdy42/keywizard-legacy
- 美术资源：WI_8614_ice
