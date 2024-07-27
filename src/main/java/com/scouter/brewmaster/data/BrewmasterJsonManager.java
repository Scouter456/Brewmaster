package com.scouter.brewmaster.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.scouter.brewmaster.events.FabricEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class BrewmasterJsonManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson STANDARD_GSON = new Gson();
    public static final Logger LOGGER = LogManager.getLogger();
    private final String folderName;
    protected static Map<ResourceLocation, PotionBrewingRecipe> recipeMap = new HashMap<>();

    public BrewmasterJsonManager() {
        this("brewmaster/potion_recipe", STANDARD_GSON);
    }

    public BrewmasterJsonManager(String folderName, Gson gson) {
        super(gson, folderName);
        this.folderName = folderName;
    }
    @Override
    public ResourceLocation getFabricId() {
        return prefix("brewmaster/potion_recipe");
    }

    public static List<PotionBrewingRecipe> getPotionBrewingEntries() {
        return recipeMap.values().stream().toList();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.recipeMap.clear();
        Map<ResourceLocation, PotionBrewingRecipe> recipeHashMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : jsons.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement element = entry.getValue();
            PotionBrewingRecipe.DIRECT_CODEC.decode(JsonOps.INSTANCE, element)
                    .get().ifLeft(result -> {
                        PotionBrewingRecipe codec = result.getFirst();
                        recipeHashMap.put(key, codec);
                    }).ifRight(partial -> LOGGER.error("Failed to parse pattern JSON for {} due to: {}", key, partial.message()));


        }
        FabricEvents.setHasReloaded(true);
        this.recipeMap = recipeHashMap;
        LOGGER.info("Data loader for {} loaded {} jsons", this.folderName, this.recipeMap.size());
    }
}
