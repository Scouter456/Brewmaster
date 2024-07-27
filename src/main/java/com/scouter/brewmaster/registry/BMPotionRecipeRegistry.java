package com.scouter.brewmaster.registry;

import com.scouter.brewmaster.data.*;
import net.minecraft.core.Registry;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class BMPotionRecipeRegistry {

    public static final PotionBrewingRecipeType<?> ADD_POTION_MIX =registerBrewingRecipeType("add_potion_mix", AddPotionMixRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> ADD_POTION_START_MIX =registerBrewingRecipeType("add_potion_start_mix", AddPotionStartMixRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REPLACE_POTION_MIX =registerBrewingRecipeType("replace_potion_mix", ReplacePotionMixRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REMOVE_POTION_MIX =registerBrewingRecipeType("remove_potion_mix", RemovePotionMixRecipe.TYPE);

    public static final PotionBrewingRecipeType<?> ADD_CONTAINER_MIX =registerBrewingRecipeType("add_container_mix", AddContainerMixRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REPLACE_CONTAINER_MIX =registerBrewingRecipeType("replace_container_mix", ReplaceContainerMixRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REMOVE_CONTAINER_MIX =registerBrewingRecipeType("remove_container_mix", RemoveContainerMixRecipe.TYPE);

    public static final PotionBrewingRecipeType<?> ADD_CONTAINER =registerBrewingRecipeType("add_container", AddContainerRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REPLACE_CONTAINER =registerBrewingRecipeType("replace_container", ReplaceContainerRecipe.TYPE);
    public static final PotionBrewingRecipeType<?> REMOVE_CONTAINER =registerBrewingRecipeType("remove_container", RemoveContainerRecipe.TYPE);

    private static PotionBrewingRecipeType<?> registerBrewingRecipeType(String name, PotionBrewingRecipeType<?> type) {
        return Registry.register(BMRegistries.POTION_BREWING_RECIPE_TYPE, prefix(name), type);
    }

    
    public static void register()
    {
    }

}
