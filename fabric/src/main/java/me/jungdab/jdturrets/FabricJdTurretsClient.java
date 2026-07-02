package me.jungdab.jdturrets;

import me.jungdab.jdturrets.client.renderer.BasicTurretRenderer;
import me.jungdab.jdturrets.client.renderer.SniperTurretRenderer;
import me.jungdab.jdturrets.client.renderer.TankTurretRenderer;
import me.jungdab.jdturrets.client.screen.TurretContainerScreen;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.init.ModMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class FabricJdTurretsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.BASIC_TURRET.get(), BasicTurretRenderer::new);
        EntityRendererRegistry.register(ModEntities.SNIPER_TURRET.get(), SniperTurretRenderer::new);
        EntityRendererRegistry.register(ModEntities.TANK_TURRET.get(), TankTurretRenderer::new);

        MenuScreens.register(ModMenuTypes.TURRET_CONTAINER_MENU.get(), TurretContainerScreen::new);
    }
}
