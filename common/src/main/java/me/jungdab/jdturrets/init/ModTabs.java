package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class ModTabs {
    public static final Supplier<CreativeModeTab> MOD_TAB = Services.REGISTRY.registerCreativeTab(
            "mob_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP,1)
                    .title(Component.translatable("itemGroup.jdturrets.mob_tab"))
                    .icon(() -> ModItems.CHIP_BASE.get().getDefaultInstance())
                    .displayItems((param, output) -> {
                        output.accept(ModItems.WRENCH.get());
                        output.accept(ModItems.BASIC_TURRET_ITEM.get());
                        output.accept(ModItems.SNIPER_TURRET_ITEM.get());
                        output.accept(ModItems.TANK_TURRET_ITEM.get());
                        output.accept(ModItems.TURRET_AMMO.get());
                        output.accept(ModItems.CHIP_BASE.get());

                        output.accept(ModItems.IRON_DAMAGE_CHIP.get());
                        output.accept(ModItems.IRON_HEALTH_CHIP.get());
                        output.accept(ModItems.IRON_RANGE_CHIP.get());
                        output.accept(ModItems.IRON_RELOAD_CHIP.get());

                        output.accept(ModItems.GOLDEN_DAMAGE_CHIP.get());
                        output.accept(ModItems.GOLDEN_HEALTH_CHIP.get());
                        output.accept(ModItems.GOLDEN_RANGE_CHIP.get());
                        output.accept(ModItems.GOLDEN_RELOAD_CHIP.get());

                        output.accept(ModItems.DIAMOND_DAMAGE_CHIP.get());
                        output.accept(ModItems.DIAMOND_HEALTH_CHIP.get());
                        output.accept(ModItems.DIAMOND_RANGE_CHIP.get());
                        output.accept(ModItems.DIAMOND_RELOAD_CHIP.get());

                        output.accept(ModItems.NETHERITE_DAMAGE_CHIP.get());
                        output.accept(ModItems.NETHERITE_HEALTH_CHIP.get());
                        output.accept(ModItems.NETHERITE_RANGE_CHIP.get());
                        output.accept(ModItems.NETHERITE_RELOAD_CHIP.get());

                        output.accept(ModItems.INFINITY_AMMO_CHIP.get());
                        output.accept(ModItems.SELF_REPAIR_CHIP.get());
                        output.accept(ModItems.POTATO_CHIP.get());
                    })
                    .build()
    );

    public static void init() {}
}
