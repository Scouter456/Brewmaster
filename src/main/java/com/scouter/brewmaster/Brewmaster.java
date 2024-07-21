package com.scouter.brewmaster;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.command.BrewCommand;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.registry.BMRegistries;
import com.scouter.brewmaster.setup.ModSetup;
import com.scouter.brewmaster.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Brewmaster.MODID)
public class Brewmaster
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "brewmaster";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Brewmaster(IEventBus modEventBus, ModContainer modContainer)
    {

        Registration.init();
        ModSetup.setup();
        IEventBus forgeBus = NeoForge.EVENT_BUS;
        IEventBus modbus = ModLoadingContext.get().getActiveContainer().getEventBus();
        NeoForge.EVENT_BUS.addListener(this::commands);
        modbus.addListener(ModSetup::init);
        modbus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(BMRegistries.Keys.POTION_RECIPE, PotionBrewingRecipe.DIRECT_CODEC, PotionBrewingRecipe.DIRECT_CODEC);
        });
    }

    public static ResourceLocation prefix(String name) {
        return  ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }

    public void commands(RegisterCommandsEvent e) {
        BrewCommand.register(e.getDispatcher());
    }
}
