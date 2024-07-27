package com.scouter.brewmaster.setup;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.message.BMMessages;
import com.scouter.brewmaster.registry.BMRegistries;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.NewRegistryEvent;

@Mod.EventBusSubscriber(modid =Brewmaster.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            BMMessages.register();
        });
    }


    //@SubscribeEvent
    //public static void registerRegistries(NewRegistryEvent event) {
    //    event.create(BMRegistries.POTION_BREWING_RECIPE_TYPE_BUILDER);
    //    //event.register(AHRegistries.WORLD_MODFICATION_TYPE_SERIALIZER);
    //}

    public static void setup(){
    }
}