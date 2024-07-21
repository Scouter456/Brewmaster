package com.scouter.brewmaster.events;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface PotionBrewingRecipeExtension {

    public void setPotionMixes(List<PotionBrewing.Mix<Potion>> potionMixes);
    public void setMixes(List<PotionBrewing.Mix<Item>> mixes);
    public void setContainer(List<Ingredient> container);

}
