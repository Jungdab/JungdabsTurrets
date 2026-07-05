package me.jungdab.jdturrets.network;

import me.jungdab.jdturrets.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ForgeNetworking {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MOD_ID, "main"),
            () -> "1",
            "1"::equals,
            "1"::equals
    );

    public static void init() {
        int id = 0;

        CHANNEL.registerMessage(
                id,
                C2STurretConfigPacket.class,
                C2STurretConfigPacket::write,
                C2STurretConfigPacket::read,
                (payload, ctxSupplier) -> {
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(() -> C2STurretConfigPacket.handle(payload, ctx.getSender()));
                    ctx.setPacketHandled(true);
                },
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}
