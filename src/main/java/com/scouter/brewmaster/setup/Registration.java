package com.scouter.brewmaster.setup;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;


public class  Registration {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){

        IEventBus bus = ModLoadingContext.get().getActiveContainer().getEventBus();
        //AHEventCueRegistry.EVENT_CUE_SERIALIZER.register(bus);
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
