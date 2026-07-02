package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricNetWorkHelper implements INetworkHelper{
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        ClientPlayNetworking.send(packet);
    }
}
