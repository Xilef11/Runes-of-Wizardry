package com.zpig333.runesofwizardry.core;

import java.util.List;

import com.zpig333.runesofwizardry.RecipeDumper;
import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.block.BlockDustDye;
import com.zpig333.runesofwizardry.block.BlockDustPlaced;
import com.zpig333.runesofwizardry.block.BlockLavastone_bricks;
import com.zpig333.runesofwizardry.block.DustStorageItemBlock;
import com.zpig333.runesofwizardry.item.ItemBroom;
import com.zpig333.runesofwizardry.item.ItemDustPouch;
import com.zpig333.runesofwizardry.item.ItemInscription;
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
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDead;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber
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
	public static Item inscription;
	
	//dyed dust
	public static IDust dust_dyed;
	public static IDust dust_dead;

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
		GameRegistry.registerTileEntity(TileEntityDustDead.class, "te_dust_dead");
		
	}
	@SubscribeEvent
	public static void onBlocksRegister(RegistryEvent.Register<Block> event){
		//register blocks
		event.getRegistry().registerAll(lavastone_bricks, dust_dye, dust_placed);

		//register dust blocks
		for(IDustStorageBlock block : DustRegistry.getAllBlocks()){
			WizardryLogger.logInfo("Registering dust block: "+block.getName());
			event.getRegistry().register(block.getInstance());
		}
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
		
		inscription =  new ItemInscription();//TODO re-add Baubles when it's ready.
		
		sacrifice_negator = new ItemSacrificeNegator();

		dust_pouch = new ItemDustPouch();
		
		dust_dyed = new DustDyed();
		dust_dead = new DustPlaceholder("dead", 0xbebebe, false){
			/* (non-Javadoc)
			 * @see com.zpig333.runesofwizardry.item.dust.DustPlaceholder#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
			 */
			@Override
			public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag){
				tooltip.add(RunesOfWizardry.proxy.translate(References.Lang.USELESS));
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
		
	}
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event){
		//register all items
		event.getRegistry().registerAll(pestle, plantballs, nether_paste, lavastone, runic_dictionary, runic_staff, dust_pouch, broom, sacrifice_negator, inscription);

		//register ItemBlocks
		event.getRegistry().register(new ItemBlock(lavastone_bricks).setRegistryName(lavastone_bricks.getRegistryName()));
		event.getRegistry().register(new ItemBlock(dust_dye).setRegistryName(dust_dye.getRegistryName()));
		event.getRegistry().register(new ItemBlock(dust_placed).setRegistryName(dust_placed.getRegistryName()));

		//addon dust items
		for(IDust dust: DustRegistry.getAllDusts()){
			event.getRegistry().register(dust);
			IDustStorageBlock dustBlock = DustRegistry.getBlock(dust);
			if(dustBlock!=null){
				WizardryLogger.logInfo("registering dust itemblock: "+dustBlock.getName());
				event.getRegistry().register(new DustStorageItemBlock(dustBlock.getInstance()));
			}
		}
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
		GameRegistry.addSmelting(nether_paste, new ItemStack(lavastone,1), 0.2F);
		//pouches
		RecipeSorter.register(References.modid+":dustPouch", RecipeDustPouch.class, RecipeSorter.Category.SHAPELESS, "");

		//Crafting the blocks
		for(IDustStorageBlock dustBlock:DustRegistry.getAllBlocks()){
			IDust dustclass = dustBlock.getIDust();
			String modID = dustclass.getRegistryName().getResourceDomain();
			for(int i:dustclass.getMetaValues()){
				GameRegistry.addShapedRecipe(new ResourceLocation(modID,dustclass.getName()+"_to_block"+i),null,new ItemStack(dustBlock.getInstance(), 1, i), 
						new Object[]{"XXX","XXX","XXX",'X',new ItemStack(dustclass,1,i)});

				ItemStack block = new ItemStack(dustBlock.getInstance(), 1, i);

				GameRegistry.addShapelessRecipe(new ResourceLocation(modID,dustclass.getName()+"_from_block"+i),null,
						new ItemStack(dustclass,9,i), Ingredient.fromStacks(block));

			}
		}

	}

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event){
		event.getRegistry().register(new RecipeDustPouch().setRegistryName(References.modid, "dustpouch"));
	}
	
	@SubscribeEvent
	public static void initItemRenders(ModelRegistryEvent event) {
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
		//inscriptions
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.inscription,
				0,
				new ModelResourceLocation(References.texture_path
						+ ((ItemInscription)inscription).getName()+"_blank", "inventory"));
		ModelLoader.setCustomModelResourceLocation(
				WizardryRegistry.inscription,
				1,
				new ModelResourceLocation(References.texture_path
						+ ((ItemInscription)inscription).getName()+"_painted", "inventory"));
	}
	
	/**Register the rendering/icon for all dusts that use the default model**/
	@SubscribeEvent
	public static void registerDustRendering(ModelRegistryEvent event){
		//The location of the JSON for default dusts
		ModelResourceLocation dustModel = new ModelResourceLocation(References.texture_path+"default_dusts","inventory");

		for(IDust dustclass:DustRegistry.getAllDusts()){
			if(!dustclass.hasCustomIcon()){
				NonNullList<ItemStack> subDusts = NonNullList.create();
				//Things must (probably) be registered for all meta values
				for(int meta:dustclass.getMetaValues()){
					ModelLoader.setCustomModelResourceLocation(dustclass, meta, dustModel);
				}
			}
		}

		ModelResourceLocation blockModel = new ModelResourceLocation(References.texture_path+"dust_storage","inventory");
		//register dust blocks
		for(IDustStorageBlock dustBlock : DustRegistry.getAllBlocks()){
			IDust dust = dustBlock.getIDust();
			for(int meta:dust.getMetaValues()){
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dustBlock.getInstance()), meta, blockModel);
			}
			if(dustBlock instanceof ADustStorageBlock){
				WizardryLogger.logInfo("Creating StateMapper for "+dustBlock.getName());
				StateMapperBase mapper = new StateMapperBase() {
					@Override
					protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
						ModelResourceLocation loc =new ModelResourceLocation(References.texture_path+"dust_storage");
						//System.err.println(loc.toString());
						return loc;
					}
				};
				ModelLoader.setCustomStateMapper(dustBlock.getInstance(), mapper);
			}
		}
	}
	
	/**registers the rendering for our blocks**/
	@SubscribeEvent
	public static void registerBlockRenders(ModelRegistryEvent event) {
		//lavastone bricks
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(lavastone_bricks), 0, new ModelResourceLocation(References.texture_path + ((BlockLavastone_bricks) lavastone_bricks).getName(), "inventory"));
		//Dust Dye
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_dye), 0, new ModelResourceLocation(References.texture_path+((BlockDustDye)dust_dye).getName(),"inventory"));
		//placed dust. for NEI/WAILA purposes
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(dust_placed), 0, new ModelResourceLocation(References.texture_path+"dust_placed","inventory"));
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
