package com.scouter.brewmaster.events;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.command.PotionBrewingRecipesToShow;
import com.scouter.brewmaster.data.BrewmasterJsonManager;
import com.scouter.brewmaster.data.OldContainerRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.message.BMMessages;
import com.scouter.brewmaster.message.PotionBrewingS2C;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.registry.BMRegistries;
import com.scouter.brewmaster.util.CustomLogger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Brewmaster.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);
    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new BrewmasterJsonManager());
    }
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        setRecipes(event.getServer());
    }

    @SubscribeEvent
    public static void synchDataEvent(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            event.getPlayerList().getPlayers().stream().findAny().ifPresentOrElse(
                    player -> setRecipes(Objects.requireNonNull(player.getServer())),
                    () -> LOGGER.logError("Not able to set potion recipes!!!")
            );
            for (ServerPlayer serverPlayer : event.getPlayerList().getPlayers()) {
                synchWithMessage(serverPlayer);
            }
        } else if (event.getPlayer() != null) {
            synchWithMessage(event.getPlayer());
        }


    }

    private static void synchWithMessage(ServerPlayer player) {
        try {
            synchRecipes(player);
        } catch (Exception e) {
            LOGGER.logError("Was not able to synch recipe for {} due to {}", player, e);
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
        PotionBrewingS2C message = new PotionBrewingS2C(OldRecipe.fromList(potionMixes), OldContainerRecipe.fromList(mixes), containerItems);
        BMMessages.sendToPlayer(message, player);
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
