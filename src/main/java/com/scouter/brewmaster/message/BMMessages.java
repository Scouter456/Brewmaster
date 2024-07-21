package com.scouter.brewmaster.message;

import com.scouter.brewmaster.Brewmaster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber(modid = Brewmaster.MODID, bus = EventBusSubscriber.Bus.MOD)
public class BMMessages {



    @SubscribeEvent
    public static void packetRegister(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Brewmaster.MODID)
                .versioned("1.0.0")
                .optional();
        registrar.playToClient(PotionBrewingS2C.TYPE, PotionBrewingS2C.STREAM_CODEC, PotionBrewingS2C::onPacketReceived);
    }
}
