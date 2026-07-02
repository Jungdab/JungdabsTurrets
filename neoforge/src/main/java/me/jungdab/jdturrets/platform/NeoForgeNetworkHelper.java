package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgeNetworkHelper implements INetworkHelper{
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        PacketDistributor.sendToServer(packet);
    }
}
