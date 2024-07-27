package com.scouter.brewmaster.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

public class PotionUtil {

    public static final MapCodec<PotionBrewing.Mix<Potion>> POTION_BREWING_MIX_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.POTION.holderByNameCodec().fieldOf("input_potion").forGetter(PotionBrewing.Mix::from),
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(PotionBrewing.Mix::ingredient),
                    BuiltInRegistries.POTION.holderByNameCodec().fieldOf("output_potion").forGetter(PotionBrewing.Mix::to)
            ).apply(instance, PotionBrewing.Mix::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PotionBrewing.Mix<Potion>> POTION_BREWING_MIX_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BuiltInRegistries.POTION.holderByNameCodec()), PotionBrewing.Mix::from,
            Ingredient.CONTENTS_STREAM_CODEC,PotionBrewing.Mix::ingredient,
            ByteBufCodecs.fromCodec(BuiltInRegistries.POTION.holderByNameCodec()), PotionBrewing.Mix::to,
            PotionBrewing.Mix::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionBrewing.Mix<Item>> ITEM_BREWING_MIX_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.holderByNameCodec()), PotionBrewing.Mix::from,
            Ingredient.CONTENTS_STREAM_CODEC,PotionBrewing.Mix::ingredient,
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.holderByNameCodec()), PotionBrewing.Mix::to,
            PotionBrewing.Mix::new
    );
}
