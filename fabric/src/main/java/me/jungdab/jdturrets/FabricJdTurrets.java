package me.jungdab.jdturrets;

import me.jungdab.jdturrets.entity.BasicTurret;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class FabricJdTurrets implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
        initAttribute();
        initPayloads();
    }

    private void initAttribute() {
        FabricDefaultAttributeRegistry.register(ModEntities.BASIC_TURRET.get(), BasicTurret.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntities.SNIPER_TURRET.get(), SniperTurret.createAttribute());
        FabricDefaultAttributeRegistry.register(ModEntities.TANK_TURRET.get(), TankTurret.createAttribute());
    }

    private void initPayloads() {
        PayloadTypeRegistry.serverboundPlay().register(
                C2STurretConfigPacket.TYPE,
                C2STurretConfigPacket.STREAM_CODEC
        );
        ServerPlayNetworking.registerGlobalReceiver(C2STurretConfigPacket.TYPE, (packet, context) -> packet.handle(context.player()));
    }
}
