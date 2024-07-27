package com.scouter.brewmaster;

import com.mojang.logging.LogUtils;
import com.scouter.brewmaster.command.BrewCommand;
import com.scouter.brewmaster.data.PotionBrewingRecipe;
import com.scouter.brewmaster.registry.BMRegistries;
import com.scouter.brewmaster.setup.ModSetup;
import com.scouter.brewmaster.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Brewmaster.MODID)
public class Brewmaster
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "brewmaster";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public Brewmaster()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init();
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        modEventBus.addListener(ModSetup::init);
        modEventBus.addListener((DataPackRegistryEvent.NewRegistry event) -> {
            event.dataPackRegistry(BMRegistries.Keys.POTION_RECIPE, PotionBrewingRecipe.DIRECT_CODEC, PotionBrewingRecipe.DIRECT_CODEC);


        });
    }

    public static ResourceLocation prefix(String name) {
        return  new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }

    public void commands(RegisterCommandsEvent e) {
        BrewCommand.register(e.getDispatcher());
    }
}
