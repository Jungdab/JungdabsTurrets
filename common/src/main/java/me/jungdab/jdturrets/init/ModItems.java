package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.item.ChipItem;
import me.jungdab.jdturrets.item.TurretItem;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public class ModItems {

    public static final Supplier<Item> WRENCH = Services.REGISTRY.registerItem(
            "wrench",
            () -> new Item(new Item.Properties().stacksTo(1))
    );

    public static final Supplier<Item> TURRET_AMMO = Services.REGISTRY.registerItem(
            "turret_ammo",
            () -> new Item(new Item.Properties())
    );

    public static final Supplier<Item> CHIP_BASE = Services.REGISTRY.registerItem(
            "chip_base",
            () -> new ChipItem(new Item.Properties())
    );

    public static final Supplier<Item> IRON_DAMAGE_CHIP = Services.REGISTRY.registerItem(
            "iron_damage_chip",
            () -> new ChipItem(new Item.Properties(), Component.translatable("item.jdturrets.lore.damage", 25).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_DAMAGE, new AttributeModifier("", 0.25, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> GOLDEN_DAMAGE_CHIP = Services.REGISTRY.registerItem(
            "golden_damage_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.damage", 50).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_DAMAGE, new AttributeModifier("", 0.50, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> DIAMOND_DAMAGE_CHIP = Services.REGISTRY.registerItem(
            "diamond_damage_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.damage", 75).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_DAMAGE, new AttributeModifier("", 0.75, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> NETHERITE_DAMAGE_CHIP = Services.REGISTRY.registerItem(
            "netherite_damage_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.damage", 100).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_DAMAGE, new AttributeModifier("", 100, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> IRON_RELOAD_CHIP = Services.REGISTRY.registerItem(
            "iron_reload_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.reload", 12.5).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_SPEED, new AttributeModifier("", -0.125, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> GOLDEN_RELOAD_CHIP = Services.REGISTRY.registerItem(
            "golden_reload_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.reload", 25).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_SPEED, new AttributeModifier("", -0.25, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> DIAMOND_RELOAD_CHIP = Services.REGISTRY.registerItem(
            "diamond_reload_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.reload", 37.5).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_SPEED, new AttributeModifier("", -0.375, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> NETHERITE_RELOAD_CHIP = Services.REGISTRY.registerItem(
            "netherite_reload_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.reload", 50).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.ATTACK_SPEED, new AttributeModifier("", -0.5, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> IRON_HEALTH_CHIP = Services.REGISTRY.registerItem(
            "iron_health_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.health", 25).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.MAX_HEALTH, new AttributeModifier("", 0.25, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> GOLDEN_HEALTH_CHIP = Services.REGISTRY.registerItem(
            "golden_health_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.health", 50).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.MAX_HEALTH, new AttributeModifier("", 0.5, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> DIAMOND_HEALTH_CHIP = Services.REGISTRY.registerItem(
            "diamond_health_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.health", 75).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.MAX_HEALTH, new AttributeModifier("", 0.75, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> NETHERITE_HEALTH_CHIP = Services.REGISTRY.registerItem(
            "netherite_health_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.health", 100).withStyle(ChatFormatting.GREEN),
                    Map.entry(Attributes.MAX_HEALTH, new AttributeModifier("", 1, AttributeModifier.Operation.MULTIPLY_BASE))
            )
    );

    public static final Supplier<Item> IRON_RANGE_CHIP = Services.REGISTRY.registerItem(
            "iron_range_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.range", 2).withStyle(ChatFormatting.GREEN),
                    Map.entry(ModAttributes.ATTACK_RANGE.get().value(), new AttributeModifier("", 2, AttributeModifier.Operation.ADDITION))
            )
    );


    public static final Supplier<Item> GOLDEN_RANGE_CHIP = Services.REGISTRY.registerItem(
            "golden_range_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.range", 4).withStyle(ChatFormatting.GREEN),
                    Map.entry(ModAttributes.ATTACK_RANGE.get().value(), new AttributeModifier("", 4, AttributeModifier.Operation.ADDITION))
            )
    );

    public static final Supplier<Item> DIAMOND_RANGE_CHIP = Services.REGISTRY.registerItem(
            "diamond_range_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.range", 6).withStyle(ChatFormatting.GREEN),
                    Map.entry(ModAttributes.ATTACK_RANGE.get().value(), new AttributeModifier("", 6, AttributeModifier.Operation.ADDITION))
            )
    );

    public static final Supplier<Item> NETHERITE_RANGE_CHIP = Services.REGISTRY.registerItem(
            "netherite_range_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.range", 8).withStyle(ChatFormatting.GREEN),
                    Map.entry(ModAttributes.ATTACK_RANGE.get().value(), new AttributeModifier("", 8, AttributeModifier.Operation.ADDITION))
            )
    );

    public static final Supplier<Item> POTATO_CHIP = Services.REGISTRY.registerItem(
            "potato_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.potato").withStyle(ChatFormatting.YELLOW))
    );

    public static final Supplier<Item> INFINITY_AMMO_CHIP = Services.REGISTRY.registerItem(
            "infinity_ammo_chip",
            () -> new ChipItem(new Item.Properties(), Component.translatable("item.jdturrets.lore.infinity_ammo").withStyle(ChatFormatting.GREEN))
    );

    public static final Supplier<Item> SELF_REPAIR_CHIP = Services.REGISTRY.registerItem(
            "self_repair_chip",
            () -> new ChipItem(new Item.Properties(),Component.translatable("item.jdturrets.lore.self_repair").withStyle(ChatFormatting.GREEN))
    );

    public static final Supplier<Item> BASIC_TURRET_ITEM = Services.REGISTRY.registerItem(
            "basic_turret_item",
            () -> new TurretItem<>(new Item.Properties(), ModEntities.BASIC_TURRET)
    );

    public static final Supplier<Item> SNIPER_TURRET_ITEM = Services.REGISTRY.registerItem(
            "sniper_turret_item",
            () -> new TurretItem<>(new Item.Properties(), ModEntities.SNIPER_TURRET)
    );

    public static final Supplier<Item> TANK_TURRET_ITEM = Services.REGISTRY.registerItem(
            "tank_turret_item",
            () -> new TurretItem<>(new Item.Properties(), ModEntities.TANK_TURRET)
    );

    public static void init() {};
}
