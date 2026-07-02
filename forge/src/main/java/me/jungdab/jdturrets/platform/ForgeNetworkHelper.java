package me.jungdab.jdturrets.platform;

import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import me.jungdab.jdturrets.network.ForgeNetworking;
import net.minecraftforge.network.PacketDistributor;

public class ForgeNetworkHelper implements INetworkHelper {
    @Override
    public void sendToServer(C2STurretConfigPacket packet) {
        ForgeNetworking.CHANNEL.send(
                packet,
                PacketDistributor.SERVER.noArg()
        );
    }
}
