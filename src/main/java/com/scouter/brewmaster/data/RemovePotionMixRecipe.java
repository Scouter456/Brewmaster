package com.scouter.brewmaster.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class RemovePotionMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final MapCodec<RemovePotionMixRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    OldRecipe.CODEC.fieldOf("recipe_to_remove").forGetter(RemovePotionMixRecipe::getOldRecipe)
            ).apply(instance, RemovePotionMixRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,RemovePotionMixRecipe> STREAM_CODEC = StreamCodec.composite(
            OldRecipe.STREAM_CODEC, RemovePotionMixRecipe::getOldRecipe,
            RemovePotionMixRecipe::new
    );

    public static final PotionBrewingRecipeType<RemovePotionMixRecipe> TYPE = new PotionBrewingRecipeType<RemovePotionMixRecipe>() {
        @Override
        public MapCodec<RemovePotionMixRecipe> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, RemovePotionMixRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    };

    private final OldRecipe oldRecipe;

    public RemovePotionMixRecipe(OldRecipe oldRecipe) {
        this.oldRecipe = oldRecipe;
    }

    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {
        boolean foundRecipe = false;
        Iterator<PotionBrewing.Mix<Potion>> iterator = mixes.iterator();

        while (iterator.hasNext()) {
            PotionBrewing.Mix<Potion> potionMix = iterator.next();
            if (potionMix.from().is(oldRecipe.input()) &&
                    potionMix.ingredient().test(oldRecipe.ingredient().getDefaultInstance()) &&
                    potionMix.to().is(oldRecipe.result())) {

                iterator.remove();  // Safely removes the element from the list
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("remove_potion_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.input().getRegisteredName(), oldRecipe.ingredient(), oldRecipe.result().getRegisteredName());
        }
    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void removeContainerMixes(List<PotionBrewing.Mix<Item>> mixes) {

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
        return BMPotionRecipeRegistry.REMOVE_POTION_MIX.get();
    }

    public OldRecipe getOldRecipe() {
        return oldRecipe;
    }
}
