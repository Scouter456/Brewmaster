package com.scouter.brewmaster.mixin.access;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {


    @Accessor("potionMixes")
    List<PotionBrewing.Mix<Potion>> brewmaster$getPotionMixes();

    @Accessor("containerMixes")
    List<PotionBrewing.Mix<Item>> brewmaster$getMixes();

    @Accessor("containers")
    List<Ingredient> brewmaster$getContainers();
}
