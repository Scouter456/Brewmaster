package com.scouter.brewmaster.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class AddPotionStartMixRecipe implements PotionBrewingRecipe {

    public static final MapCodec<AddPotionStartMixRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.either(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").codec(), TagKey.codec(Registries.ITEM).fieldOf("tag").codec()).fieldOf("ingredient").forGetter(predicate ->
                            predicate.item != null ? Either.left(predicate.item) : Either.right(predicate.itemTagKey)
                    ),
                    BuiltInRegistries.POTION.holderByNameCodec().fieldOf("output_potion").forGetter(AddPotionStartMixRecipe::getResult)
            ).apply(instance, AddPotionStartMixRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, AddPotionStartMixRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.either(ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec()), ByteBufCodecs.fromCodec(TagKey.codec(Registries.ITEM))), AddPotionStartMixRecipe::getEither,
            ByteBufCodecs.fromCodec(BuiltInRegistries.POTION.holderByNameCodec()), AddPotionStartMixRecipe::getResult,
            AddPotionStartMixRecipe::new
    );

    public static final PotionBrewingRecipeType<AddPotionStartMixRecipe> TYPE = new PotionBrewingRecipeType<AddPotionStartMixRecipe>() {
        @Override
        public MapCodec<AddPotionStartMixRecipe> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AddPotionStartMixRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    };


    private final Item item;
    private final TagKey<Item> itemTagKey;
    private final Holder<Potion> result;
    private final Either<Item, TagKey<Item>> either;

    public AddPotionStartMixRecipe(Either<Item, TagKey<Item>> either, Holder<Potion> result) {
        this.item = either.left().orElse(null);
        this.itemTagKey = either.right().orElse(null);
        this.result = result;
        this.either = either;
    }


    public AddPotionStartMixRecipe(TagKey<Item> item, Holder<Potion> result) {
        this.item = null;
        this.itemTagKey = item;
        this.result = result;
        this.either = null;
    }

    public AddPotionStartMixRecipe(Item item, Holder<Potion> result) {
        this.item = item;
        this.itemTagKey = null;
        this.result = result;
        this.either = null;
    }

    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {
        PotionBrewing.Mix<Potion> mix;
        PotionBrewing.Mix<Potion> mix2;
        if (item == null) {

            mix =  new PotionBrewing.Mix<Potion>(Potions.WATER, Ingredient.of(itemTagKey), Potions.MUNDANE);
            mix2 = new PotionBrewing.Mix<Potion>(Potions.AWKWARD, Ingredient.of(itemTagKey), result);

        } else {
            mix = new PotionBrewing.Mix<Potion>(Potions.WATER, Ingredient.of(item), Potions.MUNDANE);
            mix2 = new PotionBrewing.Mix<Potion>(Potions.AWKWARD, Ingredient.of(item), result);
        }

        mixes.add(mix);
        mixes.add(mix2);
    }

    @Override
    public void removeContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {

    }

    @Override
    public void addContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {
    }

    @Override
    public List<Ingredient> removeContainers(List<Ingredient> containers) {
        return List.of();
    }

    @Override
    public List<Ingredient> addContainers(List<Ingredient> containers) {
        return List.of();
    }


    @Override
    public PotionBrewingRecipeType<? extends PotionBrewingRecipe> type() {
        return BMPotionRecipeRegistry.ADD_POTION_START_MIX;
    }


    public Holder<Potion> getResult() {
        return result;
    }

    public Item getItem() {
        return item;
    }

    public Either<Item, TagKey<Item>> getEither() {
        return either;
    }

    public TagKey<Item> getItemTagKey() {
        return itemTagKey;
    }
}
