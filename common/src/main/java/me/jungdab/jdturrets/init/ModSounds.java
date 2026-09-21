package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSounds {
    public static final Supplier<SoundEvent> TURRET_SHOOT = Services.REGISTRY.registerSound(
            "turret_shoot",
            () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "turret_shoot"))
    );

    public static final Supplier<SoundEvent> TURRET_PLACE = Services.REGISTRY.registerSound(
            "turret_place",
            () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "turret_place"))
    );

    public static final Supplier<SoundEvent> TURRET_HURT = Services.REGISTRY.registerSound(
            "turret_hurt",
            () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "turret_hurt"))
    );

    public static final Supplier<SoundEvent> TURRET_DESTROY = Services.REGISTRY.registerSound(
            "turret_destroy",
            () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "turret_destroy"))
    );

    public static void init() {}
}
