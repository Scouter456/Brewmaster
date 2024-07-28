package com.scouter.brewmaster.datagen;

import com.scouter.brewmaster.Brewmaster;
import com.scouter.brewmaster.data.*;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class WikiPageGenerator extends WikiPageBuilderProvider {
    public WikiPageGenerator(PackOutput output) {
        super(output, "wiki", Brewmaster.MODID);
    }

    @Override
    protected void generateWikiPages(BiConsumer<String, Supplier<String>> consumer) {
        createWikiPage("Home", homePage(), consumer);
    }


    private WikiPageBuilder homePage() {
        WikiPageBuilder builder = new WikiPageBuilder();

        builder.addHeading("Brewmaster", 3);

        builder.addParagraph("Brewmaster is a Minecraft mod that allows you to define, add, remove, or replace potion recipes, container mixes, or containers using a datapack.");

        builder.addParagraph("The datapack entries need to be added to data/<id>/brewmaster/potion_recipe/");
        builder.addParagraph("The mod also adds a command to generate recipe files for all the current potions in the game, which can then be used to to change them");

        builder.startCollapsibleSection("Potion Recipes");
        builder.addHeading("Adding potions mixes",2);
        AddPotionMixRecipe addPotionMixRecipe = new AddPotionMixRecipe(Potions.HARMING, Items.ACACIA_BOAT, Potions.HEALING);
        builder.addParagraph("Below is an example of what the JSON file should look like for adding a new potion mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, addPotionMixRecipe));
        builder.addParagraph("This JSON defines a new recipe for a healing potion from an acacia boat and a potion of harming");

        builder.addParagraph("The item `item` key can also be changed with `tag` to allow for tags to be used instead of a single item as ingredient");


        RemovePotionMixRecipe removePotionMixRecipe = new RemovePotionMixRecipe(new OldRecipe(Potions.AWKWARD, Items.BLAZE_ROD, Potions.STRENGTH));
        builder.addParagraph("Below is an example of what the JSON file should look like for removing a potion mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, removePotionMixRecipe));
        builder.addParagraph("This JSON checks for the items and potions defined in the recipe list and if it finds them it will remove the recipe.");


        ReplacePotionMixRecipe replacePotionMixRecipe = new ReplacePotionMixRecipe(new OldRecipe(Potions.AWKWARD, Items.BLAZE_ROD, Potions.STRENGTH), new AddPotionMixRecipe(Potions.AWKWARD, Items.EMERALD, Potions.STRENGTH));
        builder.addParagraph("Below is an example of what the JSON file should look like for replacing a potion mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, replacePotionMixRecipe));
        builder.addParagraph("This JSON checks for the items and potions defined in the recipe list and if it finds them it will replace it with the recipe defined.");
        builder.addParagraph("This is essentially a remove and add operation added together.");
        builder.addParagraph("The item `item` key can also be changed with `tag` to allow for tags to be used instead of a single item as ingredient");



        AddPotionStartMixRecipe addPotionStartMixRecipe = new AddPotionStartMixRecipe(Items.ICE, Potions.SLOWNESS);
        builder.addParagraph("Below is an example of what the JSON file should look like for adding a potion start mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, addPotionStartMixRecipe));
        builder.addParagraph("This JSON adds a base mix with a potion of water and awkward.");
        builder.addParagraph("water -> mundane.");
        builder.addParagraph("awkward -> result.");

        builder.endCollapsibleSection();

        builder.startCollapsibleSection("Item Mixes");
        builder.addHeading("Adding container mixes",2);
        AddContainerMixRecipe addContainerMixRecipe = new AddContainerMixRecipe(Items.DIAMOND.builtInRegistryHolder(), Items.ACACIA_BOAT, Items.EMERALD.builtInRegistryHolder());
        builder.addParagraph("Below is an example of what the JSON file should look like for adding a new container mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, addContainerMixRecipe));
        builder.addParagraph("This JSON defines a new recipe for an emerald from an acacia boat and a diamond");

        builder.addParagraph("The item `item` key can also be changed with `tag` to allow for tags to be used instead of a single item as ingredient");


        RemoveContainerMixRecipe removeContainerMixRecipe = new RemoveContainerMixRecipe(new OldContainerRecipe(Items.POTION.builtInRegistryHolder(), Items.GUNPOWDER, Items.SPLASH_POTION.builtInRegistryHolder()));
        builder.addParagraph("Below is an example of what the JSON file should look like for removing a container mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, removeContainerMixRecipe));
        builder.addParagraph("This JSON checks for the items defined in the recipe list and if it finds them it will remove the recipe");
        builder.addParagraph("This one looks for a potion item, gunpowder and splash potion result");


        ReplaceContainerMixRecipe replaceContainerMixRecipe = new ReplaceContainerMixRecipe(new OldContainerRecipe(Items.POTION.builtInRegistryHolder(), Items.GUNPOWDER, Items.SPLASH_POTION.builtInRegistryHolder()), new AddContainerMixRecipe(Items.DIAMOND.builtInRegistryHolder(), Items.ACACIA_BOAT, Items.SPLASH_POTION.builtInRegistryHolder()));
        builder.addParagraph("Below is an example of what the JSON file should look like for replacing a container mix:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, replaceContainerMixRecipe));
        builder.addParagraph("This JSON checks for the items defined in the list and if it finds them it will replace it with the recipe defined.");
        builder.addParagraph("This is essentially a remove and add operation added together.");
        builder.addParagraph("The item `item` key can also be changed with `tag` to allow for tags to be used instead of a single item as ingredient");
        builder.addParagraph("This one looks for a potion item, gunpowder and splash potion result and replaces it with a recipe that uses a diamond and acacia boat to give a splash potion");

        builder.endCollapsibleSection();

        builder.startCollapsibleSection("Containers");
        builder.addHeading("Adding containers",2);
        AddContainerRecipe addContainerRecipe = new AddContainerRecipe(Items.DIAMOND);
        builder.addParagraph("Below is an example of what the JSON file should look like for adding a new container:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, addContainerRecipe));
        builder.addParagraph("This JSON defines a new container for a diamond");



        RemoveContainerRecipe removeContainerRecipe = new RemoveContainerRecipe(Items.SPLASH_POTION);
        builder.addParagraph("Below is an example of what the JSON file should look like for removing a container:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, removeContainerRecipe));
        builder.addParagraph("This JSON checks for a splash potion as container and if it finds it, it will remove it, preventing any splash potions");


        ReplaceContainerRecipe replaceContainerRecipe = new ReplaceContainerRecipe(Items.SPLASH_POTION, new AddContainerRecipe(Items.CHEST));
        builder.addParagraph("Below is an example of what the JSON file should look like for replacing a container:");
        builder.addCodeBlock(encodeDataToJsonString(PotionBrewingRecipe.DIRECT_CODEC, replaceContainerRecipe));
        builder.addParagraph("This JSON checks for a splash potion and if it finds it, it will replace it with a chest as container");
        builder.addParagraph("This is essentially a remove and add operation added together.");


        builder.endCollapsibleSection();


        return builder;
    }


    private void createWikiPage(String filename, WikiPageBuilder builder, BiConsumer<String, Supplier<String>> consumer) {
        consumer.accept(filename, builder::getContent);
    }
}
