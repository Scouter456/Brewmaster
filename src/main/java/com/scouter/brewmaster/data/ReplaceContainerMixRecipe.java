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

public class ReplaceContainerMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);
    public static final Codec<ReplaceContainerMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    OldContainerRecipe.CODEC.fieldOf("old_recipe").forGetter(ReplaceContainerMixRecipe::getOldRecipe),
                    AddContainerMixRecipe.CODEC.fieldOf("new_recipe").forGetter(ReplaceContainerMixRecipe::getAddContainerMixRecipe)
            ).apply(instance, ReplaceContainerMixRecipe::new)
    );


    public static final PotionBrewingRecipeType<ReplaceContainerMixRecipe> TYPE = new PotionBrewingRecipeType<ReplaceContainerMixRecipe>() {
        @Override
        public Codec<ReplaceContainerMixRecipe> codec() {
            return CODEC;
        }

    };




    private final OldContainerRecipe oldRecipe;
    private final AddContainerMixRecipe addContainerMixRecipe;

    public ReplaceContainerMixRecipe(OldContainerRecipe oldRecipe, AddContainerMixRecipe addPotionMixRecipe) {
        this.oldRecipe = oldRecipe;
        this.addContainerMixRecipe = addPotionMixRecipe;
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
        boolean foundRecipe = false;
        Iterator<PotionBrewing.Mix<Item>> iterator = mixes.iterator();

        while (iterator.hasNext()) {
            PotionBrewing.Mix<Item> potionMix = iterator.next();
            if (potionMix.from.getDefaultInstance().is(oldRecipe.input()) &&
                    potionMix.ingredient.test(oldRecipe.ingredient().getDefaultInstance()) &&
                    potionMix.to.getDefaultInstance().is(oldRecipe.result())) {

                iterator.remove();
                addContainerMixRecipe.addContainerMixes(mixes);
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("replace_container_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.getInputRL(), oldRecipe.ingredient(), oldRecipe.getResultRl());
        }
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
        return BMPotionRecipeRegistry.REPLACE_CONTAINER_MIX;
    }

    public AddContainerMixRecipe getAddContainerMixRecipe() {
        return addContainerMixRecipe;
    }

    public OldContainerRecipe getOldRecipe() {
        return oldRecipe;
    }


}
