package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import me.jungdab.jdturrets.network.ForgeNetworking;

public class ForgeNetworkHelper implements INetworkHelper {
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        ForgeNetworking.CHANNEL.sendToServer(
                packet
        );
    }
}
