package com.scouter.brewmaster.events;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.command.PotionBrewingRecipesToShow;
import com.scouter.brewmaster.data.BrewmasterJsonManager;
import com.scouter.brewmaster.data.OldContainerRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.message.PotionBrewingS2C;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.util.CustomLogger;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
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
        List<PotionBrewingRecipe> potionBrewingRecipes = BrewmasterJsonManager.getPotionBrewingEntries();

        LOGGER.logInfo("Adding brewing recipes");
        int total = 0;
        List<PotionBrewing.Mix<Potion>> currentMixes = new ArrayList<>(PotionBrewingAccessor.brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(PotionBrewingAccessor.brewmaster$getMixes());
        List<Ingredient> container = new ArrayList<>(PotionBrewingAccessor.brewmaster$getContainers());


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
        LOGGER.logInfo("Added a total of {} recipes ", total);

        setAllMixes(List.copyOf(currentMixes), List.copyOf(mixes), List.copyOf(container));
    }

    private static void synchRecipes(ServerPlayer player) {
        List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>(PotionBrewingAccessor.brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(PotionBrewingAccessor.brewmaster$getMixes());
        List<Ingredient> containers = new ArrayList<>(PotionBrewingAccessor.brewmaster$getContainers());
        List<Item> containerItems = containers.stream().map(e -> e.getItems()[0].getItem()).toList();
        FriendlyByteBuf message =  PotionBrewingS2C.write(new PotionBrewingS2C(OldRecipe.fromList(potionMixes), OldContainerRecipe.fromList(mixes), containerItems));
        ServerPlayNetworking.send(player,PotionBrewingS2C.ID, message);
    }

    public static void setHasReloaded(boolean hasReloaded) {
        FabricEvents.hasReloaded = hasReloaded;
    }


    public static void setAllMixes(List<PotionBrewing.Mix<Potion>> potionMixes, List<PotionBrewing.Mix<Item>> mixes, List<Ingredient> container) {
        brewmaster$setPotionMixes(potionMixes);
        brewmaster$setMixes(mixes);
        brewmaster$setContainer(container);
    }

    static void brewmaster$setPotionMixes(List<PotionBrewing.Mix<Potion>> potionMixes) {
        PotionBrewingAccessor.brewmaster$getPotionMixes().clear();
        PotionBrewingAccessor.brewmaster$getPotionMixes().addAll(potionMixes);

    }

    static void brewmaster$setMixes(List<PotionBrewing.Mix<Item>> mixes) {
        PotionBrewingAccessor.brewmaster$getMixes().clear();
        PotionBrewingAccessor.brewmaster$getMixes().addAll(mixes);
    }

    static void brewmaster$setContainer(List<Ingredient> container) {
        PotionBrewingAccessor.brewmaster$getContainers().clear();
        PotionBrewingAccessor.brewmaster$getContainers().addAll(container);
    }
}
