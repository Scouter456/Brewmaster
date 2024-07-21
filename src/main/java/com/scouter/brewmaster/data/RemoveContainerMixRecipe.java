package com.scouter.brewmaster.data;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

public class RemoveContainerMixRecipe implements PotionBrewingRecipe {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final MapCodec<RemoveContainerMixRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    OldContainerRecipe.CODEC.fieldOf("recipe_to_remove").forGetter(RemoveContainerMixRecipe::getOldRecipe)
            ).apply(instance, RemoveContainerMixRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveContainerMixRecipe> STREAM_CODEC = StreamCodec.composite(
            OldContainerRecipe.STREAM_CODEC, RemoveContainerMixRecipe::getOldRecipe,
            RemoveContainerMixRecipe::new
    );

    public static final PotionBrewingRecipeType<RemoveContainerMixRecipe> TYPE = new PotionBrewingRecipeType<RemoveContainerMixRecipe>() {
        @Override
        public MapCodec<RemoveContainerMixRecipe> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemoveContainerMixRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    };

    private final OldContainerRecipe oldRecipe;

    public RemoveContainerMixRecipe(OldContainerRecipe oldRecipe) {
        this.oldRecipe = oldRecipe;
    }

    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void removeContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {
        boolean foundRecipe = false;
        Iterator<PotionBrewing.Mix<Item>> iterator = mixes.iterator();

        while (iterator.hasNext()) {
            PotionBrewing.Mix<Item> potionMix = iterator.next();
            if (potionMix.from().is(oldRecipe.input()) &&
                    potionMix.ingredient().test(oldRecipe.ingredient().getDefaultInstance()) &&
                    potionMix.to().is(oldRecipe.result())) {

                iterator.remove();  // Safely removes the element from the list
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.warn("Brewmaster: remove_potion_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.input(), oldRecipe.ingredient(), oldRecipe.result());
        }
    }

    @Override
    public void addContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {
        // No implementation required for this example.
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
        return BMPotionRecipeRegistry.REMOVE_CONTAINER_MIX.get();
    }

    public OldContainerRecipe getOldRecipe() {
        return oldRecipe;
    }
}
