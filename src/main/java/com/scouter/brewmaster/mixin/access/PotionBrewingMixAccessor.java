package com.scouter.brewmaster.mixin.access;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.world.item.alchemy.PotionBrewing;

@Mixin(PotionBrewing.Mix.class)
public interface PotionBrewingMixAccessor<T> {

    @Accessor("from")
    T brewmaster$getFrom();

    @Accessor("ingredient")
    Ingredient brewmaster$getIngredient();

    @Accessor("to")
    T brewmaster$getTo();
}
