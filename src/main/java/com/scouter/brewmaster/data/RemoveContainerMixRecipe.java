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

public class RemoveContainerMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final Codec<RemoveContainerMixRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    OldContainerRecipe.CODEC.fieldOf("recipe_to_remove").forGetter(RemoveContainerMixRecipe::getOldRecipe)
            ).apply(instance, RemoveContainerMixRecipe::new)
    );



    public static final PotionBrewingRecipeType<RemoveContainerMixRecipe> TYPE = new PotionBrewingRecipeType<RemoveContainerMixRecipe>() {
        @Override
        public Codec<RemoveContainerMixRecipe> codec() {
            return CODEC;
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
            boolean b1 =((PotionBrewingMixAccessor<Item>)potionMix).brewmaster$getFrom().getDefaultInstance().is(oldRecipe.input());
            boolean b2 =((PotionBrewingMixAccessor<Item>)potionMix).brewmaster$getIngredient().test(oldRecipe.ingredient().getDefaultInstance()) ;
            boolean b3 =((PotionBrewingMixAccessor<Item>)potionMix).brewmaster$getTo().getDefaultInstance().is(oldRecipe.result());

            if (b1 && b2 && b3) {

                iterator.remove();
                foundRecipe = true;
                break;
            }

        }
        if (!foundRecipe) {
            LOGGER.logWarning("remove_container_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.getInputRL(), oldRecipe.ingredient(), oldRecipe.getResultRl());
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
        return BMPotionRecipeRegistry.REMOVE_CONTAINER_MIX;
    }

    public OldContainerRecipe getOldRecipe() {
        return oldRecipe;
    }
}
