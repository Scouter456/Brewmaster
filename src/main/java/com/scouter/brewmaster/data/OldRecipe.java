package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.mixin.access.PotionBrewingMixAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record OldRecipe(Potion input, Item ingredient, Potion result){




    public ResourceLocation getInputRL () {
        return createPotionRl(input);
    }

    public ResourceLocation getResultRl () {
        return createPotionRl(result);
    }

    public ResourceLocation createPotionRl(Potion potion) {
        return BuiltInRegistries.POTION.getKey(potion);
    }




    public static final Codec<OldRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.POTION.byNameCodec().fieldOf("input_potion").forGetter(OldRecipe::input),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient").forGetter(OldRecipe::ingredient),
                    BuiltInRegistries.POTION.byNameCodec().fieldOf("output_potion").forGetter(OldRecipe::result)
            ).apply(instance, OldRecipe::new)
    );

    public static List<OldRecipe> fromList(List<PotionBrewing.Mix<Potion>> mixes) {
        List<OldRecipe> oldRecipes = new ArrayList<>();
        for(PotionBrewing.Mix<Potion> pot : mixes) {
            if(((PotionBrewingMixAccessor<Potion>)pot).brewmaster$getIngredient().getItems().length == 0) continue;
            oldRecipes.add(new OldRecipe(((PotionBrewingMixAccessor<Potion>)pot).brewmaster$getFrom(), ((PotionBrewingMixAccessor<Potion>)pot).brewmaster$getIngredient().getItems()[0].getItem(), ((PotionBrewingMixAccessor<Potion>)pot).brewmaster$getTo()));
        }
        return oldRecipes;
    }

    public final PotionBrewing.Mix<Potion> toMix() {
        return new PotionBrewing.Mix<>(input, Ingredient.of(ingredient), result);
    }
}