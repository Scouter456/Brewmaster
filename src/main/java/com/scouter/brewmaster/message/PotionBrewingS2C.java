package com.scouter.brewmaster.message;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.data.OldContainerRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.events.PotionBrewingRecipeExtension;
import com.scouter.brewmaster.util.ClientUtils;
import com.scouter.brewmaster.util.PotionUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public class PotionBrewingS2C implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PotionBrewingS2C> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Brewmaster.MODID, "potion_brewing_s2c"));

    private final List<OldRecipe> potionMixes;
    private final List<OldContainerRecipe> containerRecipes;
    //private final List<PotionBrewing.Mix<Item>> mixes;
    private final  List<Ingredient> containers;
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionBrewingS2C> STREAM_CODEC = StreamCodec.composite(
            OldRecipe.STREAM_CODEC.apply(ByteBufCodecs.list()), PotionBrewingS2C::getPotionMixes,
            OldContainerRecipe.STREAM_CODEC.apply(ByteBufCodecs.list()), PotionBrewingS2C::getContainerRecipes,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),PotionBrewingS2C::getContainers,
            //PotionUtil.ITEM_BREWING_MIX_STREAM_CODEC.apply(ByteBufCodecs.list()), PotionBrewingS2C::getMixes,
            PotionBrewingS2C::new
    );

    public PotionBrewingS2C(List<OldRecipe> potionMixes, List<OldContainerRecipe> containerRecipes, List<Ingredient> containers) {
        this.potionMixes = potionMixes;
        this.containerRecipes = containerRecipes;
        this.containers = containers;
        //this.mixes = mixes;
    }

    //public List<PotionBrewing.Mix<Item>> getMixes() {
    //    return mixes;
    //}

    public List<OldRecipe> getPotionMixes() {
        return potionMixes;
    }

    public List<OldContainerRecipe> getContainerRecipes() {
        return containerRecipes;
    }

    public List<Ingredient> getContainers() {
        return containers;
    }

    public void onPacketReceived(IPayloadContext contextGetter) {
        contextGetter.enqueueWork(this::handlePacketOnMainThread);
    }

    private void handlePacketOnMainThread() {
        Level level = ClientUtils.getLevel();
        ClientLevel clientLevel = (ClientLevel) level;
        if (level == null || clientLevel == null) return;
        PotionBrewing potionBrewing = clientLevel.potionBrewing();

        List<PotionBrewing.Mix<Potion>> mixes = new ArrayList<>();
        List<PotionBrewing.Mix<Item>> container = new ArrayList<>();
        for(OldRecipe oldRecipes : potionMixes) {
            mixes.add(oldRecipes.toMix());
        }
        for(OldContainerRecipe oldRecipes : containerRecipes) {
            container.add(oldRecipes.toMix());
        }

        ((PotionBrewingRecipeExtension) potionBrewing).setPotionMixes(List.copyOf(mixes));
        ((PotionBrewingRecipeExtension) potionBrewing).setMixes(List.copyOf(container));
        ((PotionBrewingRecipeExtension) potionBrewing).setContainer(List.copyOf(containers));
        //((PotionBrewingRecipeExtension)potionBrewing).setMixes(List.copyOf(mixes));    }
    }
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
