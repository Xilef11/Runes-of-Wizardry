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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.item.ItemBroom;
import com.zpig333.runesofwizardry.item.ItemLavastone;
import com.zpig333.runesofwizardry.item.ItemNetherPaste;
import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import com.zpig333.runesofwizardry.item.ItemRunicDictionary;
import com.zpig333.runesofwizardry.item.ItemRunicStaff;
import com.zpig333.runesofwizardry.item.ItemSacrificeNegator;
import com.zpig333.runesofwizardry.item.dust.DustDyed;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
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
	public static Item broom;
	public static Item sacrifice_negator;
	public static IDust dust_dead;

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
		GameRegistry.registerTileEntity(TileEntityDustActive.class, "te_dust_active");

	}

	/**Creates the instances for all the items**/
	public static void initItems(){

		pestle = new ItemPestle();

		plantballs = new ItemPlantBalls();

		nether_paste = new ItemNetherPaste();

		lavastone=new ItemLavastone();

		runic_dictionary = new ItemRunicDictionary();

		runic_staff = new ItemRunicStaff();

		broom = new ItemBroom();
		sacrifice_negator = new ItemSacrificeNegator();
		//dyed dust
		dust_dyed = new DustDyed();
		dust_dead = new IDust() {
			
			@Override
			public int getSecondaryColor(ItemStack stack) {
				return getPrimaryColor(stack);
			}
			
			@Override
			public int getPrimaryColor(ItemStack stack) {
				return 0xbebebe;
			}
			
			@Override
			public ItemStack[] getInfusionItems(ItemStack stack) {
				return null;
			}
			
			@Override
			public String getDustName() {
				return "dead";
			}

			/* (non-Javadoc)
			 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
			 */
			@Override
			public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
				tooltip.add(StatCollector.translateToLocal(References.Lang.USELESS));
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.IDust#creativeTab()
			 */
			@Override
			public CreativeTabs creativeTab() {
				return null;
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.IDust#isMagicDust(net.minecraft.item.ItemStack)
			 */
			@Override
			public boolean isMagicDust(ItemStack stack) {
				return false;
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.IDust#hasCustomBlock()
			 */
			@Override
			public boolean hasCustomBlock() {
				return true;
			}
			
			
		};
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
		DustRegistry.registerDust(dust_dead);
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
				new ItemStack(Blocks.netherrack),new ItemStack(pestle,1,OreDictionary.WILDCARD_VALUE),new ItemStack(Items.blaze_powder));
		GameRegistry.addSmelting(nether_paste, new ItemStack(lavastone,1), 0.2F);
		GameRegistry.addRecipe(new ItemStack(lavastone_bricks,4),new Object[]{
			"XX","XX",'X',new ItemStack(lavastone,1)
		});

		//the dyed dusts
		GameRegistry.addShapelessRecipe(new ItemStack(dust_dyed,32), new ItemStack(Items.brick, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(pestle, 1,OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapedRecipe(new ItemStack(dust_dye), "XXX","XYX","XXX",'X',new ItemStack(Items.dye,1,OreDictionary.WILDCARD_VALUE),'Y',new ItemStack(dust_dyed));
		//inert dust
		GameRegistry.addShapelessRecipe(new ItemStack(RWDusts.dust_inert), new ItemStack(Items.clay_ball),new ItemStack(Items.dye,1,15),new ItemStack(pestle,1, OreDictionary.WILDCARD_VALUE));
		//broom
		GameRegistry.addShapedRecipe(new ItemStack(broom), "  X"," Y ", 'X',new ItemStack(Items.wheat),'Y',new ItemStack(Items.stick));
	}



	public static void initItemRenders() {
		// get the item renderer
		//RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		// pestle
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.pestle,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemPestle) WizardryRegistry.pestle).getName(),
						"inventory"));
		// other simple items
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.lavastone,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemLavastone) WizardryRegistry.lavastone)
						.getName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.nether_paste,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemNetherPaste) WizardryRegistry.nether_paste)
						.getName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.runic_dictionary,
				0,
				new ModelResourceLocation(
						References.texture_path
						+ ((ItemRunicDictionary) WizardryRegistry.runic_dictionary)
						.getName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.runic_staff,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemRunicStaff) WizardryRegistry.runic_staff)
						.getName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.broom,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemBroom) WizardryRegistry.broom)
						.getName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.sacrifice_negator,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemSacrificeNegator) WizardryRegistry.sacrifice_negator)
						.getName(), "inventory"));
		// plant balls - try changing the meta number only?
		ItemPlantBalls plantballs = (ItemPlantBalls) WizardryRegistry.plantballs;
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.plantballs,
				0,
				new ModelResourceLocation(References.texture_path
						+ plantballs.getFullName(0), "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.plantballs,
				1,
				new ModelResourceLocation(References.texture_path
						+ plantballs.getFullName(1), "inventory"));
		//maybe we need to setup variants?
