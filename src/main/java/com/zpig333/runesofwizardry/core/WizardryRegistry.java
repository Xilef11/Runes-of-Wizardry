package com.zpig333.runesofwizardry.core;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.item.ItemDyedDust;
import com.zpig333.runesofwizardry.item.ItemLavastone;
import com.zpig333.runesofwizardry.item.ItemNetherPaste;
import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import com.zpig333.runesofwizardry.item.ItemRunicDictionary;
import com.zpig333.runesofwizardry.item.ItemRunicStaff;
import com.zpig333.runesofwizardry.item.dust.DustAqua;
import com.zpig333.runesofwizardry.item.dust.DustBlaze;
import com.zpig333.runesofwizardry.item.dust.DustEnder;
import com.zpig333.runesofwizardry.item.dust.DustGlowstone;
import com.zpig333.runesofwizardry.item.dust.DustInert;
import com.zpig333.runesofwizardry.item.dust.DustPlant;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

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

    }

    /**Creates the instances for all the items**/
    public static void initItems(){

        pestle = new ItemPestle();
        
        plantballs = new ItemPlantBalls();

        nether_paste = new ItemNetherPaste();
        
        lavastone=new ItemLavastone();

        runic_dictionary = new ItemRunicDictionary();

        runic_staff = new ItemRunicStaff();
        //The dyed dust. XXX Temporary, will use IDust in the future
        dust_dyed = new ItemDyedDust();
    }

    /**Registers all our dusts with the DustRegistry**/
    public static void initDusts(){
        DustRegistry.registerDust(new DustInert());
        DustRegistry.registerDust(new DustPlant());
        DustRegistry.registerDust(new DustAqua());
        DustRegistry.registerDust(new DustBlaze());
        DustRegistry.registerDust(new DustGlowstone());
        DustRegistry.registerDust(new DustEnder());
    }
    /**Create the (vanilla) recipes**/
    public static void initCrafting(){
    	//TODO use OreDict for plant stuff
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
    }

    
	public static void initItemRenders() {
		// get the item renderer
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		// pestle
		renderItem.getItemModelMesher().register(
				WizardryRegistry.pestle,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemPestle) WizardryRegistry.pestle).getName(),
						"inventory"));
		// other simple items
		renderItem.getItemModelMesher().register(
				WizardryRegistry.lavastone,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemLavastone) WizardryRegistry.lavastone)
								.getName(), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.nether_paste,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemNetherPaste) WizardryRegistry.nether_paste)
								.getName(), "inventory"));
		renderItem
				.getItemModelMesher()
				.register(
						WizardryRegistry.runic_dictionary,
						0,
						new ModelResourceLocation(
								References.texture_path
										+ ((ItemRunicDictionary) WizardryRegistry.runic_dictionary)
												.getName(), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.runic_staff,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemRunicStaff) WizardryRegistry.runic_staff)
								.getName(), "inventory"));
		// plant balls - try changing the meta number only?
		ItemPlantBalls plantballs = (ItemPlantBalls) WizardryRegistry.plantballs;
		renderItem.getItemModelMesher().register(
				WizardryRegistry.plantballs,
				0,
				new ModelResourceLocation(References.texture_path
						+ plantballs.getFullName(0), "inventory"));
		renderItem.getItemModelMesher().register(
				WizardryRegistry.plantballs,
				1,
				new ModelResourceLocation(References.texture_path
						+ plantballs.getFullName(1), "inventory"));
		//maybe we need to setup variants?
		ModelBakery.addVariantName(WizardryRegistry.plantballs, References.texture_path+plantballs.getFullName(0),
				References.texture_path+plantballs.getFullName(1));
		// dyedDusts
		renderItem.getItemModelMesher().register(
				WizardryRegistry.dust_dyed,
				0,
				new ModelResourceLocation(
						References.texture_path
								+ ((ItemDyedDust) WizardryRegistry.dust_dyed)
										.getName(), "inventory"));
	}
	/**Register the rendering/icon for all dusts that use the default model**/
	public static void registerDustItemRendering(){
    	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    	//The location of the JSON for default dusts
    	ModelResourceLocation dustModel = new ModelResourceLocation(References.texture_path+"default_dusts","inventory");
    	
    	for(IDust d:DustRegistry.getAllDusts()){
    		if(!d.hasCustomIcon()){
    			List<ItemStack> subDusts = new LinkedList<ItemStack>();
    			//Things must (probably) be registered for all meta values
    			d.getSubItems(d, RunesOfWizardry.wizardry_tab, subDusts);
    			for(ItemStack i:subDusts){
    				//ModelLoader.setCustomModelResourceLocation(d, i.getMetadata(), dustModel);
    				renderItem.getItemModelMesher().register(d, i.getMetadata(), dustModel);
    			}
    			
    			ModelBakery.addVariantName(d, References.texture_path+"default_dusts");
    		}
    		
    	}
    }
	/**registers the rendering for our blocks**/
	public static void registerBlockRenders() {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//lavastone bricks
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(lavastone_bricks), 0, new ModelResourceLocation(References.texture_path + ((BlockLavastone_bricks) lavastone_bricks).getName(), "inventory"));
		//Dust Dye
		renderItem.getItemModelMesher().register(Item.getItemFromBlock(dust_dye), 0, new ModelResourceLocation(References.texture_path+((BlockDustDye)dust_dye).getName(),"inventory"));		
	}


}
