package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class RemovePotionMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final Codec<RemovePotionMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    OldRecipe.CODEC.fieldOf("recipe_to_remove").forGetter(RemovePotionMixRecipe::getOldRecipe)
            ).apply(instance, RemovePotionMixRecipe::new)
    );

    public static final PotionBrewingRecipeType<RemovePotionMixRecipe> TYPE = new PotionBrewingRecipeType<RemovePotionMixRecipe>() {
        @Override
        public Codec<RemovePotionMixRecipe> codec() {
            return CODEC;
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
            if (potionMix.from.is(oldRecipe.getInputRL()) &&
                    potionMix.ingredient.test(oldRecipe.ingredient().getDefaultInstance()) &&
                    potionMix.to.is(oldRecipe.getResultRl())) {

                iterator.remove();  // Safely removes the element from the list
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("remove_potion_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.getInputRL(), oldRecipe.ingredient(), oldRecipe.getResultRl());
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
