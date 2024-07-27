package com.scouter.brewmaster.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class AddPotionMixRecipe implements PotionBrewingRecipe {

    public static final Codec<AddPotionMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.POTION.byNameCodec().fieldOf("input_potion").forGetter(AddPotionMixRecipe::getInput),
                    Codec.either(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").codec(), TagKey.codec(Registries.ITEM).fieldOf("tag").codec()).fieldOf("ingredient").forGetter(predicate ->
                            predicate.item != null ? Either.left(predicate.item) : Either.right(predicate.itemTagKey)
                    ),
                    BuiltInRegistries.POTION.byNameCodec().fieldOf("output_potion").forGetter(AddPotionMixRecipe::getResult)
            ).apply(instance, AddPotionMixRecipe::new)
    );


    public static final PotionBrewingRecipeType<AddPotionMixRecipe> TYPE = new PotionBrewingRecipeType<AddPotionMixRecipe>() {
        @Override
        public Codec<AddPotionMixRecipe> codec() {
            return CODEC;
        }

    };


    private final Item item;
    private final TagKey<Item> itemTagKey;
    private final Potion input;
    private final Potion result;
    private final Either<Item, TagKey<Item>> either;

    public AddPotionMixRecipe(Potion input, Either<Item, TagKey<Item>> either, Potion result) {
        this.item = either.left().orElse(null);
        this.itemTagKey = either.right().orElse(null);
        this.input = input;
        this.result = result;
        this.either = either;
    }


    public AddPotionMixRecipe(Potion input, TagKey<Item> item, Potion result) {
        this.item = null;
        this.itemTagKey = item;
        this.input = input;
        this.result = result;
        this.either = null;
    }

    public AddPotionMixRecipe(Potion input, Item item, Potion result) {
        this.item = item;
        this.itemTagKey = null;
        this.input = input;
        this.result = result;
        this.either = null;
    }

    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {
        PotionBrewing.Mix<Potion> mix;
        if (item == null) {
            mix = new PotionBrewing.Mix<Potion>(input, Ingredient.of(itemTagKey), result);
        } else {
            mix = new PotionBrewing.Mix<Potion>(input, Ingredient.of(item), result);
        }

        mixes.add(mix);
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
        return BMPotionRecipeRegistry.ADD_POTION_MIX;
    }

    public Potion getInput() {
        return input;
    }

    public Potion getResult() {
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
