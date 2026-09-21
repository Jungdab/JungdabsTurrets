# Minecraft 1.21.10 verification

NeoForge 21.10.64 and Fabric API 0.138.4+1.21.10, with GeckoLib 5.3-alpha-3 and Java 21.
Forge is excluded for Minecraft 1.21 and newer at the project owner's request.

Both release builds passed. Fabric server game tests passed all five required tests
(four turret tests and the built-in control): 24 recipes, all three turret inventories
and owner/settings persistence, damage/ammunition/infinity, and chip modifiers.

Actual Fabric and NeoForge clients passed placement and rendering of all three
turrets, visible bullet trace, and all three configuration screens. All eight
screenshots were visually inspected for models, previews, labels and slots.
The NeoForge integrated test additionally verified actual C2S settings packets,
server damage, ammunition consumption, recipes and inventory/owner/settings save/load.

Commands:

```powershell
./gradlew.bat :neoforge:build :fabric:build
./gradlew.bat :fabric:runGameTest :fabric:runClientGameTest
./gradlew.bat :neoforge:runClientSmokeTest -PclientSmokeTest
```

Local evidence: `verification.log`, `verification-final.log`,
`fabric/build/gametest-results.xml`, `fabric/build/client-gametest/screenshots/`,
`neoforge/build/client-smoke/smoke-result.txt`, and its `screenshots/` directory.
Test source sets are excluded from release JARs, verified by inspecting both JARs.
The NeoForge test source set is activated only with `-PclientSmokeTest`.

No publication was performed in this branch. Publish after lower Minecraft versions,
NeoForge before Fabric.
