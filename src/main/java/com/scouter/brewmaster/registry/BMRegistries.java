package com.scouter.brewmaster.registry;

import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipeType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class BMRegistries {
    static { init(); }
    public static final Registry<PotionBrewingRecipeType<?>> POTION_BREWING_RECIPE_TYPE = FabricRegistryBuilder.createSimple(Keys.POTION_RECIPE_TYPE_SERIALIZER)
            .attribute(RegistryAttribute.SYNCED)
             .buildAndRegister();


    public static final class Keys {
        public static final ResourceKey<Registry<PotionBrewingRecipeType<?>>> POTION_RECIPE_TYPE_SERIALIZER = key(prefix("potion_recipe_type_serializer").toString());
        public static final ResourceKey<Registry<PotionBrewingRecipe>> POTION_RECIPE = key(prefix("potion_recipe").toString());

        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(ResourceLocation.parse(name));
        }
        private static void init() {}

    }

    public static void init()
    {
        Keys.init();
    }
}
