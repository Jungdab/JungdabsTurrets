package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.clienttest.ClientSmokeTest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class FabricClientSmoke implements ClientModInitializer {
    @Override
    public void onInitializeClient() { ClientTickEvents.END_CLIENT_TICK.register(ClientSmokeTest::tick); }
}
