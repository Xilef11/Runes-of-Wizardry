package com.zpig333.runesofwizardry.core;

import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by zombiepig333 on 17-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class WizardryRegistry {

    public static Item pestle;
    public static Item plant_balls;

    public static void initItems(){

        pestle = new ItemPestle().setUnlocalizedName("pestle_wizardry");
        GameRegistry.registerItem(pestle, "pestle_wizardry");

        plant_balls = new ItemPlantBalls().setUnlocalizedName("plant_balls");
        GameRegistry.registerItem(plant_balls, "plant_balls");
    }

    public static void initCrafting(){

        //Stuuuuuuupid mojang had to make 2 different leaves, didn't they?
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves2, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));

        //Craft the small plant balls into larger ones!!
        GameRegistry.addRecipe(new ItemStack(plant_balls, 1, 0), new Object[]{
                "XXX", "XXX", "XXX", 'X', new ItemStack(plant_balls, 1, 1)
        });

    }
}
