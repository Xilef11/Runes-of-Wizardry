package com.zpig333.runesofwizardry.core;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.block.BlockDustStorage;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.item.ItemLavastone;
import com.zpig333.runesofwizardry.item.ItemNetherPaste;
import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import com.zpig333.runesofwizardry.item.ItemRunicDictionary;
import com.zpig333.runesofwizardry.item.ItemRunicStaff;
import com.zpig333.runesofwizardry.item.dust.DustDyed;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import cpw.mods.fml.common.registry.GameRegistry;

public class WizardryRegistry {

	public static Block dust_blocks;
	public static Block dust_placed;
	public static Block lavastone_bricks;
	public static Block dust_dye;
	public static Item pestle;
	public static Item plantballs;
	public static Item nether_paste, lavastone;
	public static Item runic_dictionary;
	public static Item runic_staff;

	//dyed dust
	public static Item dust_dyed;
	/**create the instances for all the blocks**/
	public static void initBlocks(){

		lavastone_bricks = new BlockLavastone_bricks(Material.rock);
		//Bust Dye + its TileEntity
		dust_dye = new BlockDustDye(Material.rock);
		GameRegistry.registerTileEntity(TileEntityDustDye.class, "te_Dust_Dye");

		//placed dust
		dust_placed=new BlockDustPlaced();
		GameRegistry.registerTileEntity(TileEntityDustPlaced.class, "te_dust_placed");
		
		dust_blocks = new BlockDustStorage();

	}

	/**Creates the instances for all the items**/
	public static void initItems(){

		pestle = new ItemPestle();

		plantballs = new ItemPlantBalls();

		nether_paste = new ItemNetherPaste();

		lavastone=new ItemLavastone();

		runic_dictionary = new ItemRunicDictionary();

		runic_staff = new ItemRunicStaff();

		//dyed dust
		dust_dyed = new DustDyed();
	}

	/**Registers all our dusts with the DustRegistry**/
	public static void initDusts(){
		DustRegistry.registerDust(RWDusts.dust_inert);
		DustRegistry.registerDust(RWDusts.dust_plant);
		DustRegistry.registerDust(RWDusts.dust_aqua);
		DustRegistry.registerDust(RWDusts.dust_blaze);
		DustRegistry.registerDust(RWDusts.dust_glowstone);
		DustRegistry.registerDust(RWDusts.dust_ender);

		DustRegistry.registerDust((IDust) dust_dyed);
	}
	/**Create the (vanilla) recipes**/
	public static void initCrafting(){
		//    	for(String s:OreDictionary.getOreNames()){
		//    		WizardryLogger.logInfo("Oredict name: "+s);
		//    	}
		//Allows plants to be mashed down into a plantball.  Each plant goes for 1 plantball
		//flowers
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
		//tall grass
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.tallgrass, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
		//Leaves
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(plantballs, 1, 0), "treeLeaves", new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE)));
		//saplings
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(plantballs, 1, 0), "treeSapling", new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE)));
		//would be nice to have other plants easily (i.e all at once) in the oredict...

		//Craft the small plant balls into larger ones... for now.
		GameRegistry.addRecipe(new ItemStack(plantballs, 1, 1), new Object[]{
			"XXX", "XXX", "XXX", 'X', new ItemStack(plantballs, 1, 0)
		});

		//craft the pestle
		GameRegistry.addRecipe(new ItemStack(pestle,1,0), new Object[]{
			" Y ", "X X", " X ", 'X',new ItemStack(Blocks.stone),'Y',new ItemStack(Items.bone)
		});

		//lavastone
		GameRegistry.addShapelessRecipe(new ItemStack(nether_paste,1),
				new ItemStack(Blocks.netherrack),new ItemStack(pestle),new ItemStack(Items.blaze_powder));
		GameRegistry.addSmelting(nether_paste, new ItemStack(lavastone,1), 0.2F);
		GameRegistry.addRecipe(new ItemStack(lavastone_bricks,4),new Object[]{
			"XX","XX",'X',new ItemStack(lavastone,1)
		});

		//the dyed dusts
		GameRegistry.addShapelessRecipe(new ItemStack(dust_dyed,32), new ItemStack(Items.brick, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1));
		GameRegistry.addShapedRecipe(new ItemStack(dust_dye), "XXX","XYX","XXX",'X',new ItemStack(Items.dye,1,OreDictionary.WILDCARD_VALUE),'Y',new ItemStack(dust_dyed));
		//inert dust
		GameRegistry.addShapelessRecipe(new ItemStack(RWDusts.dust_inert), new ItemStack(Items.clay_ball),new ItemStack(Items.dye,1,15),new ItemStack(pestle));
		
	}

	//registers the recipes for all dusts
	public static void registerDustInfusion() {
		for(IDust dust:DustRegistry.getAllDusts()){
			for(int meta:dust.getMetaValues()){
				ItemStack[] recipe = dust.getInfusionItems(new ItemStack(dust, 1, meta));
				if(recipe!=null && DustRegistry.getDefaultBlock(dust)!=null){
					ItemStack output = new ItemStack(DustRegistry.getDefaultBlock(dust),1,meta);
					ItemStack input = new ItemStack(DustRegistry.getDefaultBlock(RWDusts.dust_inert));
					DustRegistry.registerBlockInfusion(recipe,input, output);
				}
			}
		}
	}

}
