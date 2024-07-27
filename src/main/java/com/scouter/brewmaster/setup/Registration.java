package com.scouter.brewmaster.setup;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.registry.BMRegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


public class  Registration {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        //AHEventCueRegistry.EVENT_CUE_SERIALIZER.register(bus);

        BMRegistries.POTION_BREWING_RECIPE_TYPE_SERIALIZERS.register(bus);
        BMPotionRecipeRegistry.RECIPE_TYPE.register(bus);
        //AHItems.ITEMS.register(bus);
        //AHCustomLevelRendererRegistry.CUSTOM_LEVEL_RENDERER.register(bus);
        //WorldModificationRegistry.WORLD_MODIFICATION.register(bus);
        //BlockPatternBuilderRegistry.BLOCK_PATTERN_SERIALIZER.register(bus);
        //BlockPatternRegistry.BLOCK_DEFINITION_SERIALIZER.register(bus);
        //KeyDefinitionRegistry.KEY_DEFINITION_SERIALIZER.register(bus);

        //PMAnimationBuilderRegistry.ANIMATION_TYPE_SERIALIZER.register(bus);
        //PMEntityTypesRegistry.ENTITY_TYPE_SERIALIZER.register(bus);


    }
}
