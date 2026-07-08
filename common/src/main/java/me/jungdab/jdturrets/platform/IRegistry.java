package me.jungdab.jdturrets.platform;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface IRegistry {
    <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);
    Supplier<Holder<Attribute>> registerAttribute(String id, Supplier<Attribute> attribute);
    Supplier<SoundEvent> registerSound(String id, Supplier<SoundEvent> sound);
    <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String id, MyMenuSupplier<T> menuType);
    <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entityType);
    Supplier<CreativeModeTab> registerCreativeTab(String id, Supplier<CreativeModeTab> creativeModeTab);

    @FunctionalInterface
    interface MyMenuSupplier<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory);
    }
}
