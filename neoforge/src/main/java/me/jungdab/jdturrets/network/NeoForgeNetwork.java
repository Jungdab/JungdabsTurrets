package me.jungdab.jdturrets.network;

import me.jungdab.jdturrets.Constants;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeNetwork {
    @SubscribeEvent
    static void init(RegisterPayloadHandlersEvent e) {
        PayloadRegistrar registrar = e.registrar("1");
        registrar.playToServer(
                C2STurretConfigPacket.TYPE,
                C2STurretConfigPacket.STREAM_CODEC,
                (packet, ctx) -> packet.handle((ServerPlayer) ctx.player())
        );
    }
}
