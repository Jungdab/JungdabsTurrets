package me.jungdab.jdturrets;


import me.jungdab.jdturrets.client.renderer.BasicTurretRenderer;
import me.jungdab.jdturrets.client.renderer.SniperTurretRenderer;
import me.jungdab.jdturrets.client.renderer.TankTurretRenderer;
import me.jungdab.jdturrets.client.screen.TurretContainerScreen;
import me.jungdab.jdturrets.entity.BasicTurret;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import me.jungdab.jdturrets.init.*;
import me.jungdab.jdturrets.platform.NeoForgeRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(Constants.MOD_ID)
public class NeoForgeJdTurrets {

    public NeoForgeJdTurrets(IEventBus eventBus) {
        CommonClass.init();
        NeoForgeRegistry.init(eventBus);
        eventBus.register(this);
    }

    @SubscribeEvent
    public void initAttribute(EntityAttributeCreationEvent e) {
        e.put(ModEntities.BASIC_TURRET.get(), BasicTurret.createAttribute().build());
        e.put(ModEntities.SNIPER_TURRET.get(), SniperTurret.createAttribute().build());
        e.put(ModEntities.TANK_TURRET.get(), TankTurret.createAttribute().build());
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.BASIC_TURRET.get(), BasicTurretRenderer::new);
            EntityRenderers.register(ModEntities.SNIPER_TURRET.get(), SniperTurretRenderer::new);
            EntityRenderers.register(ModEntities.TANK_TURRET.get(), TankTurretRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterMenus(RegisterMenuScreensEvent e) {
            e.register(ModMenuTypes.TURRET_CONTAINER_MENU.get(), TurretContainerScreen::new);
        }
    }
}