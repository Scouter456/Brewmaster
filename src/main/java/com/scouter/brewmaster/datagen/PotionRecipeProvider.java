package com.scouter.brewmaster.datagen;

import com.google.common.collect.Sets;
import com.mojang.serialization.JsonOps;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class PotionRecipeProvider implements DataProvider {
    protected final PackOutput.PathProvider potionPathProvider;

    public PotionRecipeProvider(PackOutput pOutput, String modid) {
        this.potionPathProvider = pOutput.createPathProvider(PackOutput.Target.DATA_PACK, ResourceLocation.fromNamespaceAndPath(modid,"brewmaster/potion_recipe").getPath());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Set<ResourceLocation> taskSet = Sets.newHashSet();
        List<CompletableFuture<?>> list = new ArrayList<>();
        this.createRecipe((recipe -> {
            if (!set.add(recipe.getLocation())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getLocation());
            } else {

                PotionBrewingRecipe.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, recipe.getRecipe())
                        .ifError(partial -> LOGGER.error("Failed to create recipe {}, due to {}", recipe.getLocation(), partial))
                        .ifSuccess(e -> list.add(DataProvider.saveStable(pOutput, e, this.potionPathProvider.json(recipe.getLocation()))));
            }
        }));
        return CompletableFuture.allOf(list.toArray((p_253414_) -> {
            return new CompletableFuture[p_253414_];
        }));
    }


    protected abstract void createRecipe(Consumer<PotionRecipeConsumer> pWriter);

    public void addRecipe(Consumer<PotionRecipeConsumer> consumerConsumer, ResourceLocation name,PotionBrewingRecipe recipe) {
        consumerConsumer.accept(new PotionRecipeConsumer(name, recipe));
    }

    @Override
    public String getName() {
        return "potion_recipes";
    }
}
