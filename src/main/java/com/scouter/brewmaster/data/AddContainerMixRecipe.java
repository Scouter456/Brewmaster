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

public class AddContainerMixRecipe implements PotionBrewingRecipe{

    public static final Codec<AddContainerMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("input_item").forGetter(AddContainerMixRecipe::getInput),
                    Codec.either(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").codec(), TagKey.codec(Registries.ITEM).fieldOf("tag").codec()).fieldOf("ingredient").forGetter(predicate ->
                            predicate.item != null ? Either.left(predicate.item) : Either.right(predicate.itemTagKey)
                    ),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("output_item").forGetter(AddContainerMixRecipe::getResult)
            ).apply(instance, AddContainerMixRecipe::new)
    );



    public static final PotionBrewingRecipeType<AddContainerMixRecipe> TYPE = new PotionBrewingRecipeType<AddContainerMixRecipe>() {
        @Override
        public Codec<AddContainerMixRecipe> codec() {
            return CODEC;
        }
    };

    private final Item item;
    private final TagKey<Item> itemTagKey;
    private final Item input;
    private final Item result;
    private final Either<Item, TagKey<Item>> either;


    public AddContainerMixRecipe(Item input, Item item, Item result) {
        this.item = item;
        this.itemTagKey = null;
        this.input = input;
        this.result = result;
        this.either = null;
    }

    public AddContainerMixRecipe(Item input, Either<Item, TagKey<Item>> either, Item result) {
        this.item = either.left().orElse(null);
        this.itemTagKey = either.right().orElse(null);
        this.input = input;
        this.result = result;
        this.either = either;
    }


    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void removeContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {

    }

    @Override
    public void addContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {
        PotionBrewing.Mix<Item> mix;
        if (item == null) {
            mix = new PotionBrewing.Mix<Item>(input, Ingredient.of(itemTagKey), result);
        } else {
            mix = new PotionBrewing.Mix<Item>(input, Ingredient.of(item), result);
        }

        mixes.add(mix);
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
        return BMPotionRecipeRegistry.ADD_CONTAINER_MIX;
    }

    public Either<Item, TagKey<Item>> getEither() {
        return either;
    }

    public TagKey<Item> getItemTagKey() {
        return itemTagKey;
    }

    public Item getInput() {
        return input;
    }

    public Item getResult() {
        return result;
    }
}
