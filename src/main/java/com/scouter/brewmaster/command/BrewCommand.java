package com.scouter.brewmaster.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.scouter.brewmaster.data.AddPotionMixRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.data.RemovePotionMixRecipe;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BrewCommand {
    private static final Logger LOGGER = LogUtils.getLogger();




    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("brews")
                .requires(s -> s.hasPermission(2));

        LiteralArgumentBuilder<CommandSourceStack> createBuilder = Commands.literal("create")
                .then(Commands.literal("create_potion_mix_files")
                        .executes(BrewCommand::createPotionFiles))
                .then(Commands.literal("remove_potion_mix_files")
                        .executes(BrewCommand::removePotionFiles));

        builder.then(createBuilder);

        pDispatcher.register(builder);
    }

    public static int createPotionFiles(CommandContext<CommandSourceStack> c) {
        int updatedFile = 0;
        Entity nullableSummoner = c.getSource().getEntity();
        ServerLevel level = c.getSource().getLevel();
        Path PATH = FMLPaths.GAMEDIR.get().resolve("potion_files");
        try {
            List<PotionBrewing.Mix<Potion>> potMix = ((PotionBrewingAccessor)level.potionBrewing()).brewmaster$getPotionMixes();

            try {
                Files.createDirectories(PATH); // Create the directory if it doesn't exist
            } catch (IOException e) {
                LOGGER.error("Error creating directory: ", e);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for(PotionBrewing.Mix<Potion> potionMix : potMix) {
                    AddPotionMixRecipe recipe = new AddPotionMixRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to());

                    DataResult<JsonElement> jsonElement = PotionBrewingRecipe.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, recipe);
                    ResourceLocation key = BuiltInRegistries.ITEM.getKey(potionMix.ingredient().getItems()[0].getItem());
                    try (FileWriter writer = new FileWriter(PATH + "/" + "potion_from_" + potionMix.from().getRegisteredName().split(":")[1]+ "_with_ingredient_" + key.getPath() + "_to_" + potionMix.to().getRegisteredName().split(":")[1] + ".json")) {
                        gson.toJson(jsonElement.getOrThrow(), writer);
                        updatedFile += 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } catch (Exception ex) {
            c.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            ex.printStackTrace();
        }
        if(nullableSummoner instanceof Player player){
            player.sendSystemMessage(Component.literal("A new directory has been created at: " + PATH).withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.literal("Updated " + updatedFile + " files").withStyle(ChatFormatting.GREEN));

        }
        return 0;
    }

    public static int removePotionFiles(CommandContext<CommandSourceStack> c) {
        int updatedFile = 0;
        Entity nullableSummoner = c.getSource().getEntity();
        ServerLevel level = c.getSource().getLevel();
        Path PATH = FMLPaths.GAMEDIR.get().resolve("remove_potion_files");
        try {
            List<PotionBrewing.Mix<Potion>> potMix = ((PotionBrewingAccessor)level.potionBrewing()).brewmaster$getPotionMixes();

            try {
                Files.createDirectories(PATH); // Create the directory if it doesn't exist
            } catch (IOException e) {
                LOGGER.error("Error creating directory: ", e);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for(PotionBrewing.Mix<Potion> potionMix : potMix) {
                RemovePotionMixRecipe recipe = new RemovePotionMixRecipe(new OldRecipe(potionMix.from(), potionMix.ingredient().getItems()[0].getItem(), potionMix.to()));

                DataResult<JsonElement> jsonElement = PotionBrewingRecipe.DIRECT_CODEC.encodeStart(JsonOps.INSTANCE, recipe);
                ResourceLocation key = BuiltInRegistries.ITEM.getKey(potionMix.ingredient().getItems()[0].getItem());
                try (FileWriter writer = new FileWriter(PATH + "/" + "remove_potion_from_" + potionMix.from().getRegisteredName().split(":")[1]+ "_with_ingredient_" + key.getPath() + "_to_" + potionMix.to().getRegisteredName().split(":")[1] + ".json")) {
                    gson.toJson(jsonElement.getOrThrow(), writer);
                    updatedFile += 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            c.getSource().sendFailure(Component.literal("Exception thrown - see log"));
            ex.printStackTrace();
        }
        if(nullableSummoner instanceof Player player){
            player.sendSystemMessage(Component.literal("A new directory has been created at: " + PATH).withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.literal("Updated " + updatedFile + " files").withStyle(ChatFormatting.GREEN));

        }
        return 0;
    }
}
