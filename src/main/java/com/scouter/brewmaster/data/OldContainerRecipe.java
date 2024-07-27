package com.scouter.brewmaster.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record OldContainerRecipe(Holder<Item> input, Item ingredient, Holder<Item> result){
        public static final MapCodec<OldContainerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("input_item").forGetter(OldContainerRecipe::input),
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient").forGetter(OldContainerRecipe::ingredient),
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("output_item").forGetter(OldContainerRecipe::result)
                        ).apply(instance, OldContainerRecipe::new)
        );

    public static final StreamCodec<RegistryFriendlyByteBuf, OldContainerRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.holderByNameCodec()), OldContainerRecipe::input,
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec()), OldContainerRecipe::ingredient,
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.holderByNameCodec()), OldContainerRecipe::result,
            OldContainerRecipe::new
    );
    public static List<OldContainerRecipe> fromList(List<PotionBrewing.Mix<Item>> mixes) {
        List<OldContainerRecipe> oldRecipes = new ArrayList<>();
        for(PotionBrewing.Mix<Item> pot : mixes) {
            oldRecipes.add(new OldContainerRecipe(pot.from(), pot.ingredient().getItems()[0].getItem(), pot.to()));
        }
        return oldRecipes;
    }

    public final PotionBrewing.Mix<Item> toMix() {
        return new PotionBrewing.Mix<>(input, Ingredient.of(ingredient), result);
    }
}