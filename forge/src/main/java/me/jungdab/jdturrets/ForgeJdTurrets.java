package me.jungdab.jdturrets;

import me.jungdab.jdturrets.client.renderer.BasicTurretRenderer;
import me.jungdab.jdturrets.client.renderer.SniperTurretRenderer;
import me.jungdab.jdturrets.client.renderer.TankTurretRenderer;
import me.jungdab.jdturrets.client.screen.TurretContainerScreen;
import me.jungdab.jdturrets.entity.BasicTurret;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.init.ModMenuTypes;
import me.jungdab.jdturrets.network.ForgeNetworking;
import me.jungdab.jdturrets.platform.ForgeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgeJdTurrets {

    public ForgeJdTurrets() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonClass.init();
        ForgeRegistry.init(modEventBus);
        ForgeNetworking.init();
        modEventBus.register(this);
    }
    @SubscribeEvent
    public void initAttribute(EntityAttributeCreationEvent e) {
        e.put(ModEntities.BASIC_TURRET.get(), BasicTurret.createAttribute().build());
        e.put(ModEntities.SNIPER_TURRET.get(), SniperTurret.createAttribute().build());
        e.put(ModEntities.TANK_TURRET.get(), TankTurret.createAttribute().build());
    }

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.BASIC_TURRET.get(), BasicTurretRenderer::new);
            EntityRenderers.register(ModEntities.SNIPER_TURRET.get(), SniperTurretRenderer::new);
            EntityRenderers.register(ModEntities.TANK_TURRET.get(), TankTurretRenderer::new);

            event.enqueueWork(() -> MenuScreens.register(ModMenuTypes.TURRET_CONTAINER_MENU.get(), TurretContainerScreen::new));
        }
    }
}