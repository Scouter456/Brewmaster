package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;

public interface PotionBrewingRecipeType <T extends PotionBrewingRecipe> {

    Codec<T> codec();

}
