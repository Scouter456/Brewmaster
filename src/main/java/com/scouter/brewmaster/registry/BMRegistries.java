package com.scouter.brewmaster.registry;

import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class BMRegistries {
    static { init(); }
    public static final Registry<PotionBrewingRecipeType<?>> POTION_BREWING_RECIPE_TYPE = new RegistryBuilder<>(Keys.POTION_RECIPE_TYPE_SERIALIZER).maxId(Integer.MAX_VALUE - 1).sync(true).create();


    public static final class Keys {
        public static final ResourceKey<Registry<PotionBrewingRecipeType<?>>> POTION_RECIPE_TYPE_SERIALIZER = key(prefix("potion_recipe_type_serializer").toString());
        public static final ResourceKey<Registry<PotionBrewingRecipe>> POTION_RECIPE = key(prefix("potion_recipe").toString());

        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(ResourceLocation.parse(name));
        }
        private static void init() {}

    }

    private static void init()
    {
        Keys.init();
    }
}