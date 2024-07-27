package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.scouter.brewmaster.registry.BMRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface PotionBrewingRecipe {

    Codec<PotionBrewingRecipe> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() ->BMRegistries.POTION_BREWING_RECIPE_TYPE_SERIALIZERS_SUPP.get().getCodec().dispatch(PotionBrewingRecipe::type, PotionBrewingRecipeType::codec));


    void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes);

    void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes);

    void removeContainerMixes(List<PotionBrewing.Mix<Item>> mixes);

    void addContainerMixes(List<PotionBrewing.Mix<Item>> mixes);

    List<Ingredient> removeContainers(List<Ingredient> containers);


    List<Ingredient> addContainers(List<Ingredient> containers);

    PotionBrewingRecipeType<? extends PotionBrewingRecipe> type();

}
