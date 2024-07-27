package com.scouter.brewmaster.setup;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.registry.BMRegistries;
import org.slf4j.Logger;


public class Registration {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static void init(){

        BMRegistries.init();
        BMPotionRecipeRegistry.register();


    }
}
