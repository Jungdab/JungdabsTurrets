# GeckoLib compatibility backport for Minecraft 1.21.9

This is an unofficial compatibility build used by Jungdab's Turrets, maintained separately from the GeckoLib project.

- Upstream: https://github.com/bernie-g/geckolib
- Base commit: `91fe873495815f8f723dcd1f1bee2ccf9fb929f0` (branch `1.21.10`)
- Backport version: `5.3-alpha-3-jdt.1`
- License: MIT, copyright 2024 GeckoLib; original authors and contributors are retained in metadata.
- Additional MIT notices for `EasingType.step` (Boris Chumichev) and the McLib mathematical parser (McHorse; original design credits to Eliot Lash and Hiroku) are retained in `META-INF/GECKOLIB-THIRD-PARTY-NOTICES.txt` in each JAR.
- Local Maven group: `me.jungdab.backport.geckolib`.

The source is rebuilt against Minecraft 1.21.9, NeoForm `1.21.9-20250930.151910`, Parchment `1.21.9:2025.10.05`, NeoForge `21.9.16-beta`, and Fabric API `0.134.1+1.21.9`. Loader metadata accepts only Minecraft 1.21.9. Forge is excluded, and upstream publication tasks are disabled.

To reproduce with JDK 21, check out the pinned upstream commit and apply `backport.patch`:

```powershell
git apply /path/to/backport.patch
./gradlew.bat :common:jar :neoforge:jar :fabric:remapJar
```

The build uses Gradle 8.14.3 and pinned Loom 1.11.8. Artifacts are stored under `vendor/maven/me/jungdab/backport/geckolib`; each loader JAR is embedded in that loader's turret release JAR. No unavailable official 1.21.9 GeckoLib download is required.

Validation completed:

- Rebuilt common, NeoForge and Fabric GeckoLib JARs from source against Minecraft 1.21.9.
- Built both turret release JARs and verified that the correct loader library is embedded with its original license and third-party notices.
- Passed all five Fabric server game tests (the four turret checks plus the framework's baseline check).
- Passed actual NeoForge and Fabric integrated client tests for all three turret placements, rendering, tracers and configuration screens; all eight screenshots were inspected.
- NeoForge's integrated client also verified configuration packets, recipes, damage, ammunition, inventory synchronization and save/load persistence.

Commands used from the turret project with JDK 21:

```powershell
./gradlew.bat :neoforge:build :fabric:build :fabric:runGameTest
./gradlew.bat :neoforge:runClientSmokeTest :fabric:runClientGameTest -PclientSmokeTest
```

The shared `pack.mcmeta` is omitted because Minecraft 1.21.9 has different metadata validation rules for resource and data packs. Fabric and NeoForge provide the appropriate metadata for bundled mod resources; both client runs and the server tests loaded all turret resources and 24 recipes without pack metadata errors.
