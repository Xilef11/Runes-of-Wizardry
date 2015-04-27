package com.zpig333.runesofwizardry.core;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.block.BlockDust;
import com.zpig333.runesofwizardry.block.BlockDustBlocks;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.block.itemblocks.ItemBlockDustBlocks;
import com.zpig333.runesofwizardry.dusts.RWDusts;
import com.zpig333.runesofwizardry.item.*;
import com.zpig333.runesofwizardry.tileentity.TileEntityDust;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class WizardryRegistry {

    public static Block dust_blocks;
    public static Block dust_placed;
    public static Block lavastone_bricks;
    public static Block dust_dye;
    /** The item which all dust pieces are registered under.**/
    public static Item dust_item;
    public static Item pestle;
    public static Item plantballs;
    public static Item nether_paste, lavastone;
    public static Item wizardry_dictionary;
    public static Item wizards_staff;
    
    //dyed dust
    public static Item dust_dyed;

    public static void initBlocks(){
    	//FIXME setBlockName not used
        //dust_placed = new BlockDust().setBlockName("dust_placed");
        //GameRegistry.registerBlock(dust_placed, "dust_placed");
        //GameRegistry.registerTileEntity(TileEntityDust.class, "dust_placed");
      //FIXME setBlockName not used
        //dust_blocks = new BlockDustBlocks(Material.clay).setBlockName("dust_storage");
        //GameRegistry.registerBlock(dust_blocks, ItemBlockDustBlocks.class, "dust_storage");
        
        lavastone_bricks = new BlockLavastone_bricks(Material.rock);

    }


    public static void initItems(){

        pestle = new ItemPestle();
        

        plantballs = new ItemPlantBalls();
		
//        dust_item = new ItemDustPieces().setUnlocalizedName("dust").setCreativeTab(RunesOfWizardry.wizardry_tab);;
//        GameRegistry.registerItem(dust_item, "dust");

        nether_paste = new ItemNetherPaste();
        
        lavastone=new ItemLavastone();

        wizardry_dictionary = new ItemWizardryDictionary();

        wizards_staff = new ItemWizardsStaff();
    }

    public static void initDusts(){

        RWDusts instance = new RWDusts();
        DustRegistry.registerDust(instance.new DustInert());
        DustRegistry.registerDust(instance.new DustPlant());
        DustRegistry.registerDust(instance.new DustAqua());
        DustRegistry.registerDust(instance.new DustBlaze());
        DustRegistry.registerDust(instance.new DustGlowstone());
        DustRegistry.registerDust(instance.new DustEnder());
    }

    public static void initCrafting(){

        //Allows plants to be mashed down into a plantball.  Each plant goes for 1 plantball
        GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 1), new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 1), new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));


        //Stuuuuuuupid mojang had to make 2 different leaves, didn't they?
        GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 1), new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 1), new ItemStack(Blocks.leaves2, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));

        //Craft the small plant balls into larger ones... for now.
        GameRegistry.addRecipe(new ItemStack(plantballs, 1, 0), new Object[]{
                "XXX", "XXX", "XXX", 'X', new ItemStack(plantballs, 1, 1)
        });

        //a way to craft dust chunks and blocks
        GameRegistry.addShapelessRecipe(new ItemStack(dust_item, 1, 0), new ItemStack(Items.clay_ball, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
        //FIXME not working with the new way of implementing dusts
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
        
        //lavastone
        GameRegistry.addShapelessRecipe(new ItemStack(nether_paste,1),
                new ItemStack(Blocks.netherrack),new ItemStack(pestle),new ItemStack(Items.blaze_powder));
        GameRegistry.addSmelting(nether_paste, new ItemStack(lavastone,1), 0.2F);
        GameRegistry.addRecipe(new ItemStack(lavastone_bricks,4),new Object[]{
        	"XX","XX",'X',new ItemStack(lavastone,1)
        });
    }

    //a separate method will allow for easier disabling/enabling via config
    public static void initDecItems(){
    	dust_dye = new BlockDustDye();
        GameRegistry.registerTileEntity(TileEntityDustDye.class, "te_Dust_Dye");
        
        dust_dyed = new ItemDyedDust();
        
       
    }
    public static void initDecRecipes(){
    	 //the dyed dusts
        GameRegistry.addShapelessRecipe(new ItemStack(dust_dyed,32), new ItemStack(Items.brick, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1));
    }
    
	public static void initItemRenders() {
		// get the item renderer
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		// pestle
		renderItem.getItemModelMesher().register(
				WizardryRegistry.pestle,
				0,
				new ModelResourceLocation(References.modid + ":"
						+ ((ItemPestle) WizardryRegistry.pestle).getName(),
						"inventory"));
		// other simple items
		renderItem.getItemModelMesher().register(
				WizardryRegistry.lavastone,
				0,
				new ModelResourceLocation(References.modid
						+ ":"
						+ ((ItemLavastone) WizardryRegistry.lavastone)
								.getName(), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.nether_paste,
				0,
				new ModelResourceLocation(References.modid
						+ ":"
						+ ((ItemNetherPaste) WizardryRegistry.nether_paste)
								.getName(), "inventory"));
		renderItem
				.getItemModelMesher()
				.register(
						WizardryRegistry.wizardry_dictionary,
						0,
						new ModelResourceLocation(
								References.modid
										+ ":"
										+ ((ItemWizardryDictionary) WizardryRegistry.wizardry_dictionary)
												.getName(), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.wizards_staff,
				0,
				new ModelResourceLocation(References.modid
						+ ":"
						+ ((ItemWizardsStaff) WizardryRegistry.wizards_staff)
								.getName(), "inventory"));

		// plant balls - try changing the meta number only?
		renderItem.getItemModelMesher().register(
				WizardryRegistry.plantballs,
				0,
				new ModelResourceLocation(References.modid
						+ ":"
						+ ((ItemPlantBalls) WizardryRegistry.plantballs)
								.getFullName(0), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.plantballs,
				1,
				new ModelResourceLocation(References.modid
						+ ":"
						+ ((ItemPlantBalls) WizardryRegistry.plantballs)
								.getFullName(1), "inventory"));
		// dyedDusts
		renderItem.getItemModelMesher().register(
				WizardryRegistry.dust_dyed,
				0,
				new ModelResourceLocation(
						References.modid
								+ ":"
								+ ((ItemDyedDust) WizardryRegistry.dust_dyed)
										.getName(), "inventory"));
	}


	public static void registerBlockRenders() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//lavastone bricks
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(lavastone_bricks), 0, new ModelResourceLocation(References.modid + ":" + ((BlockLavastone_bricks) lavastone_bricks).getName(), "inventory"));
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(dust_dye), 0, new ModelResourceLocation(References.modid+":"+((BlockDustDye)dust_dye).getName()));		
	}


}
