package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;

public interface INetworkHelper {
    void sendToServer(C2STurretConfigPacket packet);
}
