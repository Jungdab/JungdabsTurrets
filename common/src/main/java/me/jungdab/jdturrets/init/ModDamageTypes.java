package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> TURRET_SHOOT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, "turret_shoot"));

    public static DamageSource turretShoot(Entity entity) {
        return new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(TURRET_SHOOT), entity) ;
    }
}
