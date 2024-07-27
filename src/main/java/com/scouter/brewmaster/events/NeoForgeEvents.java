package com.scouter.brewmaster.events;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.command.PotionBrewingRecipesToShow;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@EventBusSubscriber(modid = Brewmaster.MODID, bus = EventBusSubscriber.Bus.GAME)
public class NeoForgeEvents {
    private static final CustomLogger LOGGER = new CustomLogger(Brewmaster.MODID);

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        setRecipes(event.getServer());
    }

    @SubscribeEvent
    public static void synchDataEvent(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            event.getRelevantPlayers().findAny().ifPresentOrElse(
                    player -> setRecipes(Objects.requireNonNull(player.getServer())),
                    () -> LOGGER.logError("Not able to set potion recipes!!!")
            );
            for (ServerPlayer serverPlayer : event.getRelevantPlayers().toList()) {
                synchWithMessage(serverPlayer);
            }
        } else if(event.getPlayer() != null){
            synchWithMessage(event.getPlayer());
        }


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
        server.registryAccess().registry(BMRegistries.Keys.POTION_RECIPE).ifPresent(
                potionBrewingRecipes -> {
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
                    LOGGER.logInfo("Added a total of {} recipes ", total);
                    ((PotionBrewingRecipeExtension) potionBrewing).setMixes(List.copyOf(mixes));
                    ((PotionBrewingRecipeExtension) potionBrewing).setPotionMixes(List.copyOf(currentMixes));
                    ((PotionBrewingRecipeExtension) potionBrewing).setContainer(List.copyOf(container));
                }
        );
    }

    private static void synchRecipes(ServerPlayer player) {
        PotionBrewing potionBrewing = player.getServer().potionBrewing();
        List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getMixes());
        List<Ingredient> containers = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getContainers());
        PacketDistributor.sendToPlayer(player, new PotionBrewingS2C(potionMixes, mixes, containers));
    }

    private static void setAndSynchRecipes(MinecraftServer server, Stream<ServerPlayer> serverPlayerStream) {
        setRecipes(server);
        for (ServerPlayer serverPlayer : serverPlayerStream.toList()) {
            try {
                synchRecipes(serverPlayer);
            } catch (Exception e) {
                LOGGER.logError("Was not able to synch recipe for {} due to {}", serverPlayer, e.toString());
            }
        }
    }
}
