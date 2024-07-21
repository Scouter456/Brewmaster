package com.scouter.brewmaster.registry;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.data.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BMPotionRecipeRegistry {
    public static final DeferredRegister<PotionBrewingRecipeType<?>> RECIPE_TYPE = DeferredRegister.create(BMRegistries.Keys.POTION_RECIPE_TYPE_SERIALIZER, Brewmaster.MODID);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<AddPotionMixRecipe>> ADD_POTION_MIX = RECIPE_TYPE.register("add_potion_mix", () -> AddPotionMixRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<AddPotionStartMixRecipe>> ADD_POTION_START_MIX = RECIPE_TYPE.register("add_potion_start_mix", () -> AddPotionStartMixRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<ReplacePotionMixRecipe>> REPLACE_POTION_MIX = RECIPE_TYPE.register("replace_potion_mix", () -> ReplacePotionMixRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<RemovePotionMixRecipe>> REMOVE_POTION_MIX = RECIPE_TYPE.register("remove_potion_mix", () -> RemovePotionMixRecipe.TYPE);

    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<AddContainerMixRecipe>> ADD_CONTAINER_MIX = RECIPE_TYPE.register("add_container_mix", () -> AddContainerMixRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<ReplaceContainerMixRecipe>> REPLACE_CONTAINER_MIX = RECIPE_TYPE.register("replace_container_mix", () -> ReplaceContainerMixRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<RemoveContainerMixRecipe>> REMOVE_CONTAINER_MIX = RECIPE_TYPE.register("remove_container_mix", () -> RemoveContainerMixRecipe.TYPE);

    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<AddContainerRecipe>> ADD_CONTAINER = RECIPE_TYPE.register("add_container", () -> AddContainerRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<ReplaceContainerRecipe>> REPLACE_CONTAINER = RECIPE_TYPE.register("replace_container", () -> ReplaceContainerRecipe.TYPE);
    public static final DeferredHolder<PotionBrewingRecipeType<?>, PotionBrewingRecipeType<RemoveContainerRecipe>> REMOVE_CONTAINER = RECIPE_TYPE.register("remove_container", () -> RemoveContainerRecipe.TYPE);




}
