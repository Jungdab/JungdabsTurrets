package me.jungdab.jdturrets.network;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record C2STurretConfigPacket (
        int turretId,
        int configType,
        int value
) {
    public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "c2s_turret_config_packet");

    public static final int TARGET_FILTER = 0;
    public static final int TARGETING_MODE = 1;

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(turretId);
        buf.writeInt(configType);
        buf.writeInt(value);
    }

    public static C2STurretConfigPacket read(FriendlyByteBuf buf) {
        return new C2STurretConfigPacket(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void handle(C2STurretConfigPacket packet, ServerPlayer player) {
        if(player == null) return;

        Entity entity = player.level().getEntity(packet.turretId);
        if (!(entity instanceof AbstractTurret turret)) return;
        switch (packet.configType) {
            case 0 -> turret.targetFilter = (byte) packet.value;
            case 1 -> turret.targetingMode = packet.value;
        }
    }
}
