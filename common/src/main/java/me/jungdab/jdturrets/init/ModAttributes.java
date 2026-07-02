package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class ModAttributes {
    public static final Supplier<Holder<Attribute>> ATTACK_RANGE = Services.REGISTRY.registerAttribute(
            "attack_range",
            () -> new RangedAttribute(
                    "attributes.jdturrets.attack_range",
                    0,
                    0,
                    100
            )
    );

    public static void init() {
    }
}
