package com.scouter.brewmaster.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class RemoveContainerRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final Codec<RemoveContainerRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").fieldOf("container").forGetter(RemoveContainerRecipe::getItem)
                    ).apply(instance, RemoveContainerRecipe::new)
    );



    public static final PotionBrewingRecipeType<RemoveContainerRecipe> TYPE = new PotionBrewingRecipeType<RemoveContainerRecipe>() {
        @Override
        public Codec<RemoveContainerRecipe> codec() {
            return CODEC;
        }

    };

    private final Item item;

    public RemoveContainerRecipe(Ingredient ingredient) {
        this.item = ingredient.getItems()[0].getItem();
    }

    public RemoveContainerRecipe(Item item) {
        this.item = item;
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
        // No implementation required for this example.
    }

    @Override
    public List<Ingredient> removeContainers(List<Ingredient> containers) {
        boolean foundRecipe = false;
        Iterator<Ingredient> iterator = containers.iterator();

        while (iterator.hasNext()) {
            Ingredient ingredients = iterator.next();
            if (ingredients.test(item.getDefaultInstance())) {
                iterator.remove();  // Safely removes the element from the list
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning("remove_container did not find old container with {}", item);
        }
        return List.of();
    }

    @Override
    public List<Ingredient> addContainers(List<Ingredient> containers) {
        return List.of();
    }


    @Override
    public PotionBrewingRecipeType<? extends PotionBrewingRecipe> type() {
        return BMPotionRecipeRegistry.REMOVE_CONTAINER.get();
    }

    public Item getItem() {
        return item;
    }
}
