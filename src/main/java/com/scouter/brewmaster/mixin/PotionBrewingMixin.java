package com.scouter.brewmaster.mixin;

import com.scouter.brewmaster.events.PotionBrewingRecipeExtension;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin implements PotionBrewingRecipeExtension {

    @Mutable
    @Shadow @Final private List<PotionBrewing.Mix<Potion>> potionMixes;

    @Mutable
    @Shadow @Final private List<PotionBrewing.Mix<Item>> containerMixes;

    @Mutable
    @Shadow @Final private List<Ingredient> containers;

    @Override
    public void setPotionMixes(List<PotionBrewing.Mix<Potion>> potionMixes) {
        this.potionMixes = potionMixes;
    }

    @Override
    public void setMixes(List<PotionBrewing.Mix<Item>> mixes) {
        this.containerMixes = mixes;
    }

    @Override
    public void setContainer(List<Ingredient> container) {
        this.containers = container;
    }
}
