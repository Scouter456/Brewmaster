package com.scouter.brewmaster.events;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.command.PotionBrewingRecipesToShow;
import com.scouter.brewmaster.data.BrewmasterJsonManager;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.message.PotionBrewingS2C;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.util.CustomLogger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FabricEvents {

    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);
    private static boolean hasReloaded = false;
    public static void onServerStart() {
        ServerLifecycleEvents.SERVER_STARTED.register(FabricEvents::setRecipes);
    }

    public static void endDataReload() {
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            setRecipes(Objects.requireNonNull(server));
        });
    }


    public static void onDataSynch() {
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(((player, joined) -> {
            synchWithMessage(player);
        }));
    }

    private static void synchWithMessage(ServerPlayer player) {
        try {
            synchRecipes(player);
        } catch (Exception e) {
            LOGGER.logError("Was not able to synch recipe for {} due to {}", player, e.toString());
        }
    }

    private static void setRecipes(MinecraftServer server) {
        PotionBrewing potionBrewing = server.potionBrewing();

        List<PotionBrewingRecipe> potionBrewingRecipes = BrewmasterJsonManager.getPotionBrewingEntries();
        LOGGER.logInfo("Adding brewing recipes");
        int total = 0;
        List<PotionBrewing.Mix<Potion>> currentMixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getMixes());
        List<Ingredient> container = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getContainers());


        for (PotionBrewingRecipe recipe : potionBrewingRecipes) {
            total++;
            recipe.removePotionMixes(currentMixes);
            recipe.removeContainerMixes(mixes);
            recipe.removeContainers(container);
        }

        for (PotionBrewingRecipe recipe : potionBrewingRecipes) {
            recipe.addPotionMixes(currentMixes);
            recipe.addContainerMixes(mixes);
            recipe.addContainers(container);
        }
        PotionBrewingRecipesToShow.setPotions(currentMixes);
        LOGGER.logInfo("Added a total of %d recipes ", total);
        ((PotionBrewingRecipeExtension) potionBrewing).setMixes(List.copyOf(mixes));
        ((PotionBrewingRecipeExtension) potionBrewing).setPotionMixes(List.copyOf(currentMixes));
        ((PotionBrewingRecipeExtension) potionBrewing).setContainer(List.copyOf(container));
        setHasReloaded(false);
    }

    private static void synchRecipes(ServerPlayer player) {
        PotionBrewing potionBrewing = player.getServer().potionBrewing();
        List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getMixes());
        List<Ingredient> containers = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getContainers());
        ServerPlayNetworking.send(player, new PotionBrewingS2C(potionMixes, mixes, containers));
    }

    public static void setHasReloaded(boolean hasReloaded) {
        FabricEvents.hasReloaded = hasReloaded;
    }
}
