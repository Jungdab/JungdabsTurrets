package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class NeoForgeNetworkHelper implements INetworkHelper{
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        ClientPacketDistributor.sendToServer(packet);
    }
}
