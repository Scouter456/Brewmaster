package com.scouter.brewmaster.datagen;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.data.*;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

import java.util.function.Consumer;

import static com.scouter.brewmaster.Brewmaster.prefix;

public class PotionRecipeDataGenerator extends PotionRecipeProvider{


    public PotionRecipeDataGenerator(PackOutput pOutput) {
        super(pOutput, Brewmaster.MODID);
    }

    @Override
    protected void createRecipe(Consumer<PotionRecipeConsumer> pWriter) {
        addRecipe(pWriter, prefix("replace_invisibility_recipe"), new ReplacePotionMixRecipe( new OldRecipe(
                Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY
        ), new AddPotionMixRecipe(
                Potions.HEALING, Items.DIAMOND, Potions.INVISIBILITY
        )));
        addRecipe(pWriter, prefix("remove_long_from_fermented_spider_eye_invisiblity"), new RemovePotionMixRecipe( new OldRecipe(
                Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY
        )));
       //pBuilder.addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
       addRecipe(pWriter, prefix("add_wind_charged_from_gold_block_and_healing"), new AddPotionMixRecipe( Potions.HEALING, Items.GOLD_BLOCK, Potions.WIND_CHARGED
       ));


        addRecipe(pWriter, prefix("add_emerald_container"), new AddContainerRecipe(Items.EMERALD
        ));

        addRecipe(pWriter, prefix("add_fire_resistance_start_potion"), new AddPotionStartMixRecipe(Items.EMERALD, Potions.FIRE_RESISTANCE
        ));
    }
}
