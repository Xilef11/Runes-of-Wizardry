package com.zpig333.runesofwizardry.core;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.client.model.ModelBakeEventHandler;
import com.zpig333.runesofwizardry.client.model.ModelDustStorage;
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
	//This must be used on the Client only
	public static void registerDustStorageRendering() {
		// We need to tell Forge how to map our BlockCamouflage's IBlockState to a ModelResourceLocation.
	    // For example, the BlockStone granite variant has a BlockStateMap entry that looks like
	    //   "stone[variant=granite]" (iBlockState)  -> "minecraft:granite#normal" (ModelResourceLocation)
	    // For the camouflage block, we ignore the iBlockState completely and always return the same ModelResourceLocation,
	    //   which is done using the anonymous class below
	    StateMapperBase ignoreState = new StateMapperBase() {
	      @Override
	      protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
	        return ModelDustStorage.modelResourceLocation;
	      }
	    };
	    for(Block b:DustRegistry.getAllBlocks()){
	    	ModelLoader.setCustomStateMapper(b, ignoreState);
	    }

	    // ModelBakeEvent will be used to add our ISmartBlockModel to the ModelManager's registry (the
	    //  registry used to map all the ModelResourceLocations to IBlockModels).  For the stone example there is a map from
	    // ModelResourceLocation("minecraft:granite#normal") to an IBakedModel created from models/block/granite.json.
	    // For the camouflage block, it will map from
	    // CamouflageISmartBlockModelFactory.modelResourceLocation to our CamouflageISmartBlockModelFactory instance
	    MinecraftForge.EVENT_BUS.register(ModelBakeEventHandler.instance);
	}

	public static void registerDustStorageItemRendering() {
		// This is currently necessary in order to make your block render properly when it is an item (i.e. in the inventory
	    //   or in your hand or thrown on the ground).
	    // Minecraft knows to look for the item model based on the GameRegistry.registerBlock.  However the registration of
	    //  the model for each item is normally done by RenderItem.registerItems(), and this is not currently aware
	    //   of any extra items you have created.  Hence you have to do it manually.  This will probably change in future.
	    // It must be done in the init phase, not preinit, and must be done on client only.
		for(IDustStorageBlock b:DustRegistry.getAllBlocks()){
			Item itemBlockDustStorage = GameRegistry.findItem(References.modid, b.getName());
			ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(References.texture_path+"block_dust_storage", "inventory");
			final int DEFAULT_ITEM_SUBTYPE = 0;//XXX multiple meta values might need to be handled
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlockDustStorage, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
		}
	}

}
