package me.jungdab.jdturrets.network;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record C2STurretConfigPacket (
        int turretId,
        int configType,
        int value
) implements CustomPacketPayload {
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

    public void handle(ServerPlayer player) {
        if(player == null) return;

        Entity entity = player.level().getEntity(turretId);
        if (!(entity instanceof AbstractTurret turret)) return;
        switch (configType) {
            case 0 -> turret.targetFilter = (byte) value;
            case 1 -> turret.targetingMode = value;
        }
    }

    public static final Type<C2STurretConfigPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "c2s_turret_config_packet"));

    public static final StreamCodec<FriendlyByteBuf, C2STurretConfigPacket> STREAM_CODEC = new StreamCodec<>() {
        public C2STurretConfigPacket decode(FriendlyByteBuf byteBuf) {return C2STurretConfigPacket.read(byteBuf);}
        public void encode(FriendlyByteBuf o, C2STurretConfigPacket packet) {packet.write(o);}
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
