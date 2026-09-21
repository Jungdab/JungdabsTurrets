package me.jungdab.jdturrets.item;

import me.jungdab.jdturrets.init.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;

public class ChipItem extends Item {
    public ChipItem(Properties properties) {
        super(properties.component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.ATTRIBUTE_MODIFIERS, true)));
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);

        if (enchantments != null && enchantments.getLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY)) > 0) {
            ItemStack newStack = new ItemStack(ModItems.INFINITY_AMMO_CHIP.get());
            if (entity instanceof Player player) {
                for (int slotId = 0; slotId < player.getInventory().getContainerSize(); slotId++) {
                    if (player.getInventory().getItem(slotId) == stack) {
                        player.getInventory().setItem(slotId, newStack);
                        break;
                    }
                }
            }
        }
        super.inventoryTick(stack, level, entity, equipmentSlot);
    }
}

