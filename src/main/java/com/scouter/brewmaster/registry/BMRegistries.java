package com.scouter.brewmaster.registry;

import com.mojang.serialization.Codec;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class BMRegistries {
    static { init(); }
    //public static final DeferredRegister<PotionBrewingRecipeType<?>> POTION_BREWING_RECIPE_TYPE = DeferredRegister.create(Keys.POTION_RECIPE_TYPE_SERIALIZER, Keys.POTION_RECIPE_TYPE_SERIALIZER.location().getNamespace());
    //public static final Supplier<IForgeRegistry<PotionBrewingRecipeType<?>>> POTION_BREWING_RECIPE_TYPE_SUPPLIER = POTION_BREWING_RECIPE_TYPE.makeRegistry(() -> new RegistryBuilder<PotionBrewingRecipeType<?>>().disableSaving());

    public static final RegistryBuilder<PotionBrewingRecipeType<?>> POTION_BREWING_RECIPE_TYPE_BUILDER = makeRegistry(Keys.POTION_RECIPE_TYPE_SERIALIZER);

    public static final DeferredRegister<PotionBrewingRecipeType<?>> POTION_BREWING_RECIPE_TYPE_SERIALIZERS = DeferredRegister.create(Keys.POTION_RECIPE_TYPE_SERIALIZER, Keys.POTION_RECIPE_TYPE_SERIALIZER.location().getNamespace());
    public static final Supplier<IForgeRegistry<PotionBrewingRecipeType<?>>> POTION_BREWING_RECIPE_TYPE_SERIALIZERS_SUPP = POTION_BREWING_RECIPE_TYPE_SERIALIZERS.makeRegistry(() -> POTION_BREWING_RECIPE_TYPE_BUILDER);




    private static <T> RegistryBuilder<T> makeRegistry(ResourceKey<? extends Registry<T>> key)
    {
        return new RegistryBuilder<T>().setName(key.location()).setMaxID(Integer.MAX_VALUE - 1);
    }
    public static final class Keys {
        public static final ResourceKey<Registry<PotionBrewingRecipeType<?>>> POTION_RECIPE_TYPE_SERIALIZER = key(prefix("potion_recipe_type_serializer").toString());
        public static final ResourceKey<Registry<PotionBrewingRecipe>> POTION_RECIPE = key(prefix("potion_recipe").toString());

        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(new  ResourceLocation(name));
        }
        private static void init() {}

    }

    private static void init()
    {
        Keys.init();
    }
}
