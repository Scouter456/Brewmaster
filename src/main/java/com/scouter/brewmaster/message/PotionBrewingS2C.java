package com.scouter.brewmaster.message;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.scouter.brewmaster.data.OldContainerRecipe;
import com.scouter.brewmaster.data.OldRecipe;
import com.scouter.brewmaster.mixin.access.PotionBrewingAccessor;
import com.scouter.brewmaster.util.ClientUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PotionBrewingS2C {

    public static final Codec<PotionBrewingS2C> MAPPER = RecordCodecBuilder.create(instance ->
            instance.group(
                    OldRecipe.CODEC.listOf().fieldOf("in").forGetter(PotionBrewingS2C::getPotionMixes),
                    OldContainerRecipe.CODEC.listOf().fieldOf("out").forGetter(PotionBrewingS2C::getMixes),
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("idk").forGetter(PotionBrewingS2C::getContainers)
            ).apply(instance, PotionBrewingS2C::new)
    );

    private final List<OldRecipe> potionMixes;

    private final List<OldContainerRecipe> mixes;
    private final List<Item> containers;

    public PotionBrewingS2C(List<OldRecipe> potionMixes, List<OldContainerRecipe> containerRecipes, List<Item> containers) {
        this.potionMixes = potionMixes;
        this.mixes = containerRecipes;
        this.containers = containers;
        //this.mixes = mixes;
    }

    public void encode(FriendlyByteBuf buffer) {
        CompoundTag encodedTag = (CompoundTag) (MAPPER.encodeStart(NbtOps.INSTANCE, this).result().orElse(new CompoundTag()));
        buffer.writeNbt(encodedTag);
    }

    public static PotionBrewingS2C decode(FriendlyByteBuf buffer) {
        CompoundTag receivedTag = buffer.readNbt();
        return MAPPER.parse(NbtOps.INSTANCE, receivedTag).result().orElse(null);
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        Level level = ClientUtils.getLevel();
        ClientLevel clientLevel = (ClientLevel) level;
        if (level == null || clientLevel == null) return;

        List<PotionBrewing.Mix<Potion>> pMix = new ArrayList<>();
        List<PotionBrewing.Mix<Item>> iMix = new ArrayList<>();
        List<Ingredient> inMix = containers.stream().map(Ingredient::of).toList();

        for(OldRecipe oldRecipe : potionMixes) {
            pMix.add(oldRecipe.toMix());
        }
        for(OldContainerRecipe oldRecipe : mixes) {
            iMix.add(oldRecipe.toMix());
        }
        setAllMixes(pMix, iMix, inMix);
    }

    public List<OldRecipe> getPotionMixes() {
        return potionMixes;
    }

    public List<OldContainerRecipe> getMixes() {
        return mixes;
    }

    public List<Item> getContainers() {
        return containers;
    }


    public static void setAllMixes(List<PotionBrewing.Mix<Potion>> potionMixes, List<PotionBrewing.Mix<Item>> mixes, List<Ingredient> container) {
        brewmaster$setPotionMixes(potionMixes);
        brewmaster$setMixes(mixes);
        brewmaster$setContainer(container);
    }

    static void brewmaster$setPotionMixes(List<PotionBrewing.Mix<Potion>> potionMixes) {
        PotionBrewingAccessor.brewmaster$getPotionMixes().clear();
        PotionBrewingAccessor.brewmaster$getPotionMixes().addAll(potionMixes);

    }

    static void brewmaster$setMixes(List<PotionBrewing.Mix<Item>> mixes) {
        PotionBrewingAccessor.brewmaster$getMixes().clear();
        PotionBrewingAccessor.brewmaster$getMixes().addAll(mixes);
    }

    static void brewmaster$setContainer(List<Ingredient> container) {
        PotionBrewingAccessor.brewmaster$getContainers().clear();
        PotionBrewingAccessor.brewmaster$getContainers().addAll(container);
    }
}