//		ModelBakery.addVariantName(WizardryRegistry.plantballs, References.texture_path+plantballs.getFullName(0),
//				References.texture_path+plantballs.getFullName(1));
		//ModelBakery.registerItemVariants(WizardryRegistry.plantballs, new ResourceLocation(References.texture_path+plantballs.getFullName(0)),new ResourceLocation(References.texture_path+plantballs.getFullName(1)));
	}
	/**Register the rendering/icon for all dusts that use the default model**/
	public static void registerDustItemRendering(){
		//RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//The location of the JSON for default dusts
		ModelResourceLocation dustModel = new ModelResourceLocation(References.texture_path+"default_dusts","inventory");

		for(IDust d:DustRegistry.getAllDusts()){
			if(!d.hasCustomIcon()){
				List<ItemStack> subDusts = new LinkedList<ItemStack>();
				//Things must (probably) be registered for all meta values
				d.getSubItems(d, RunesOfWizardry.wizardry_tab, subDusts);
				for(ItemStack i:subDusts){
					ModelLoader.setCustomModelResourceLocation(d, i.getMetadata(), dustModel);
				}
				//ModelBakery.addVariantName(d, References.texture_path+"default_dusts");
				//ModelBakery.registerItemVariants(d, dustModel);
			}

		}
	}
	/**registers the rendering for our blocks**/
	public static void registerBlockRenders() {
		//RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//lavastone bricks
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(lavastone_bricks), 0, new ModelResourceLocation(References.texture_path + ((BlockLavastone_bricks) lavastone_bricks).getName(), "inventory"));
		//Dust Dye
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_dye), 0, new ModelResourceLocation(References.texture_path+((BlockDustDye)dust_dye).getName(),"inventory"));
		//placed dust. for NEI/WAILA purposes
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_placed), 0, new ModelResourceLocation(References.texture_path+"dust_placed","inventory"));
		//dust storage items
		ModelResourceLocation location = new ModelResourceLocation(References.texture_path+"dust_storage","inventory");
		for(IDustStorageBlock b:DustRegistry.getAllBlocks()){
			if(b.getInstance() instanceof ADustStorageBlock){
				IDust dust = b.getIDust();
				for(int meta:dust.getMetaValues()){
					ItemStack stack = new ItemStack(dust,1,meta);
					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b.getInstance()), meta, location);
				}
				//ModelBakery.registerItemVariants(Item.getItemFromBlock(b.getInstance()), location);
			}
		}
	}
	//registers the recipes for all dusts
	public static void registerDustInfusion() {
		for(IDust dust:DustRegistry.getAllDusts()){
			for(int meta:dust.getMetaValues()){
				ItemStack[] recipe = dust.getInfusionItems(new ItemStack(dust, 1, meta));
				if(recipe!=null && DustRegistry.getBlock(dust)!=null){
					ItemStack output = new ItemStack(DustRegistry.getBlock(dust).getInstance(),1,meta);
					ItemStack input = new ItemStack(DustRegistry.getBlock(RWDusts.dust_inert).getInstance());
					DustRegistry.registerBlockInfusion(recipe,input, output);
				}
			}
		}
	}

}
