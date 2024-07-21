package com.scouter.brewmaster.events;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.data.OldContainerRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.message.PotionBrewingS2C;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.registry.BMRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Brewmaster.MODID, bus = EventBusSubscriber.Bus.GAME)
public class NeoForgeEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        PotionBrewing potionBrewing = event.getServer().potionBrewing();
        event.getServer().registryAccess().registry(BMRegistries.Keys.POTION_RECIPE).ifPresent(
                potionBrewingRecipes -> {
                    List<PotionBrewing.Mix<Potion>> currentMixes = new ArrayList<>(((PotionBrewingAccessor)potionBrewing).brewmaster$getPotionMixes());
                    List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(((PotionBrewingAccessor)potionBrewing).brewmaster$getMixes());
                    List<Ingredient> container = new ArrayList<>(((PotionBrewingAccessor)potionBrewing).brewmaster$getContainers());

                    potionBrewingRecipes.forEach(recipe -> {
                        recipe.removePotionMixes(currentMixes);
                        recipe.removeContainerMixes(mixes);
                        recipe.removeContainers(container);
                    });

                    potionBrewingRecipes.forEach(recipe -> {
                        recipe.addPotionMixes(currentMixes);
                        recipe.addContainerMixes(mixes);
                        recipe.addContainers(container);
                    });

                    ((PotionBrewingRecipeExtension)potionBrewing).setMixes(List.copyOf(mixes));
                    ((PotionBrewingRecipeExtension)potionBrewing).setPotionMixes(List.copyOf(currentMixes));
                    ((PotionBrewingRecipeExtension)potionBrewing).setContainer(List.copyOf(container));
                }
        );
    }


    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        PotionBrewing potionBrewing = player.getServer().potionBrewing();
        List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getPotionMixes());
        List<PotionBrewing.Mix<Item>> mixes = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getMixes());
        List<Ingredient> containers = new ArrayList<>(((PotionBrewingAccessor) potionBrewing).brewmaster$getContainers());
        PacketDistributor.sendToPlayer((ServerPlayer) player, new PotionBrewingS2C(OldRecipe.fromList(potionMixes), OldContainerRecipe.fromList(mixes), containers));
    }


    @SubscribeEvent
    public static void onServerStop(ServerStoppingEvent event) {

    }
}
