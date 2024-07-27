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

public class ReplacePotionMixRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final MapCodec<ReplacePotionMixRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    OldRecipe.CODEC.fieldOf("old_recipe").forGetter(ReplacePotionMixRecipe::getOldRecipe),
                    AddPotionMixRecipe.CODEC.fieldOf("new_recipe").forGetter(ReplacePotionMixRecipe::getAddPotionMixRecipe)
            ).apply(instance, ReplacePotionMixRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,ReplacePotionMixRecipe> STREAM_CODEC = StreamCodec.composite(
            OldRecipe.STREAM_CODEC, ReplacePotionMixRecipe::getOldRecipe,
            AddPotionMixRecipe.STREAM_CODEC, ReplacePotionMixRecipe::getAddPotionMixRecipe,
            ReplacePotionMixRecipe::new
    );

    public static final PotionBrewingRecipeType<ReplacePotionMixRecipe> TYPE = new PotionBrewingRecipeType<ReplacePotionMixRecipe>() {
        @Override
        public MapCodec<ReplacePotionMixRecipe> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ReplacePotionMixRecipe> streamCodec() {
            return STREAM_CODEC;
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
            if (potionMix.from().is(oldRecipe.input()) &&
                    potionMix.ingredient().test(oldRecipe.ingredient().getDefaultInstance()) &&
                    potionMix.to().is(oldRecipe.result())) {

                iterator.remove();
                addPotionMixRecipe.addPotionMixes(mixes);
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("replace_potion_mix did not find old recipe with input {}, ingredient {}, result {}", oldRecipe.input().getRegisteredName(), oldRecipe.ingredient(), oldRecipe.result().getRegisteredName());
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
