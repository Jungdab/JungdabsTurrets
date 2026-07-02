package me.jungdab.jdturrets.network;

import me.jungdab.jdturrets.Constants;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ForgeNetworking {
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "main")).simpleChannel();

    public static void init() {
        CHANNEL.messageBuilder(C2STurretConfigPacket.class)
                .direction(PacketFlow.SERVERBOUND)
                .encoder(C2STurretConfigPacket::write)
                .decoder(C2STurretConfigPacket::read)
                .consumerMainThread((packet, ctx) -> {
                    packet.handle(ctx.getSender());
                })
                .add();
    }
}
