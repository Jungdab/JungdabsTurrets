# GeckoLib compatibility backport for Minecraft 1.21.2

This is an unofficial compatibility build used by Jungdab's Turrets, not a release from the GeckoLib project.

- Upstream: https://github.com/bernie-g/geckolib
- Base commit: `01c4209736cd9089a208fe94c742024eb78cee96` (branch `1.21.3`)
- Backport version: `4.7.3-jdt.1`
- License: MIT, copyright GeckoThePecko and original contributors; retained in each dependency JAR.
- Additional MIT notices from `EasingType.step` (Boris Chumichev) and McLib's mathematical parser (McHorse; design credits to Eliot Lash and Hiroku) are included in `META-INF/GECKOLIB-THIRD-PARTY-NOTICES.txt`.
- Local Maven coordinates use `me.jungdab.backport.geckolib` to distinguish this build from official artifacts.

The patch targets Minecraft 1.21.2, NeoForm `1.21.2-20241022.151510`, Fabric API `0.106.1+1.21.2`, and NeoForge `21.2.1-beta`. It excludes the Forge project because no 1.21.2 Forge loader exists. The library's Minecraft range is limited to 1.21.2. Upstream publication tasks are disabled.

Build with JDK 21 after checking out the pinned upstream commit and applying `backport.patch`:

The patch pins Gradle 8.10 for Loom 1.8.13. Using the upstream Gradle 8.14.2 wrapper can produce an empty Fabric remap artifact even when Gradle reports success.

```powershell
git apply /path/to/backport.patch
./gradlew.bat :common:jar :fabric:remapJar :neoforge:jar
```

The built artifacts are stored under `vendor/maven/me/jungdab/backport/geckolib` for reproducible local dependency resolution. The loader JAR is embedded in the turret mod's release JAR; players do not need to locate an unavailable official 1.21.2 GeckoLib release.

Status: dependency build and game verification in progress. Do not publish until both loader builds and game tests pass.

The upstream source license and the CurseForge GeckoLib license page both identify MIT. MIT grants modification and redistribution subject to preserving the copyright and permission notices. Platform review remains separate: identify the embedded library as an unofficial compatibility build, credit/link GeckoLib, explain the compatibility changes, and declare the embedded dependency. Relevant rules: https://modrinth.com/legal/rules and https://support.curseforge.com/support/solutions/articles/9000197279.
