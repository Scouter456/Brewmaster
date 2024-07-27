package com.scouter.brewmaster.setup;/*package com.scouter.jsonblockpatterns.setup;

import com.scouter.jsonblockpatterns.JsonBlockPatterns;
import com.scouter.jsonblockpatterns.registry.JBPRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = JsonBlockPatterns.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static void init(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
        });
    }

    public static void setup(){
    }
    @SubscribeEvent
    private static void registerRegistries(NewRegistryEvent event) {
        event.register(JBPRegistries.BLOCK_PATTERN_BUILDER_TYPE_SERIALIZER);
        event.register(JBPRegistries.BLOCK_PATTERN_TYPE_SERIALIZER);
        event.register(JBPRegistries.KEY_DEFINITION_TYPE_SERIALIZER);

    }
}
*/