# Minecraft version expansion

Existing projects: [Modrinth](https://modrinth.com/mod/jungdabs-turrets) (`TYLHPOeu`),
[CurseForge](https://www.curseforge.com/minecraft/mc-mods/jungdabs-turrets) (`1595544`).

Requested targets: every stable Java release from 1.21.1 through 1.21.11 and 26.1 through 26.3,
including 26.1.1 and 26.1.2. Preserve existing 1.20.1 and 1.21.1 publications.

## Branches

- `codex/minecraft-multiversion`: support audit and release tracking, based on local `master` at `c57080a`.
- `codex/minecraft-1.21.3`: three loader builds; Fabric server tests and user gameplay passed.
- `codex/minecraft-1.21.4`: three loader builds and Fabric server tests passed; client verification pending.
- `codex/minecraft-1.21.5`: GeckoLib 5 port; three loader builds, Fabric server and client tests passed.
- `codex/minecraft-1.21.6`: port in progress.

## Verification and publication

The unmodified 1.21.1 baseline builds on Fabric, NeoForge and Forge with JDK 21 and Gradle 8.10.
It currently has no automated gameplay tests.

The 1.21.3 port adds a separate Fabric GameTest source set. It checks all three turret types'
inventory/owner/target configuration persistence, damage, ammunition, upgrade removal, and recipe loading.
Run `gradlew.bat :fabric:runGameTest` in its worktree. Test classes are excluded from release JARs.

Before uploading a new version:

1. Build each advertised loader's JAR against that exact Minecraft version.
2. Run server gameplay tests and inspect logs for recipe, registry and mixin errors.
3. Verify client startup, all turret models/animations, bullet traces and configuration screens.
4. Test multiplayer configuration changes and world save/reload.
5. Record artifact hashes and test results, then add files to the existing hosting projects.

For all future uploads, follow the user's requested order on both platforms: ascending Minecraft version, then **Forge → NeoForge → Fabric** within each version. A pending test or fix in an earlier file holds later uploads. Explicitly document unavailable upstream loader/dependency combinations. Preserve files already published before this ordering instruction.

The first 1.21.5 manual test found a missing GeckoLib model on placement. Fixed by moving models and animations to GeckoLib 5 resource directories. Added `:fabric:runClientGameTest` to check baked models and animations, place all three turret items, render bullet traces and open every turret configuration screen. All checks now pass, with screenshots saved in `fabric/build/client-gametest/screenshots`.

1.21.2 has no official GeckoLib Maven or Modrinth release, and 1.21.3 artifacts require Minecraft >=1.21.3. A separate local GeckoLib source backport is being investigated for Fabric and NeoForge; it must pass actual builds and runtime tests before any release. The Forge Maven version list has no 1.21.2 loader. Do not advertise support by relabelling 1.21.3 files.

## Dependency discovery

Run `./scripts/Get-VersionSupport.ps1` in PowerShell to refresh `version-support.json`.
The report records Modrinth listings, not a compatibility guarantee or a build result.
Missing entries require further upstream investigation. In particular, it must not be used to
relabel existing 1.21.1 JARs as compatible with newer releases.

On this Windows machine, Java's default Unix-domain socket temporary path prevented Gradle
from starting. Setting `jdk.net.unixdomain.tmpdir` and `java.io.tmpdir` to a short writable
directory fixed it. JDK 21 is installed at `C:/Program Files/Java/jdk-21`.


## Latest published files

| Minecraft | Loader | Modrinth | CurseForge |
| --- | --- | --- | --- |
| 1.21.3 | Fabric | DdtCyIpt | 8940288 (approved) |
| 1.21.3 | NeoForge | kL3iSWqx | 8940362 (submitted, under review) |
| 1.21.5 | Fabric | 1r11DdZL | 8940387 (submitted, processing) |

NeoForge 1.21.3 was tested by the user for placement, attacks and configuration screens before upload.
