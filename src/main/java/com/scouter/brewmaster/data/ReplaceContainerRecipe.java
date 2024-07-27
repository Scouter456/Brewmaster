package com.scouter.brewmaster.data;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.registry.BMPotionRecipeRegistry;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Iterator;
import java.util.List;

public class ReplaceContainerRecipe implements PotionBrewingRecipe {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    public static final MapCodec<ReplaceContainerRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").fieldOf("old_container").forGetter(ReplaceContainerRecipe::getOldContainer),
                    AddContainerRecipe.CODEC.fieldOf("new_container").forGetter(ReplaceContainerRecipe::getAddContainerMixRecipe)
            ).apply(instance, ReplaceContainerRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ReplaceContainerRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(BuiltInRegistries.ITEM.byNameCodec()), ReplaceContainerRecipe::getOldContainer,
            AddContainerRecipe.STREAM_CODEC, ReplaceContainerRecipe::getAddContainerMixRecipe,
            ReplaceContainerRecipe::new
    );

    public static final PotionBrewingRecipeType<ReplaceContainerRecipe> TYPE = new PotionBrewingRecipeType<ReplaceContainerRecipe>() {
        @Override
        public MapCodec<ReplaceContainerRecipe> mapCodec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ReplaceContainerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    };


    private final Item item;
    private final AddContainerRecipe addContainerRecipe;

    public ReplaceContainerRecipe(Item item, AddContainerRecipe addContainerRecipe) {
        this.item = item;
        this.addContainerRecipe = addContainerRecipe;
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
        boolean foundRecipe = false;
        Iterator<Ingredient> iterator = containers.iterator();

        while (iterator.hasNext()) {
            Ingredient ingredients = iterator.next();
            if (ingredients.test(item.getDefaultInstance())) {
                iterator.remove();  // Safely removes the element from the list
                addContainerRecipe.addContainers(containers);
                foundRecipe = true;
                break;
            }
        }

        if (!foundRecipe) {
            LOGGER.logWarning(" replace_container did not find old container with {}", item);
        }

        return List.of();
    }


    @Override
    public PotionBrewingRecipeType<? extends PotionBrewingRecipe> type() {
        return BMPotionRecipeRegistry.REPLACE_CONTAINER;
    }

    public AddContainerRecipe getAddContainerMixRecipe() {
        return addContainerRecipe;
    }

    public Item getOldContainer() {
        return item;
    }


}
