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
import net.minecraft.world.level.Level;

public class ChipItem extends Item {
    public ChipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);

        if (enchantments != null && enchantments.getLevel(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.INFINITY)) > 0) {
            ItemStack newStack = new ItemStack(ModItems.INFINITY_AMMO_CHIP.get());
            if (entity instanceof Player player) {
                player.getInventory().setItem(slotId, newStack);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}
