package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class FabricNetWorkHelper implements INetworkHelper{
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.write(buf);
        ClientPlayNetworking.send(C2STurretConfigPacket.ID, buf);
    }
}
