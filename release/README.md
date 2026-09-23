# Minecraft version ports

The existing 1.20.1 and 1.21.1 releases are preserved. New ports target each
stable version from 1.21.2 through 1.21.11, then 26.1, 26.1.1, 26.1.2, 26.2
and 26.3. All new releases support NeoForge and Fabric; Forge is excluded.

All version branches have been built and exercised in actual clients.
The 26.x tests verify placement, animation progress on initial GUI opening
and reopening, configuration synchronization, recipes, damage, ammunition,
and save/load behavior. See [the animation regression report](preview-animation-fix.md).
Full artifact hashes, individual test evidence and upload state are in
[verification.json](verification.json).

## Publication order and current state

All 30 new artifacts have been published on Modrinth and submitted to
CurseForge. Statuses below were checked on 2026-09-23 at 18:25 KST;
CurseForge automatically publishes the remaining files once approved.

Upload in ascending Minecraft version order and **NeoForge → Fabric** within
each version, on both platforms. A pending test or fix blocks publication
of that file and later files. Review by CurseForge can continue after a
verified submission. Existing files predating this ordering instruction
are retained.

| Minecraft | Loader | Modrinth | CurseForge |
| --- | --- | --- | --- |
| 1.21.2 | neoforge | [CJJrQ1zE](https://modrinth.com/mod/jungdabs-turrets/version/CJJrQ1zE) | [8941656 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8941656) |
| 1.21.2 | fabric | [HaARmWGC](https://modrinth.com/mod/jungdabs-turrets/version/HaARmWGC) | [8941697 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8941697) |
| 1.21.3 | neoforge | [kL3iSWqx](https://modrinth.com/mod/jungdabs-turrets/version/kL3iSWqx) | [8940362 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8940362) |
| 1.21.3 | fabric | [DdtCyIpt](https://modrinth.com/mod/jungdabs-turrets/version/DdtCyIpt) | [8940288 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8940288) |
| 1.21.4 | neoforge | [lRyArzpO](https://modrinth.com/mod/jungdabs-turrets/version/lRyArzpO) | [8941724 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8941724) |
| 1.21.4 | fabric | [W2R9yIGF](https://modrinth.com/mod/jungdabs-turrets/version/W2R9yIGF) | [8941763 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8941763) |
| 1.21.5 | neoforge | [DxUHCokL](https://modrinth.com/mod/jungdabs-turrets/version/DxUHCokL) | [8941783 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8941783) |
| 1.21.5 | fabric | [1r11DdZL](https://modrinth.com/mod/jungdabs-turrets/version/1r11DdZL) | [8940387 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8940387) |
| 1.21.6 | neoforge | [6EHl08rR](https://modrinth.com/mod/jungdabs-turrets/version/6EHl08rR) | [8953109 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953109) |
| 1.21.6 | fabric | [HyX7bCvI](https://modrinth.com/mod/jungdabs-turrets/version/HyX7bCvI) | [8953131 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953131) |
| 1.21.7 | neoforge | [uKNgNA6K](https://modrinth.com/mod/jungdabs-turrets/version/uKNgNA6K) | [8953153 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953153) |
| 1.21.7 | fabric | [4kTGFpVx](https://modrinth.com/mod/jungdabs-turrets/version/4kTGFpVx) | [8953653 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953653) |
| 1.21.8 | neoforge | [ESuwK3no](https://modrinth.com/mod/jungdabs-turrets/version/ESuwK3no) | [8953696 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953696) |
| 1.21.8 | fabric | [RNNh3Roq](https://modrinth.com/mod/jungdabs-turrets/version/RNNh3Roq) | [8953753 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953753) |
| 1.21.9 | neoforge | [W0V0gF9O](https://modrinth.com/mod/jungdabs-turrets/version/W0V0gF9O) | [8953792 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953792) |
| 1.21.9 | fabric | [S0pFMU6m](https://modrinth.com/mod/jungdabs-turrets/version/S0pFMU6m) | [8953812 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953812) |
| 1.21.10 | neoforge | [uTq0nFTp](https://modrinth.com/mod/jungdabs-turrets/version/uTq0nFTp) | [8953828 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953828) |
| 1.21.10 | fabric | [3PvAoxBz](https://modrinth.com/mod/jungdabs-turrets/version/3PvAoxBz) | [8953848 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953848) |
| 1.21.11 | neoforge | [Augl2kAO](https://modrinth.com/mod/jungdabs-turrets/version/Augl2kAO) | [8953852 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953852) |
| 1.21.11 | fabric | [PxWyvKtJ](https://modrinth.com/mod/jungdabs-turrets/version/PxWyvKtJ) | [8953857 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953857) |
| 26.1 | neoforge | [3OaY9S3k](https://modrinth.com/mod/jungdabs-turrets/version/3OaY9S3k) | [8953875 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953875) |
| 26.1 | fabric | [FET56HTp](https://modrinth.com/mod/jungdabs-turrets/version/FET56HTp) | [8953914 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953914) |
| 26.1.1 | neoforge | [ETAQAa1Y](https://modrinth.com/mod/jungdabs-turrets/version/ETAQAa1Y) | [8953922 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953922) |
| 26.1.1 | fabric | [16ZfLsuA](https://modrinth.com/mod/jungdabs-turrets/version/16ZfLsuA) | [8953953 (Approved)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953953) |
| 26.1.2 | neoforge | [gmOZC7LO](https://modrinth.com/mod/jungdabs-turrets/version/gmOZC7LO) | [8953962 (Under Review)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953962) |
| 26.1.2 | fabric | [dXyOvUuJ](https://modrinth.com/mod/jungdabs-turrets/version/dXyOvUuJ) | [8953967 (Under Review)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953967) |
| 26.2 | neoforge | [B0kXKjlU](https://modrinth.com/mod/jungdabs-turrets/version/B0kXKjlU) | [8953973 (Under Review)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953973) |
| 26.2 | fabric | [7bVLl4Nq](https://modrinth.com/mod/jungdabs-turrets/version/7bVLl4Nq) | [8953984 (Under Review)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953984) |
| 26.3 | neoforge | [Ds3eGbqd](https://modrinth.com/mod/jungdabs-turrets/version/Ds3eGbqd) | [8953988 (Under Review)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8953988) |
| 26.3 | fabric | [aIRSXCcH](https://modrinth.com/mod/jungdabs-turrets/version/aIRSXCcH) | [8954001 (Processing)](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets/files/8954001) |

## Dependencies and sources

Minecraft 1.21.x requires Java 21; Minecraft 26.x requires Java 25.
Fabric releases require Fabric API. Use the GeckoLib build for the exact
Minecraft version shown in each release's notes.

Minecraft 1.21.2 and 1.21.9 include unofficial MIT-licensed GeckoLib source
backports because the corresponding official artifact is unavailable.
These are embedded dependencies, with upstream attribution, license,
sources and patches under `vendor/geckolib` in their version branches.
They do not require a separate GeckoLib download. Other versions use
official GeckoLib releases as external required dependencies.

Branches are named `codex/minecraft-<version>` and have been pushed to
https://github.com/Jungdab/JungdabsTurrets. The root 1.20.1 checkout is
unchanged. Worktrees are under `.worktrees/<version>`.

## Reproducing validation

Run `:fabric:runGameTest` for server behavior and `:fabric:runClientGameTest`
for actual Fabric rendering. Recent NeoForge ports provide
`:neoforge:runClientSmokeTest -PclientSmokeTest` for integrated client/server
validation. Test source sets are excluded from production JARs.

On this Windows host, set `JAVA_HOME` to the appropriate installed JDK,
`GRADLE_USER_HOME` to the workspace `.gradle` directory, and both Java
temporary-directory properties to the workspace `.tmp` directory.
