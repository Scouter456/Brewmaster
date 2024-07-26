package com.scouter.brewmaster.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.scouter.brewmaster.data.AddPotionMixRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.data.RemovePotionMixRecipe;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.util.PotionFileHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import java.util.Optional;

public class BrewCommand {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPE = (ctx, builder) -> {
        return SharedSuggestionProvider.suggest(PotionBrewingRecipesToShow.getRegisteredPotions(), builder);
    };


    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("brews")
                .requires(s -> s.hasPermission(0));

        LiteralArgumentBuilder<CommandSourceStack> createBuilder = Commands.literal("create")
                .then(Commands.literal("create_potion_mix_files")
                        .executes(PotionFileHandler::createPotionFiles))
                .then(Commands.literal("remove_potion_mix_files")
                        .executes(PotionFileHandler::removePotionFiles))
                .then(Commands.literal("replace_potion_mix_files")
                        .executes(PotionFileHandler::replacePotionFiles))
                .then(Commands.literal("create_container_files")
                        .executes(PotionFileHandler::createContainerFiles))
                .then(Commands.literal("remove_container_files")
                        .executes(PotionFileHandler::removeContainerFiles))
                .then(Commands.literal("replace_container_files")
                        .executes(PotionFileHandler::replaceContainerFiles))
                .then(Commands.literal("create_container_mix_files")
                        .executes(PotionFileHandler::createContainerMixFiles))
                .then(Commands.literal("remove_container_mix_files")
                        .executes(PotionFileHandler::removeContainerMixFiles))
                .then(Commands.literal("replace_container_mix_files")
                        .executes(PotionFileHandler::replaceContainerMixFiles))
                .requires(s -> s.hasPermission(2));

        LiteralArgumentBuilder<CommandSourceStack> showBuilder = Commands.literal("show")
                .then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SUGGEST_TYPE)
                        .executes(context -> showRecipe(context, ResourceLocationArgument.getId(context, "recipe"))))
                .requires(s -> s.hasPermission(0));


        builder.then(createBuilder);
        builder.then(showBuilder);

        pDispatcher.register(builder);
    }

    public static int showRecipe(CommandContext<CommandSourceStack> c, ResourceLocation location) {
        Entity nullableSummoner = c.getSource().getEntity();
        ServerLevel level = c.getSource().getLevel();
        Path PATH = FMLPaths.GAMEDIR.get().resolve("brewmaster/potion_files");
        Optional<Holder.Reference<Potion>> reference = BuiltInRegistries.POTION.getHolder(location);
        if(nullableSummoner instanceof ServerPlayer player) {
            if (!reference.isPresent()) {
                player.sendSystemMessage(Component.literal("Potion not available!").withStyle(ChatFormatting.RED));
                return 0;
            } else {
                Holder<Potion> potionHolder = reference.get().getDelegate();
                PotionBrewingRecipesToShow.printMessageForPotion(player, potionHolder);
            }
        }
        return 0;
    }


}
