**简体中文** | [English](README.en.md)

# 按键精灵社区版（Keyboard Wizard CE）

按键精灵社区版是面向 Minecraft Java 版的 Modern KeyWizard 和 Keyboard Wizard 社区维护版本。你可以在交互式键盘界面中查找、查看和修改按键绑定。

[下载 v1.1.0](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases/tag/v1.1.0) · [共享源码](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/tree/codex/shared-core) · [历史版本](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases)

## 支持的 Minecraft 版本

v1.1.0 共提供 33 个构建目标，覆盖 Fabric、Forge 和 NeoForge。其中 Fabric 支持从 1.20 到 26.3 的 24 个正式版本。

| 加载器 | Minecraft 版本 | Java |
| --- | --- | --- |
| Fabric | 1.20, 1.20.1, 1.20.2, 1.20.3, 1.20.4 | 17 |
| Fabric | 1.20.5, 1.20.6, 1.21, 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.7, 1.21.8, 1.21.9, 1.21.10, 1.21.11 | 21 |
| Fabric | 26.1, 26.1.1, 26.1.2, 26.2, 26.3 | 25 |
| Forge | 1.12.2, 1.16.5 | 8 |
| Forge | 1.18.2, 1.20.1 | 17 |
| NeoForge | 1.21.1, 1.21.4 | 21 |
| NeoForge | 26.1.2, 26.2, 26.3 | 25 |

## 安装和升级

1. 从发布页下载与 Minecraft 版本和加载器匹配的 `-1.1.0.jar`。使用 Fabric 时，还需安装对应 Minecraft 版本的 Fabric API。
2. 将下载的 JAR 放入 `mods` 文件夹。升级时，先移除其中旧版按键精灵社区版的 JAR。
3. 已保存的按键绑定沿用原有的配置键名。如果自定义资源包覆盖了本模组的资源，请将这些资源从 `assets/keyboard_wizard_ce/` 移到 `assets/keyboard_wizard_community_edition/`。如果整合包引用了旧模组 ID，请改用完整的模组 ID `keyboard_wizard_community_edition`。

发布页还为每个构建提供对应的 `-sources.jar`，并附有 `SHA256SUMS.txt` 校验文件。

## 功能

- 在游戏中按 **F7**，或使用原版控制设置界面的切换按钮，打开按键精灵社区版。
- 查看主键盘、数字小键盘和辅助按键布局。
- 按模组或类别筛选，按绑定名称搜索，或按物理按键反查绑定。
- 在侧栏查看绑定名称、已分配的按键及其所属模组或类别。
- 银灰像素主题和配套键盘图标适用于 Fabric 26.3，以及 NeoForge 1.21.1、1.21.4 和 26.3。

## 源码和开发历史

当前开发集中在 `codex/shared-core` 分支，33 个目标基于同一份共享代码构建。完整的目标矩阵见[构建工作流](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/blob/codex/shared-core/.github/workflows/shared-core.yml)。

早期构建按 Minecraft 版本分别维护。旧的 Fabric 1.16.5 和 1.18.2 分支从未发布。[历史更新记录](CHANGELOG.md)收录了早期分支构建，包括 Fabric 26.3 的快照版构建。

## 许可证和致谢

按键精灵社区版采用 AGPL-3.0-only 许可证。分发二进制 JAR 时，请一并提供对应的源码。详见 [LICENSE.md](LICENSE.md)、[COPYING.md](COPYING.md) 和 [NOTICE.md](NOTICE.md)。

- [Modern KeyWizard 上游项目](https://github.com/VulpesStella/ModernKeyWizard)
- [Keyboard Wizard 上游项目](https://github.com/Fuzss/keyboardwizard)
- [Keyboard Wizard 旧版参考](https://github.com/MrNerdy42/keywizard-legacy)
- 美术资源由 WI_8614_ice 提供
