package com.zpig333.runesofwizardry.core;

import com.zpig333.runesofwizardry.block.BlockDustBlocks;
import com.zpig333.runesofwizardry.block.itemblocks.ItemBlockDustBlocks;
import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by zombiepig333 on 17-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class WizardryRegistry {

    public static Block dust_blocks;

    public static Item pestle;
    public static Item plant_balls;

    public static void initBlocks(){
        dust_blocks = new BlockDustBlocks(Material.clay).setBlockName("dust_storage");
        GameRegistry.registerBlock(dust_blocks, ItemBlockDustBlocks.class, "dust_storage");
    }


    public static void initItems(){

        pestle = new ItemPestle().setUnlocalizedName("pestle_wizardry");
        GameRegistry.registerItem(pestle, "pestle_wizardry");

        plant_balls = new ItemPlantBalls().setUnlocalizedName("plant_balls");
        GameRegistry.registerItem(plant_balls, "plant_balls");
    }

    public static void initCrafting(){

        //Allows plants to be mashed down into a plantball.  Each plant goes for 1 plantball
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));


        //Stuuuuuuupid mojang had to make 2 different leaves, didn't they?
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves2, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));

        //Craft the small plant balls into larger ones... for now.
        GameRegistry.addRecipe(new ItemStack(plant_balls, 1, 0), new Object[]{
                "XXX", "XXX", "XXX", 'X', new ItemStack(plant_balls, 1, 1)
        });

    }
}
