package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.mixin.access.PotionBrewingMixAccessor;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class ReplacePotionMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);
    public static final Codec<ReplacePotionMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    OldRecipe.CODEC.fieldOf("old_recipe").forGetter(ReplacePotionMixRecipe::getOldRecipe),
                    AddPotionMixRecipe.CODEC.fieldOf("new_recipe").forGetter(ReplacePotionMixRecipe::getAddPotionMixRecipe)
            ).apply(instance, ReplacePotionMixRecipe::new)
    );



    public static final PotionBrewingRecipeType<ReplacePotionMixRecipe> TYPE = new PotionBrewingRecipeType<ReplacePotionMixRecipe>() {
        @Override
        public Codec<ReplacePotionMixRecipe> codec() {
            return CODEC;
        }

    };



    private final OldRecipe oldRecipe;
    private final AddPotionMixRecipe addPotionMixRecipe;

    public ReplacePotionMixRecipe(OldRecipe oldRecipe, AddPotionMixRecipe addPotionMixRecipe) {
        this.oldRecipe = oldRecipe;
        this.addPotionMixRecipe = addPotionMixRecipe;
    }

    @Override
    public void removePotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {

    }

    @Override
    public void addPotionMixes(List<PotionBrewing.Mix<Potion>> mixes) {
        boolean foundRecipe = false;
        Iterator<PotionBrewing.Mix<Potion>> iterator = mixes.iterator();

        while (iterator.hasNext()) {
            PotionBrewing.Mix<Potion> potionMix = iterator.next();
            if (((PotionBrewingMixAccessor<Potion>)potionMix).brewmaster$getFrom() == oldRecipe.input() &&
                    ((PotionBrewingMixAccessor<Potion>)potionMix).brewmaster$getIngredient().test(oldRecipe.ingredient().getDefaultInstance()) &&
                    ((PotionBrewingMixAccessor<Potion>)potionMix).brewmaster$getTo() == oldRecipe.result()) {


                iterator.remove();
                addPotionMixRecipe.addPotionMixes(mixes);
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("replace_potion_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.getInputRL(), oldRecipe.ingredient(), oldRecipe.getResultRl());
        }
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
        return BMPotionRecipeRegistry.REPLACE_POTION_MIX;
    }

    public AddPotionMixRecipe getAddPotionMixRecipe() {
        return addPotionMixRecipe;
    }

    public OldRecipe getOldRecipe() {
        return oldRecipe;
    }


}
