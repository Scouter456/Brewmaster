package com.scouter.brewmaster.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record OldRecipe(Holder<Potion> input, Item ingredient, Holder<Potion> result){
        public static final MapCodec<OldRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        BuiltInRegistries.POTION.holderByNameCodec().fieldOf("input_potion").forGetter(OldRecipe::input),
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient").forGetter(OldRecipe::ingredient),
                        BuiltInRegistries.POTION.holderByNameCodec().fieldOf("output_potion").forGetter(OldRecipe::result)
                        ).apply(instance, OldRecipe::new)
        );

    public static final StreamCodec<RegistryFriendlyByteBuf,OldRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BuiltInRegistries.POTION.holderByNameCodec()), OldRecipe::input,
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec()), OldRecipe::ingredient,
            ByteBufCodecs.fromCodec(BuiltInRegistries.POTION.holderByNameCodec()), OldRecipe::result,
            OldRecipe::new
    );
    public static List<OldRecipe> fromList(List<PotionBrewing.Mix<Potion>> mixes) {
        List<OldRecipe> oldRecipes = new ArrayList<>();
        for(PotionBrewing.Mix<Potion> pot : mixes) {
            oldRecipes.add(new OldRecipe(pot.from(), pot.ingredient().getItems()[0].getItem(), pot.to()));
        }
        return oldRecipes;
    }

    public final PotionBrewing.Mix<Potion> toMix() {
        return new PotionBrewing.Mix<Potion>(input, Ingredient.of(ingredient), result);
    }
}