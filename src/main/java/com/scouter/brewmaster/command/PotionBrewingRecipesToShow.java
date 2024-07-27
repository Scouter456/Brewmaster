package com.scouter.brewmaster.command;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class PotionBrewingRecipesToShow {


    public static final List<Potion> holders = new ArrayList<>();
    public static Map<Potion,List<PotionBrewing.Mix<Potion>>> potions = new HashMap<>();

    public static void setPotions(List<PotionBrewing.Mix<Potion>> potionsList) {
        potions.clear();
        holders.clear();
        for(PotionBrewing.Mix<Potion> mix : potionsList) {
            holders.add(mix.to.get());
            List<PotionBrewing.Mix<Potion>> mixes = potions.computeIfAbsent(mix.to.get(), potionHolder -> new ArrayList<>());
            mixes.add(mix);
            potions.put(mix.to.get(), mixes);
        }
    }

    public static Stream<String> getRegisteredPotions() {
        return holders.stream().map(PotionBrewingRecipesToShow::getName);
    }

    private static List<PotionBrewing.Mix<Potion>> getMixesList(Potion potionHolder) {
        return potions.getOrDefault(potionHolder, Collections.emptyList());
    }



    public static void printMessageForPotion(ServerPlayer player, Potion potionHolder) {
        player.sendSystemMessage(Component.literal("Mix(es) for " + getName(potionHolder)));
        AtomicInteger integer = new AtomicInteger(1);
        for(PotionBrewing.Mix<Potion> mix : getMixesList(potionHolder)) {
            player.sendSystemMessage(formatPotionMessage(mix, integer));
        }
    }


    private static Component formatPotionMessage(PotionBrewing.Mix<Potion> potionMix, AtomicInteger integer) {
        // Extract potion names

        String potionInput = getName(potionMix.from.get());
        String potionOutput = getName(potionMix.to.get());

        // Extract ingredients
        ItemStack[] ingredients = potionMix.ingredient.getItems();

        // Create colored components for each part
        MutableComponent formattedMessage = Component.literal("");

        for (ItemStack item : ingredients) {
            Component recipeNr =  Component.literal("Recipe number: " + integer)
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_BLUE));
            Component potionInputComponent =  Component.literal("Potion Input: " + potionInput)
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED));
            Component ingredientInputComponent =  Component.literal("Ingredient Input: " + BuiltInRegistries.ITEM.getKey(item.getItem()))
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN));
            Component potionOutputComponent =  Component.literal("Potion Output: " + potionOutput)
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE));

            formattedMessage
                    .append(recipeNr)
                    .append(Component.literal("\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)))
                    .append(potionInputComponent)
                    .append(Component.literal("\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)))
                    .append(ingredientInputComponent)
                    .append(Component.literal("\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)))
                    .append(potionOutputComponent)
                    .append(Component.literal("\n").setStyle(Style.EMPTY.withColor(ChatFormatting.RESET)));
            integer.addAndGet(1);
        }

        return formattedMessage;
    }

    public static String getName(Potion potionHolder) {
        return BuiltInRegistries.POTION.getKey(potionHolder).toString();
    }
}
