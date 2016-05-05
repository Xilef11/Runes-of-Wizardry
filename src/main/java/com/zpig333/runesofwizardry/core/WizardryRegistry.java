package com.zpig333.runesofwizardry.core;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.block.DustStorageBlockColor;
import com.zpig333.runesofwizardry.item.DustItemColor;
import com.zpig333.runesofwizardry.item.DustPouchItemColor;
import com.zpig333.runesofwizardry.item.ItemBroom;
import com.zpig333.runesofwizardry.item.ItemDustPouch;
import com.zpig333.runesofwizardry.item.ItemLavastone;
import com.zpig333.runesofwizardry.item.ItemNetherPaste;
import com.zpig333.runesofwizardry.item.ItemPestle;
import com.zpig333.runesofwizardry.item.ItemPlantBalls;
import com.zpig333.runesofwizardry.item.ItemRunicDictionary;
import com.zpig333.runesofwizardry.item.ItemRunicStaff;
import com.zpig333.runesofwizardry.item.ItemSacrificeNegator;
import com.zpig333.runesofwizardry.item.dust.DustDyed;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.recipe.RecipeDustPouch;
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
	public static Item dust_pouch;
	public static Item broom;
	public static Item sacrifice_negator;
	public static IDust dust_dead;

	//dyed dust
	public static Item dust_dyed;
	/**create the instances for all the blocks**/
	public static void initBlocks(){

		lavastone_bricks = new BlockLavastone_bricks(Material.ROCK);
		//Bust Dye + its TileEntity
		dust_dye = new BlockDustDye(Material.ROCK);
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
		dust_dead = new DustPlaceholder("dead", 0xbebebe, false){
			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.item.dust.DustPlaceholder#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
			 */
			@Override
			public void addInformation(ItemStack stack, EntityPlayer playerIn,List<String> tooltip, boolean advanced) {
				tooltip.add(I18n.translateToLocal(References.Lang.USELESS));
			}

			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.api.IDust#dustsMatch(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
			 * Will only match dead dust
			 */
			@Override
			public boolean dustsMatch(ItemStack thisDust, ItemStack other) {
				return other.getItem()==thisDust.getItem();
			}
		};

		dust_pouch = new ItemDustPouch();
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
		DustRegistry.registerDust(DustRegistry.MAGIC_DUST);
		DustRegistry.registerDust(DustRegistry.ANY_DUST);
	}
	/**Create the (vanilla) recipes**/
	public static void initCrafting(){
		//Allows plants to be mashed down into a plantball.  Each plant goes for 1 plantball
		//flowers
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.YELLOW_FLOWER, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
		//tall GROUND
		GameRegistry.addShapelessRecipe(new ItemStack(plantballs, 1, 0), new ItemStack(Blocks.TALLGRASS, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(pestle, 1, OreDictionary.WILDCARD_VALUE));
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
			" Y ", "X X", " X ", 'X',new ItemStack(Blocks.STONE),'Y',new ItemStack(Items.BONE)
		});

		//lavastone
		GameRegistry.addShapelessRecipe(new ItemStack(nether_paste,1),
				new ItemStack(Blocks.NETHERRACK),new ItemStack(pestle,1,OreDictionary.WILDCARD_VALUE),new ItemStack(Items.BLAZE_POWDER));
		GameRegistry.addSmelting(nether_paste, new ItemStack(lavastone,1), 0.2F);
		GameRegistry.addRecipe(new ItemStack(lavastone_bricks,4),new Object[]{
			"XX","XX",'X',new ItemStack(lavastone,1)
		});

		//the dyed dusts
		GameRegistry.addShapelessRecipe(new ItemStack(dust_dyed,32), new ItemStack(Items.BRICK, 1), new ItemStack(Items.DYE, 1, 15), new ItemStack(pestle, 1,OreDictionary.WILDCARD_VALUE));
		GameRegistry.addShapedRecipe(new ItemStack(dust_dye), "XXX","XYX","XXX",'X',new ItemStack(Items.DYE,1,OreDictionary.WILDCARD_VALUE),'Y',new ItemStack(dust_dyed));
		//inert dust
		GameRegistry.addShapelessRecipe(new ItemStack(RWDusts.dust_inert), new ItemStack(Items.CLAY_BALL),new ItemStack(Items.DYE,1,15),new ItemStack(pestle,1, OreDictionary.WILDCARD_VALUE));
		//broom
		GameRegistry.addShapedRecipe(new ItemStack(broom), "  X"," Y ", 'X',new ItemStack(Items.WHEAT),'Y',new ItemStack(Items.STICK));
		//book
		GameRegistry.addShapelessRecipe(new ItemStack(WizardryRegistry.runic_dictionary), new ItemStack(Items.ENCHANTED_BOOK,1,OreDictionary.WILDCARD_VALUE),new ItemStack(WizardryRegistry.runic_staff));
		//staff
		GameRegistry.addShapedRecipe(new ItemStack(runic_staff), " XY"," ZX","W  ",'X',new ItemStack(Items.GOLD_NUGGET),'Y',new ItemStack(Items.DIAMOND),'Z',new ItemStack(Items.STICK),'W',new ItemStack(Items.GOLD_INGOT));
		GameRegistry.addShapedRecipe(new ItemStack(runic_staff), "YX ","XZ ","  W",'X',new ItemStack(Items.GOLD_NUGGET),'Y',new ItemStack(Items.DIAMOND),'Z',new ItemStack(Items.STICK),'W',new ItemStack(Items.GOLD_INGOT));
		//pouches
		GameRegistry.addRecipe(new ShapedOreRecipe(dust_pouch, " X ","YZY"," Y ",'X',new ItemStack(Items.STRING),'Y',new ItemStack(Blocks.WOOL),'Z',new ItemStack(runic_staff)));
		GameRegistry.addRecipe(new RecipeDustPouch());
	}



	public static void initItemRenders() {
		// get the item renderer
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

		//pouches - maybe we can do something to handle custom dust models?
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.dust_pouch,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemDustPouch)dust_pouch).getName()+"_empty", "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.dust_pouch,
				1,
				new ModelResourceLocation(References.texture_path
						+ ((ItemDustPouch)dust_pouch).getName()+"_full", "inventory"));
	}
	public static void registerColors(){
		ItemColors items = Minecraft.getMinecraft().getItemColors();
		items.registerItemColorHandler(new DustPouchItemColor(), WizardryRegistry.dust_pouch);
		registerDustColors();
	}
	private static void registerDustColors(){
		ItemColors itcols = Minecraft.getMinecraft().getItemColors();
		for(IDust dust:DustRegistry.getAllDusts()){
			if(!dust.hasCustomIcon()){
				itcols.registerItemColorHandler(DustItemColor.instance(), dust);
			}
		}
	}
	private static void registerDustBlockColors(){
		BlockColors bcols = Minecraft.getMinecraft().getBlockColors();
		for(IDustStorageBlock block: DustRegistry.getAllBlocks()){
			if(block.getInstance() instanceof ADustStorageBlock)bcols.registerBlockColorHandler(DustStorageBlockColor.instance(), block.getInstance());
		}
	}
	/**Register the rendering/icon for all dusts that use the default model**/
	@Deprecated
	public static void registerDustItemRendering(){
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
			}

		}
	}
	/**registers the rendering for our blocks**/
	public static void registerBlockRenders() {
		//lavastone bricks
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(lavastone_bricks), 0, new ModelResourceLocation(References.texture_path + ((BlockLavastone_bricks) lavastone_bricks).getName(), "inventory"));
		//Dust Dye
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_dye), 0, new ModelResourceLocation(References.texture_path+((BlockDustDye)dust_dye).getName(),"inventory"));
		//placed dust. for NEI/WAILA purposes
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_placed), 0, new ModelResourceLocation(References.texture_path+"dust_placed","inventory"));
		//dust storage items
		//		ModelResourceLocation location = new ModelResourceLocation(References.texture_path+"dust_storage","inventory");
		//		for(IDustStorageBlock b:DustRegistry.getAllBlocks()){
		//			if(b.getInstance() instanceof ADustStorageBlock){
		//				IDust dust = b.getIDust();
		//				for(int meta:dust.getMetaValues()){
		//					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b.getInstance()), meta, location);
		//				}
		//			}
		//		}
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
