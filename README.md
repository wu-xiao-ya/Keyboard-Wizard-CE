# 按键精灵社区版（Keyboard Wizard CE）

按键精灵社区版（Keyboard Wizard CE）是 Modern KeyWizard / Keyboard Wizard 的社区维护分支，用于补充较新 Minecraft 与 Forge / NeoForge 目标的可用构建。

`main` 分支只作为仓库入口和文档页使用；各版本源码按分支独立维护。

## 支持版本

| 目标 | 源码分支 | Java | 最新分支构建 | 基线 Release |
| --- | --- | --- | --- | --- |
| Minecraft 1.20.1 + Forge 47.4.10+ | `ce/forge-1.20.1` | 17 | GitHub Actions | [`forge-1.20.1-v1.0.0`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/forge-1.20.1-v1.0.0) |
| Minecraft 1.21.1 + NeoForge 21.1.219+ | `ce/neoforge-1.21.1` | 21 | GitHub Actions | [`neoforge-1.21.1-v1.0.0`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-1.21.1-v1.0.0) |
| Minecraft 26.1.2 + NeoForge 26.1.2.x | `ce/neoforge-26.1.2` | 25 | GitHub Actions | [`neoforge-26.1.2-v1.0.0`](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/neoforge-26.1.2-v1.0.0) |
| Minecraft 26.2 + NeoForge 26.2.0.0-beta | `ce/neoforge-26.2` | 25 | [Actions run 27646319618](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/actions/runs/27646319618) | pending |

CE 项目从 `1.0.0` 重新开始版本线。旧 `v2.1.0` tag 属于早期过渡构建，后续发布以 `v1.x.x` 为准。

## 当前功能

- 模组名：按键精灵社区版（Keyboard Wizard CE）。
- Mod id：`keyboard_wizard_ce`。
- 当前 CE 版本：`1.0.0`。
- F7 在游戏内直接打开 Keyboard Wizard CE。
- 支持主键区、小键盘、辅助键三套键位布局切换。
- 控制设置界面右下角提供原版 Controls 与 Keyboard Wizard CE 的切换按钮。
- CE 界面使用独立背景材质，按钮图标使用随模组打包的材质文件。

## 分支策略

- `main` 只放仓库级文档。
- 每个 `ce/*` 分支包含一个目标版本的完整源码。
- 不在版本分支之间直接合并；跨版本改动按目标 API 手动移植。
- 发布 jar 时必须同时提供该 jar 对应的源码。

## 构建

进入对应版本分支后执行：

```powershell
.\gradlew.bat assemble
```

Forge 1.20.1 分支也可以执行：

```powershell
.\gradlew.bat build --warning-mode all
```

26.1.2 构建压力较高，优先使用 GitHub Actions 生成产物。

## 授权与来源

本社区版按 Modern KeyWizard 上游的 AGPL-3.0-only 义务分发。发布二进制 jar 时，需要提供对应源码。

详见 [LICENSE.md](LICENSE.md)、[COPYING.md](COPYING.md)、[NOTICE.md](NOTICE.md)。

- Modern KeyWizard 上游：https://github.com/VulpesStella/ModernKeyWizard
- Keyboard Wizard 上游：https://github.com/Fuzss/keyboardwizard
- Legacy reference：https://github.com/MrNerdy42/keywizard-legacy
