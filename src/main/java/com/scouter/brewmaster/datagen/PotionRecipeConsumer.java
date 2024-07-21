package com.scouter.brewmaster.datagen;

import com.scouter.brewmaster.data.PotionBrewingRecipe;
import net.minecraft.resources.ResourceLocation;

public class PotionRecipeConsumer {

    private ResourceLocation location;
    private PotionBrewingRecipe recipe;

    public PotionRecipeConsumer(ResourceLocation loc, PotionBrewingRecipe recipe) {
        this.location = loc;
        this.recipe = recipe;
    }

    // Getter for location
    public ResourceLocation getLocation() {
        return location;
    }

    // Setter for location
    public void setLocation(ResourceLocation location) {
        this.location = location;
    }

    public PotionBrewingRecipe getRecipe() {
        return recipe;
    }
}
