# Changelog

## Fabric texture asset restore - 2026-07-07

- Restored binary PNG GUI assets on all newly added Fabric stable gap branches after detecting corrupted background, toggle, and icon resources in packaged jars.
- Rebuilt and redeployed the local test jars from Fabric 1.21.2 through 26.1.1.
- Verified GitHub Actions builds:
  - Fabric 1.20.2: `28859633895`
  - Fabric 1.20.3: `28859635803`
  - Fabric 1.20.4: `28859638110`
  - Fabric 1.20.5: `28859641369`
  - Fabric 1.20.6: `28859644815`
  - Fabric 1.21: `28859647798`
  - Fabric 1.21.2: `28859650059`
  - Fabric 1.21.3: `28859652487`
  - Fabric 1.21.4: `28859654552`
  - Fabric 1.21.5: `28859656790`
  - Fabric 1.21.6: `28859658889`
  - Fabric 1.21.7: `28859661079`
  - Fabric 1.21.8: `28859663370`
  - Fabric 1.21.9: `28859665359`
  - Fabric 1.21.10: `28859667415`
  - Fabric 1.21.11: `28859669528`
  - Fabric 26.1: `28859672310`
  - Fabric 26.1.1: `28859674268`
## Fabric stable gap rollout - 2026-07-06

- Added stable Fabric support branches from Minecraft 1.20.2 through 1.21.11 plus 26.1 and 26.1.1.
- Kept the existing Fabric 26.3 line as `ce/refactor/internal-api-fabric-26.3` snapshot-only support until a formal 26.3 release exists.
- Verified GitHub Actions builds:
  - Fabric 1.20.2: `28753530011`
  - Fabric 1.20.3: `28753531007`
  - Fabric 1.20.4: `28753532063`
  - Fabric 1.20.5: `28753533337`
  - Fabric 1.20.6: `28753534552`
  - Fabric 1.21: `28753535644`
  - Fabric 1.21.2: `28753536619`
  - Fabric 1.21.3: `28753537824`
  - Fabric 1.21.4: `28753538823`
  - Fabric 1.21.5: `28753539839`
  - Fabric 1.21.6: `28753540990`
  - Fabric 1.21.7: `28753541920`
  - Fabric 1.21.8: `28753542985`
  - Fabric 1.21.9: `28753544101`
  - Fabric 1.21.10: `28753545219`
  - Fabric 1.21.11: `28753546477`
  - Fabric 26.1: `28753547832`
  - Fabric 26.1.1: `28753549370`

## Fabric 1.20.1+ support - 2026-06-20

- Added Fabric CE support branches:
  - `ce/fabric-1.20.1`
  - `ce/fabric-1.21.1`
  - `ce/fabric-26.1.2`
  - `ce/fabric-26.2`
- Verified GitHub Actions builds:
  - Fabric 1.20.1: `27875789463`
  - Fabric 1.21.1: `27876181334`
  - Fabric 26.1.2: `27876944618`
  - Fabric 26.2: `27876944805`
- Dropped planned support for old Fabric 1.16.5 and 1.18.2 branches.

## Forge compatibility expansion - 2026-06-20

- Added and verified Forge 1.12.2 CE support, including the modern texture toggle icon.
- Verified additional Forge branch builds:
  - Forge 1.12.2: `27872807776`
  - Forge 1.16.5: `27818845763`
  - Forge 1.18.2: `27818850228`

## 1.0.1 CE - 2026-06-19

- Published updated release tags for the maintained production branches:
  - `forge-1.20.1-v1.0.1`
  - `neoforge-1.21.1-v1.0.1`
  - `neoforge-26.1.2-v1.0.1`
- Added the left-side key binding mod/category label on those branches so similar bindings from different mods are easier to distinguish.

## NeoForge 26.2 release - 2026-06-17

- Published `neoforge-26.2-v1.0.0` from Actions run `27647213392`.
- Release includes the key category localization fix and matching source/checksum artifacts.

## Localization fix - 2026-06-17

- Added `key.category.keyboard_wizard_ce.bindings` on all maintained branches so the Keyboard Wizard CE category name renders correctly in Controls screens.
- Latest verified Actions builds at that time: Forge 1.20.1 `27646830595`, NeoForge 1.21.1 `27646836029`, NeoForge 26.1.2 `27646857844`, NeoForge 26.2 `27647213392`.

## 26.2 preview branch - 2026-06-17

- Added `ce/neoforge-26.2` for Minecraft 26.2 + NeoForge 26.2.0.0-beta.
- Ported the 26.1.2 CE feature set to the 26.2 screen API changes.
- Verified GitHub Actions build `27646123159`.
- Runtime testing completed; current published tag is `neoforge-26.2-v1.0.0`.

## 1.0.0 CE - 2026-06-17

- Restarted the Community Edition version line at `1.0.0`.
- Updated supported release tags to:
  - `forge-1.20.1-v1.0.0`
  - `neoforge-1.21.1-v1.0.0`
  - `neoforge-26.1.2-v1.0.0`
- Kept the existing CE feature set: direct F7 opening, main / numpad / auxiliary layouts, CE background texture, Controls screen toggle button, and `keyboard_wizard_ce` metadata.

## Unreleased branch builds - 2026-06-16

- Added a repository status update for all maintained CE branches.
- Documented the current branch build artifacts from GitHub Actions separately from the v2.1.0 release tags.
- Documented the current CE feature set: direct F7 opening, main / numpad / auxiliary layout switching, CE background texture, and Controls screen toggle button.
- Documented recent branch fixes:
  - Forge 1.20.1 now registers the Controls screen toggle through the Forge screen init event.
  - NeoForge 1.21.1 and 26.1.2 now render the Controls screen toggle from packaged GUI textures instead of missing atlas sprites.
  - CE mod identity and metadata are aligned on `keyboard_wizard_ce`.

## 2.1.0 CE - 2026-06-15

- Published Keyboard Wizard CE as a community-maintained CE distribution.
- Added version branches for:
  - Minecraft 1.20.1 + Forge
  - Minecraft 1.21.1 + NeoForge
  - Target 26.1.2 + NeoForge
- Added GitHub Actions build and release workflows for each version branch.
- Added AGPL license files, CE notices, release documentation, and SHA-256 release checksums.
- Bundled README, NOTICE, COPYING, and LICENSE files into published jars.
