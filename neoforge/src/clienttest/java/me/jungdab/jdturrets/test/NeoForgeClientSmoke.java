package me.jungdab.jdturrets.test;

import me.jungdab.jdturrets.clienttest.ClientSmokeTest;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@Mod("jdturrets_clienttest")
public class NeoForgeClientSmoke {
    @EventBusSubscriber(modid = "jdturrets_clienttest", value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onTick(ClientTickEvent.Post event) { ClientSmokeTest.tick(Minecraft.getInstance()); }
    }
}
