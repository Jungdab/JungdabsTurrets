package me.jungdab.jdturrets.init;

import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static Supplier<MenuType<TurretContainerMenu>> TURRET_CONTAINER_MENU = Services.REGISTRY.registerMenuType("turret_container_menu", TurretContainerMenu::new);

    public static void init() {}
}
