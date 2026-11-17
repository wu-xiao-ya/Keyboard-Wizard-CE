# Keyboard Wizard CE · 共享核心

这个分支在同一份源码中维护共享核心和各 Minecraft 目标。改动核心后，GitHub Actions 从同一个提交构建所有已接入目标。

| 目标 | 导入基线 | 构建 JDK | 迁移阶段 |
| --- | --- | --- | --- |
| Forge 1.20.1 | `e9fd013` | 17 | 首批 |
| Fabric 1.21.1 | `64811ec` | 21 | 首批 |
| NeoForge 26.3 | `70124d7:neoforge-26.3` | 25 | 首批 |
| Forge 1.18.2 | `c71c4a0` | 17 | 第二批第 1 个 |
| Forge 1.16.5 | `8df2b23` | 17（产物兼容 Java 8） | 第二批第 2 个 |
| Forge 1.12.2 / Cleanroom | `9b1ff94` | 8 | 第二批第 3 个 |
| NeoForge 1.21.1 | `d0b6414` | 21 | 像素界面试验 |
| NeoForge 1.21.4 | `26cbe95:targets/neoforge-1.21.1` | 21 | 从 1.21.1 移植 |
| NeoForge 26.1.2 | `ab159e9` | 25 | 独立分支接入 |
| NeoForge 26.2 | `2e8a6cc` | 25 | 独立分支接入 |

Fabric 覆盖 Minecraft Java 版 1.20 至 26.3 的 24 个正式版本。1.20 至 1.20.4 用 JDK 17 构建，1.20.5 至 1.21.11 用 JDK 21，26.1 至 26.3 用 JDK 25。各版本的构建目标和产物名称见工作流。

## 安装和升级

从 [GitHub Releases](https://github.com/wu-xiao-ya/Keyboard-Wizard-CE/releases) 下载与 Minecraft 版本和加载器对应的模组 JAR。升级到 1.1.1 时，先移除旧的 Keyboard Wizard CE JAR，再把新版放入 `mods` 文件夹。

1.1.1 的模组 ID 为 `keyboard_wizard_community_edition`。现有按键配置继续使用原有的键名。自定义资源包需要把该模组的资源移到 `assets/keyboard_wizard_community_edition/`。

## 架构

- `shared-core/` 维护 Java 8 兼容的搜索、分类、键盘布局和绑定接口。
- `shared-core/src/main/resources/` 维护界面切换图标、背景和模组标志。所有目标从这里打包，修改画面素材时只需更新这一处。
- `PixelKeyboardStyle` 统一全部 33 个目标的银灰主题面板、三色键帽和导航图标。每个目标的实际界面和键盘组件都调用这份绘制逻辑。Fabric 与 NeoForge 的 26.3 目标还共用 `targets/gui-26.3/` 下的游戏界面组件。
- `targets/glfw-keys/` 和 `targets/sdl-keys/` 把布局使用的物理键标识转换成游戏使用的键码。
- `targets/<loader>-<version>/` 管理加载器入口、界面、游戏绑定和资源。各目标的 Gradle 构建读取同一份核心源码，并把它打进各自的模组 JAR。

## 验证

GitHub Actions 在分支推送时测试核心、独立构建已接入的目标，并上传各目标的 JAR、匹配源码和 SHA-256。制品校验还会检查实际界面、键盘键帽和像素控件之间的调用，防止包含共享类的旧界面包通过验证。需要单独验证核心时，在 `shared-core/` 使用 JDK 17 运行 `./gradlew test`。

对同一提交输出的 JAR 逐个验证 F7、原版控制设置入口、三套键盘布局、文字搜索、按键反查和主键盘 Enter 与小键盘 Enter 的区分。验证改键、清除、还原、重启后保留设置，以及适用目标的 Hide Key Binding 兼容。Forge 1.16.5 使用 Java 8 启动，1.12.2 分别用 Forge 和 Cleanroom 验证。

## 授权与致谢

项目采用 AGPL-3.0-only。发布二进制 JAR 时应提供对应源码。具体授权和致谢见根目录及对应目标下的 LICENSE、COPYING 和 NOTICE。
