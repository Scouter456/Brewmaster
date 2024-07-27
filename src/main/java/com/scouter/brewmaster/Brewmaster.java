package com.scouter.brewmaster;

import com.scouter.brewmaster.command.BrewCommand;
import com.scouter.brewmaster.data.BrewmasterJsonManager;
import com.scouter.brewmaster.events.FabricEvents;
import com.scouter.brewmaster.message.PotionBrewingS2C;
import com.scouter.brewmaster.setup.Registration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Brewmaster implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("brewmaster");
	public static final String MODID = "brewmaster";

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.playS2C().register(PotionBrewingS2C.TYPE, PotionBrewingS2C.STREAM_CODEC);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new BrewmasterJsonManager());
		CommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> BrewCommand.register(commandDispatcher));

		Registration.init();
		FabricEvents.onServerStart();
		FabricEvents.endDataReload();
		FabricEvents.onDataSynch();

	}

	public static ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
	}
}