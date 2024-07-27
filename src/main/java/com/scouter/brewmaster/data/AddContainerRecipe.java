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

public class AddContainerRecipe implements PotionBrewingRecipe{

    public static final Codec<AddContainerRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.either(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").codec(),TagKey.codec(Registries.ITEM).fieldOf("tag").codec()).fieldOf("container").forGetter(predicate ->
                            predicate.item != null ? Either.left(predicate.item) : Either.right(predicate.itemTagKey)
                    )
            ).apply(instance, AddContainerRecipe::new)
    );



    public static final PotionBrewingRecipeType<AddContainerRecipe> TYPE = new PotionBrewingRecipeType<AddContainerRecipe>() {
        @Override
        public Codec<AddContainerRecipe> codec() {
            return CODEC;
        }

    };

    private final Item item;
    private final TagKey<Item> itemTagKey;
    private final Either<Item, TagKey<Item>> either;


    public AddContainerRecipe(Ingredient ingredient) {
        this.item = ingredient.getItems()[0].getItem();
        this.itemTagKey = null;
        this.either = null;
    }

    public AddContainerRecipe(TagKey<Item> tagKey) {
        this.item = null;
        this.itemTagKey =tagKey;
        this.either = null;
    }

    public AddContainerRecipe(Item item) {
        this.item = item;
        this.itemTagKey =null;
        this.either = null;
    }

    public AddContainerRecipe(Either<Item, TagKey<Item>> either) {
        this.item = either.left().orElse(null);
        this.itemTagKey = either.right().orElse(null);
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

    }

    @Override
    public List<Ingredient> removeContainers(List<Ingredient> containers) {
        return List.of();
    }

    @Override
    public List<Ingredient> addContainers(List<Ingredient> containers) {
        if(item == null) {
            containers.add(Ingredient.of(itemTagKey));
        } else {
            containers.add(Ingredient.of(item));

        }

        return List.of();
    }

    @Override
    public PotionBrewingRecipeType<? extends PotionBrewingRecipe> type() {
        return BMPotionRecipeRegistry.ADD_CONTAINER;
    }

    public Either<Item, TagKey<Item>> getEither() {
        return either;
    }
}
