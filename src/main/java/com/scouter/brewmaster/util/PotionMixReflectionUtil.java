/*package com.scouter.brewmaster.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

public class PotionMixReflectionUtil {

    public static PotionBrewing.Mix<Potion> createPotionMix(
        Holder<Potion> from,
        Ingredient ingredient,
        Holder<Potion> to
    ) {
        try {
            // Access the class and constructor
            Class<?> mixClass = Class.forName("net.minecraft.world.item.alchemy.PotionBrewing$Mix");
            Constructor<?> constructor = mixClass.getDeclaredConstructor(Holder.class, Ingredient.class, Holder.class);
            constructor.setAccessible(true); // Ensure we can access it

            // Create a new instance
            return (PotionBrewing.Mix<Potion>) constructor.newInstance(from, ingredient, to);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to create PotionBrewing.Mix instance", e);
        }
    }

    public static PotionBrewing.Mix<Item> createItemMix(
            Holder<Item> from,
            Ingredient ingredient,
            Holder<Item> to
    ) {
        try {
            // Access the class and constructor
            Class<?> mixClass = Class.forName("net.minecraft.world.item.alchemy.PotionBrewing$Mix");
            Constructor<?> constructor = mixClass.getDeclaredConstructor(Holder.class, Ingredient.class, Holder.class);
            constructor.setAccessible(true); // Ensure we can access it
            // Create a new instance
            return (PotionBrewing.Mix<Item>) constructor.newInstance(from, ingredient, to);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to create PotionBrewing.Mix instance", e);
        }
    }
}
*/