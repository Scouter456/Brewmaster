package com.scouter.brewmaster.setup;

import com.scouter.brewmaster.message.PotionBrewingS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(PotionBrewingS2C.TYPE, PotionBrewingS2C::onPacketReceived);
    }
}
