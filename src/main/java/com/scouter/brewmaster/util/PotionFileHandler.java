package com.scouter.brewmaster.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.scouter.brewmaster.data.*;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PotionFileHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static int createPotionFiles(CommandContext<CommandSourceStack> context) {
        return processPotionFiles(
                context,
                "brewmaster/potion_files",
                PotionFileHandler::createPotionFileName,
                potionMix -> new AddPotionMixRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to())
        );
    }

    public static int removePotionFiles(CommandContext<CommandSourceStack> context) {
        return processPotionFiles(
                context,
                "brewmaster/remove_potion_files",
                PotionFileHandler::removePotionFileName,
                potionMix -> new RemovePotionMixRecipe(new OldRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()))
        );
    }

    public static int replacePotionFiles(CommandContext<CommandSourceStack> context) {
        return processPotionFiles(
                context,
                "brewmaster/replace_potion_files",
                PotionFileHandler::replacePotionFileName,
                potionMix -> new ReplacePotionMixRecipe(new OldRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()),new AddPotionMixRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()))
        );
    }

    public static int createContainerFiles(CommandContext<CommandSourceStack> context) {
        return processContainerFiles(
                context,
                "brewmaster/container_files",
                (mix, key) -> createContainerFileName(mix),
                AddContainerRecipe::new
        );
    }

    public static int removeContainerFiles(CommandContext<CommandSourceStack> context) {
        return processContainerFiles(
                context,
                "brewmaster/remove_container_files",
                (mix, key) ->  removeContainerFileName(mix),
                 RemoveContainerRecipe::new
        );
    }

    public static int replaceContainerFiles(CommandContext<CommandSourceStack> context) {
        return processContainerFiles(
                context,
                "brewmaster/replace_container_files",
                (mix, key) ->  replaceContainerFileName(mix),
                mix -> new ReplaceContainerRecipe(mix.getItems()[0].getItem(), new AddContainerRecipe(mix))
        );
    }

    public static int createContainerMixFiles(CommandContext<CommandSourceStack> context) {
        return processContainerMixFiles(
                context,
                "brewmaster/container_mix_files",
                PotionFileHandler::createContainerMixFileName,
                potionMix -> new AddContainerMixRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to())
        );
    }

    public static int removeContainerMixFiles(CommandContext<CommandSourceStack> context) {
        return processContainerMixFiles(
                context,
                "brewmaster/remove_container_mix_files",
                PotionFileHandler::removeContainerMixFileName,
                potionMix -> new RemoveContainerMixRecipe(new OldContainerRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()))
        );
    }

    public static int replaceContainerMixFiles(CommandContext<CommandSourceStack> context) {
        return processContainerMixFiles(
                context,
                "brewmaster/replace_container_mix_files",
                PotionFileHandler::replaceContainerMixFileName,
                potionMix -> new ReplaceContainerMixRecipe(new OldContainerRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()), new AddContainerMixRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()))
        );
    }


    private static int processPotionFiles(
            CommandContext<CommandSourceStack> context,
            String directoryPath,
            FileNameGenerator<PotionBrewing.Mix<Potion>> fileNameGenerator,
            RecipeConstructor<PotionBrewing.Mix<Potion>> recipeConstructor
    ) {
        AtomicInteger updatedFileCount = new AtomicInteger(0);
        Entity nullableSummoner = context.getSource().getEntity();
        ServerLevel level = context.getSource().getLevel();
        Path path = FMLPaths.GAMEDIR.get().resolve(directoryPath);

        try {
            List<PotionBrewing.Mix<Potion>> potionMixes = ((PotionBrewingAccessor) level.potionBrewing()).brewmaster$getPotionMixes();
            createDirectoryIfNotExists(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            for (PotionBrewing.Mix<Potion> potionMix : potionMixes) {
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(potionMix.ingredient().getItems()[0].getItem());
                String fileName = fileNameGenerator.generateFileName(potionMix, key);
                writePotionMixToFile(gson, path.resolve(fileName), recipeConstructor.constructRecipe(potionMix), updatedFileCount);
            }
        } catch (Exception ex) {
            context.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            LOGGER.error("Exception thrown while processing potion files: ", ex);
        }

        if (nullableSummoner instanceof Player player) {
            sendPlayerMessage(player, "A new directory has been created at: " + path);
            sendPlayerMessage(player, "Updated " + updatedFileCount + " files");
        }
        return 0;
    }

    private static int processContainerFiles(
            CommandContext<CommandSourceStack> context,
            String directoryPath,
            FileNameGenerator<Ingredient> fileNameGenerator,
            RecipeConstructor<Ingredient> recipeConstructor
    ) {
        AtomicInteger updatedFileCount = new AtomicInteger(0);
        Entity nullableSummoner = context.getSource().getEntity();
        ServerLevel level = context.getSource().getLevel();
        Path path = FMLPaths.GAMEDIR.get().resolve(directoryPath);

        try {
            List<Ingredient> containers = ((PotionBrewingAccessor) level.potionBrewing()).brewmaster$getContainers();
            createDirectoryIfNotExists(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            for (Ingredient container : containers) {
                Item item = container.getItems()[0].getItem();
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                String fileName = fileNameGenerator.generateFileName(container,key);
                writePotionMixToFile(gson, path.resolve(fileName), recipeConstructor.constructRecipe(container), updatedFileCount);
            }
        } catch (Exception ex) {
            context.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            LOGGER.error("Exception thrown while processing container files: ", ex);
        }

        if (nullableSummoner instanceof Player player) {
            sendPlayerMessage(player, "A new directory has been created at: " + path);
            sendPlayerMessage(player, "Updated " + updatedFileCount + " files");
        }
        return 0;
    }

    private static int processContainerMixFiles(
            CommandContext<CommandSourceStack> context,
            String directoryPath,
            FileNameGenerator<PotionBrewing.Mix<Item>> fileNameGenerator,
            RecipeConstructor<PotionBrewing.Mix<Item>> recipeConstructor
    ) {
        AtomicInteger updatedFileCount = new AtomicInteger(0);
        Entity nullableSummoner = context.getSource().getEntity();
        ServerLevel level = context.getSource().getLevel();
        Path path = FMLPaths.GAMEDIR.get().resolve(directoryPath);

        try {
            List<PotionBrewing.Mix<Item>> containers = ((PotionBrewingAccessor) level.potionBrewing()).brewmaster$getMixes();
            createDirectoryIfNotExists(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            for (PotionBrewing.Mix<Item> mix : containers) {
                Item item = mix.ingredient().getItems()[0].getItem();
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                String fileName = fileNameGenerator.generateFileName(mix,key);
                writePotionMixToFile(gson, path.resolve(fileName), recipeConstructor.constructRecipe(mix), updatedFileCount);
            }
        } catch (Exception ex) {
            context.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            LOGGER.error("Exception thrown while processing container files: ", ex);
        }

        if (nullableSummoner instanceof Player player) {
            sendPlayerMessage(player, "A new directory has been created at: " + path);
            sendPlayerMessage(player, "Updated " + updatedFileCount + " files");
        }
        return 0;
    }



    private static void createDirectoryIfNotExists(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            LOGGER.error("Error creating directory: ", e);
        }
    }

    private static <T> void writePotionMixToFile(Gson gson, Path filePath, PotionBrewingRecipe recipe, AtomicInteger  updatedFileCount) {
        DataResult<JsonElement> jsonElement = PotionBrewingRecipe.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, recipe);
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            gson.toJson(jsonElement.getOrThrow(), writer);
            updatedFileCount.incrementAndGet();
        } catch (IOException e) {
            LOGGER.error("Error writing potion mix to file: ", e);
        }
    }

    //private static <T> void writeContainerToFile(Gson gson, Path filePath, PotionBrewingRecipe recipe, AtomicInteger  updatedFileCount) {
    //    DataResult<JsonElement> jsonElement = PotionBrewingRecipe.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, recipe);
    //    try (FileWriter writer = new FileWriter(filePath.toFile())) {
    //        gson.toJson(jsonElement.getOrThrow(), writer);
    //        updatedFileCount.incrementAndGet();
    //    } catch (IOException e) {
    //        LOGGER.error("Error writing container to file: ", e);
    //    }
    //}

    private static void sendPlayerMessage(Player player, String message) {
        player.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.GREEN));
    }

    private static String createPotionFileName(PotionBrewing.Mix<Potion> potionMix, ResourceLocation key) {
        return String.format("potion_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }

    private static String removePotionFileName(PotionBrewing.Mix<Potion> potionMix, ResourceLocation key) {
        return String.format("remove_potion_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }

    private static String replacePotionFileName(PotionBrewing.Mix<Potion> potionMix, ResourceLocation key) {
        return String.format("replace_potion_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }

    private static String createContainerFileName(Ingredient container) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(container.getItems()[0].getItem());
        return String.format("container_item_%s.json", key.getPath());
    }

    private static String removeContainerFileName(Ingredient container) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(container.getItems()[0].getItem());
        return String.format("remove_container_item_%s.json", key.getPath());
    }

    private static String replaceContainerFileName(Ingredient container) {
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(container.getItems()[0].getItem());
        return String.format("replace_container_item_%s.json", key.getPath());
    }

    private static String createContainerMixFileName(PotionBrewing.Mix<Item> potionMix, ResourceLocation key) {
        return String.format("container_mix_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }

    private static String removeContainerMixFileName(PotionBrewing.Mix<Item> potionMix, ResourceLocation key) {
        return String.format("remove_container_mix_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }

    private static String replaceContainerMixFileName(PotionBrewing.Mix<Item> potionMix, ResourceLocation key) {
        return String.format("remove_container_mix_from_%s_with_ingredient_%s_to_%s.json",
                potionMix.from().getRegisteredName().split(":")[1],
                key.getPath(),
                potionMix.to().getRegisteredName().split(":")[1]);
    }



    @FunctionalInterface
    private interface FileNameGenerator<T> {
        String generateFileName(T mix, ResourceLocation key);
    }

    @FunctionalInterface
    private interface RecipeConstructor<T> {
        PotionBrewingRecipe constructRecipe(T mix);
    }
}
