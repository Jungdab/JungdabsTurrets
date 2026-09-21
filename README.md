![Logo](https://cdn.modrinth.com/data/cached_images/85b1a9c7c9925725864cf1b2f4df099e4dbd4bb2.png)

**Minecraft 1.21.2: Fabric and NeoForge compatibility port. Both loaders pass actual client/server gameplay verification.**

This version bundles an unofficial GeckoLib 4 backport because upstream GeckoLib did not release a Minecraft 1.21.2 build. Fabric API is still required on Fabric. The bundled dependency retains GeckoLib's MIT license and contributor credits; see `vendor/geckolib/README.md` for its source and build instructions. Forge did not release a loader for Minecraft 1.21.2.

Development verification with JDK 21:

```powershell
./gradlew.bat :neoforge:build :fabric:build :fabric:runGameTest
./gradlew.bat :neoforge:runClientSmokeTest -PclientSmokeTest
./gradlew.bat :fabric:runClientSmokeTest
```

Client smoke tests create a separate flat world, place all three turret items, verify configuration packets, recipes, damage, ammunition and NBT persistence, render the turret models/tracer and open each menu. They save screenshots and a pass marker in each loader's `build/client-smoke` folder. Test classes are excluded from release JARs.


This is a simple mod that adds various turrets to the game.
Upgrade your turrets with chips and use the targeting system to shoot your friends, fend off monsters, or hunt down animals.

Mix it up and have fun!



**Repair: Right-click the turret with Iron to repair it.**

**Retrieve: Right-click the turret with a Wrench to pick it up as an item.**

![wrench](https://cdn.modrinth.com/data/cached_images/7abce9f515e9954a571c12124253d576ded03da5_0.webp)


# **Basic Turret**
![basic_turret](https://cdn.modrinth.com/data/cached_images/8515ed61634d528901e9219d5bb987c73d8de352.png)

# **Sniper Turret**
![sniper_turret](https://cdn.modrinth.com/data/cached_images/447876d07458abe92470dbac3e8e0c1220568217.png)

# **Tank Turret**
![tank_turret](https://cdn.modrinth.com/data/cached_images/05fec4f85eefa89cf1938f08a86c38d8468b1f5f.png)

# **Recipe**
![recipe](https://cdn.modrinth.com/data/cached_images/4e6fb9218a5300d966422655eba1984ed61c01e8.png)
