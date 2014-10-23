package com.zpig333.runesofwizardry.core;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.RunesOfWizardryAPI;
import com.zpig333.runesofwizardry.block.BlockDust;
import com.zpig333.runesofwizardry.block.BlockDustBlocks;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.itemblocks.ItemBlockDustBlocks;
import com.zpig333.runesofwizardry.dusts.*;
import com.zpig333.runesofwizardry.item.*;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class WizardryRegistry {

    public static Block dust_blocks;
    public static Block dust_placed;

    /** The item which all dust pieces are registered under.**/
    public static Item dust_item;
    public static Item pestle;
    public static Item plant_balls;
    public static Item wizardry_dictionary;
    public static Item wizards_staff;
    
    //dyed dust
    public static Item dust_dyed;

    public static void initBlocks(){

        dust_placed = new BlockDust().setBlockName("dust_placed");
        GameRegistry.registerBlock(dust_placed, "dust_placed");

        dust_blocks = new BlockDustBlocks(Material.clay).setBlockName("dust_storage");
        GameRegistry.registerBlock(dust_blocks, ItemBlockDustBlocks.class, "dust_storage");
    }


    public static void initItems(){

        pestle = new ItemPestle().setUnlocalizedName("pestle_wizardry");
        GameRegistry.registerItem(pestle, "pestle_wizardry");

        plant_balls = new ItemPlantBalls().setUnlocalizedName("plant_balls");
        GameRegistry.registerItem(plant_balls, "plant_balls");
        
        dust_item = new ItemDustPieces().setUnlocalizedName("dust").setCreativeTab(RunesOfWizardry.wizardry_tab);;
        GameRegistry.registerItem(dust_item, "dust");

        wizardry_dictionary = new ItemWizardryDictionary().setUnlocalizedName("wizardry_dictionary");
        GameRegistry.registerItem(wizardry_dictionary, "wizardry_dictionary");

        wizards_staff = new ItemWizardsStaff().setUnlocalizedName("wizards_staff");
        GameRegistry.registerItem(wizards_staff, "wizards_staff");
    }

    public static void initDusts(){

        RWDusts instance = new RWDusts();
        RunesOfWizardryAPI.registerDust(0, instance.new DustInert());
        RunesOfWizardryAPI.registerDust(1, instance.new DustPlant());
        RunesOfWizardryAPI.registerDust(2, instance.new DustAqua());
        RunesOfWizardryAPI.registerDust(3, instance.new DustBlaze());
        RunesOfWizardryAPI.registerDust(4, instance.new DustGlowstone());
        RunesOfWizardryAPI.registerDust(5, instance.new DustEnder());
    }

    public static void initCrafting(){

        //Allows plants to be mashed down into a plantball.  Each plant goes for 1 plantball
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));


        //Stuuuuuuupid mojang had to make 2 different leaves, didn't they?
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plant_balls, 1, 1), new ItemStack(Blocks.leaves2, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));

        //Craft the small plant balls into larger ones... for now.
        GameRegistry.addRecipe(new ItemStack(plant_balls, 1, 0), new Object[]{
                "XXX", "XXX", "XXX", 'X', new ItemStack(plant_balls, 1, 1)
        });

        //a way to craft dust chunks and blocks
        GameRegistry.addShapelessRecipe(new ItemStack(dust_item, 1, 0), new ItemStack(Items.clay_ball, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        //all dusts
        for(int i=0;i<6;i++){
            GameRegistry.addRecipe(new ItemStack(dust_blocks, 1, i), new Object[]{
            "XXX", "XXX","XXX", 'X', new ItemStack(dust_item, 1, i)
        });
        GameRegistry.addShapelessRecipe(new ItemStack(dust_item,9,i), new ItemStack(dust_blocks, 1, i));
        }
        
        //craft the pestle
        GameRegistry.addRecipe(new ItemStack(pestle,1,0), new Object[]{
            " Y ", "X X", " X ", 'X',new ItemStack(Blocks.stone),'Y',new ItemStack(Items.bone)
        });
    }

    //TODO temporary to avoid messing up existing methods
    public static void initDec(){
        Block dust_dye = new BlockDustDye(Material.rock).setBlockName("dust_dye_block");
        GameRegistry.registerBlock(dust_dye, "dust_dye_block");
        GameRegistry.registerTileEntity(TileEntityDustDye.class, "te_Dust_Dye");
        NetworkRegistry.INSTANCE.registerGuiHandler(RunesOfWizardry.instance, new GuiHandler());
        
        dust_dyed = new ItemDyedDust();
        GameRegistry.registerItem(dust_dyed, "dust_dyed");
        
        //the dyed dusts
        GameRegistry.addShapelessRecipe(new ItemStack(dust_dyed,32), new ItemStack(Items.brick, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1));
    }
}
