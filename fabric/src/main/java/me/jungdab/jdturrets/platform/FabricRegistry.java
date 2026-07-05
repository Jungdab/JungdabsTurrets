package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricRegistry implements IRegistry {
    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        var object = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, id), item.get());
        return () -> object;
    }

    @Override
    public Supplier<Holder<Attribute>> registerAttribute(String id, Supplier<Attribute> attribute) {
        var object = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, new ResourceLocation(Constants.MOD_ID, id), attribute.get());
        return () -> object;
    }

    @Override
    public Supplier<SoundEvent> registerSound(String id, Supplier<SoundEvent> sound) {
        var object = Registry.register(BuiltInRegistries.SOUND_EVENT, new ResourceLocation(Constants.MOD_ID, id), sound.get());
        return () -> object;
    }

    @Override
    public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(String id, MyMenuSupplier<T> menuType) {
        var object = Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MOD_ID, id), new MenuType<>(menuType::create, FeatureFlags.DEFAULT_FLAGS));
        return () -> object;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entityType) {
        var object = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, id), entityType.get());
        return () -> object;
    }

    @Override
    public Supplier<CreativeModeTab> registerCreativeTab(String id, Supplier<CreativeModeTab> creativeModeTab) {
        var object = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MOD_ID, id), creativeModeTab.get());
        return () -> object;
    }
}
