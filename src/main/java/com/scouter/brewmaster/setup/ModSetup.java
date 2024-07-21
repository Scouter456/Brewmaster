package com.scouter.brewmaster.setup;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.registry.BMRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = Brewmaster.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
        });
    }


    @SubscribeEvent
    private static void registerRegistries(NewRegistryEvent event) {
        event.register(BMRegistries.POTION_BREWING_RECIPE_TYPE);
        //event.register(AHRegistries.WORLD_MODFICATION_TYPE_SERIALIZER);
    }

    public static void setup(){
    }
}