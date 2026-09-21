package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.entity.BasicTurret;
import me.jungdab.jdturrets.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {
    public static final Supplier<EntityType<BasicTurret>> BASIC_TURRET = Services.REGISTRY.registerEntity(
            "basic_turret",
            () -> EntityType.Builder
                    .of(BasicTurret::new, MobCategory.MISC)
                    .sized(1,1)
                    .build(key("basic_turret"))
    );

    public static final Supplier<EntityType<SniperTurret>> SNIPER_TURRET = Services.REGISTRY.registerEntity(
            "sniper_turret",
            () -> EntityType.Builder
                    .of(SniperTurret::new, MobCategory.MISC)
                    .sized(1,1)
                    .build(key("sniper_turret"))
    );

    public static final Supplier<EntityType<TankTurret>> TANK_TURRET = Services.REGISTRY.registerEntity(
            "tank_turret",
            () -> EntityType.Builder
                    .of(TankTurret::new, MobCategory.MISC)
                    .sized(1,1)
                    .build(key("tank_turret"))
    );

    private static ResourceKey<EntityType<?>> key(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    public static void init() {}
}
