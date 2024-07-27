package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public record OldContainerRecipe(Item input, Item ingredient, Item result){
        public static final Codec<OldContainerRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("input_item").forGetter(OldContainerRecipe::input),
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient").forGetter(OldContainerRecipe::ingredient),
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("output_item").forGetter(OldContainerRecipe::result)
                        ).apply(instance, OldContainerRecipe::new)
        );
    public ResourceLocation getInputRL () {
        return createPotionRl(input);
    }

    public ResourceLocation getResultRl () {
        return createPotionRl(result);
    }

    public ResourceLocation createPotionRl(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

    public static List<OldContainerRecipe> fromList(List<PotionBrewing.Mix<Item>> mixes) {
        List<OldContainerRecipe> oldRecipes = new ArrayList<>();
        for(PotionBrewing.Mix<Item> pot : mixes) {
            if(pot.ingredient.getItems().length == 0) continue;
            oldRecipes.add(new OldContainerRecipe(pot.from.get(), pot.ingredient.getItems()[0].getItem(), pot.to.get()));
        }
        return oldRecipes;
    }

    public final PotionBrewing.Mix<Item> toMix() {
        return new PotionBrewing.Mix<>(ForgeRegistries.ITEMS,input, Ingredient.of(ingredient), result);
    }
}