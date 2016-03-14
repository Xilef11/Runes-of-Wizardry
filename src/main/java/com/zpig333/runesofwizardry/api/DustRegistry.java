package com.zpig333.runesofwizardry.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.block.ADustStorageBlock;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.core.rune.RunesUtil.InvalidRuneException;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;
import com.zpig333.runesofwizardry.util.Utils;

/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

	/** List of all registered dusts **/
	private static List<IDust> dusts = new LinkedList<IDust>();
	private static List<DustPlaceholder> placeholders = new LinkedList<DustPlaceholder>();
	/** map of all the storage blocks**/
	private static Map<IDust,IDustStorageBlock> blocks = new HashMap<IDust,IDustStorageBlock>();
	/** Map of all the infusion recipes **/
	//FUTURE use a custom IRecipe or something
	//private static Map<ItemStack[], ItemStack> recipes = new HashMap<ItemStack[], ItemStack>();
	/**List of all registered runes**/
	private static Map<String,IRune> runes = new LinkedHashMap<String,IRune>();
	/** The dust requirements for all runes**/
	private static Map<String,RunesUtil.RuneStats> duststats = new HashMap<String, RunesUtil.RuneStats>();
	//Special constants
	/**
	 * Represents any "magic" dust
	 */
	public static final IDust MAGIC_DUST = new DustPlaceholder("magic", 0xff00e0, true){

		/* (non-Javadoc)
		 * @see com.zpig333.runesofwizardry.api.IDust#dustsMatch(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
		 */
		@Override
		public boolean dustsMatch(ItemStack thisDust, ItemStack other) {
			IDust otherDust = getDustFromItemStack(other); 
			return otherDust.isMagicDust(other) && !(otherDust instanceof DustPlaceholder);
		}
		
	};
	/** represents any dust **/
	public static final IDust ANY_DUST = new DustPlaceholder("any", 0x00ffff, false){

		/* (non-Javadoc)
		 * @see com.zpig333.runesofwizardry.api.IDust#dustsMatch(net.minecraft.item.ItemStack, net.minecraft.item.ItemStack)
		 */
		@Override
		public boolean dustsMatch(ItemStack thisDust, ItemStack other) {
			IDust oDust = getDustFromItemStack(other);
			return !(oDust instanceof DustPlaceholder);
		}
	};
	/** returns a list of all the registered dusts.
	 * 
	 * @return a LinkedList of all the dusts, in the order they were registered
	 */
	public static List<IDust> getAllDusts(){
		return new LinkedList<IDust>(dusts);
	}
	/**
	 * returns all registered placeholder dusts
	 * @return a LinkedList of the placeholder dusts, in the order of registration
	 */
	public static List<DustPlaceholder> getPlaceholders(){
		return new LinkedList<DustPlaceholder>(placeholders);
	}
	/** returns all the registered runes
	 * 
	 * @return a LinkedList of all runes, in the order they were registered
	 */
	public static List<IRune> getAllRunes(){
		return new LinkedList<IRune>(runes.values());
	}
	/**
	 * Returns the rune registered as the given id, or null if the ID is not found
	 * @param id the id to get the rune for
	 * @return the rune registered as {@code id}, or {@code null} if it dosen't exist
	 */
	public static IRune getRuneByID(String id){
		return runes.get(id);
	}
	/**
	 * Returns all registered rune IDs
	 * @return a set of the registered rune IDs (modid:runeID)
	 */
	public static Set<String> getRuneIDs(){
		return runes.keySet();
	}
	/**
	 * Returns size and cost stats for the rune, including a list of the dusts required to build a rune. /!\ Do NOT edit this list or any of the ItemStacks in it, things WILL break
	 * @param id the identifier of the rune for which to get the cost
	 * @return the dusts required to build the rune identified by id, as well as its size and centering
	 */
	public static RunesUtil.RuneStats getRuneStats(String id){
		return  duststats.get(id);
	}
	/** Given a dust, returns the block that was created in registerDust
	 * 
	 * @param dust the dust to find the block for
	 * @return the block created from this dust, or {@code null} if it has a custom block 
	 */
	public static IDustStorageBlock getBlock(IDust dust){
		return blocks.get(dust);
	}
	public static Collection<IDustStorageBlock> getAllBlocks(){
		return blocks.values();
	}
	/**
	 * Registers a valid dust into the RunesOfWizardry system.  MUST EXTEND IDUST!!
	 * <br/>Note: also registers it as an Item in the GameRegistry, sets up its unlocalized name and creative tab.
	 */ 
	public static void registerDust(final IDust dustclass) {
		//add it to our list of dusts
		dusts.add(dustclass);
		if(dustclass instanceof DustPlaceholder){
			placeholders.add((DustPlaceholder) dustclass);
		}
		String modID = Utils.getCurrentModID();
		dustclass.setUnlocalizedName(modID+"_"+dustclass.getName());
		dustclass.setCreativeTab(dustclass.creativeTab());
		//list of subItems
		int[] metavalues = dustclass.getMetaValues();
		//if we have meta-based subdusts, mark it as such
		if(metavalues.length>1){
			dustclass.setHasSubtypes(true);
		}
		//register the dust item with the appropriate modid
		GameRegistry.registerItem(dustclass, modID+":"+dustclass.getName());

		//create the block form of the dust
		IDustStorageBlock dustBlock;
		if(!dustclass.hasCustomBlock()){
			dustBlock = new ADustStorageBlock(Material.sand,modID) {

				@Override
				public IDust getIDust() {
					return dustclass;
				}

			};
		}else {
			dustBlock = dustclass.getCustomBlock();
		}
		if(dustBlock!=null){
			blocks.put(dustclass, dustBlock);
			//Crafting the blocks
			for(int i:metavalues){
				ItemStack dust = new ItemStack(dustclass,1,i);
				GameRegistry.addShapedRecipe(new ItemStack(dustBlock.getInstance(), 1, i), 
						new Object[]{"XXX","XXX","XXX",'X',dust});
				GameRegistry.addShapelessRecipe(new ItemStack(dustclass,9,i), new ItemStack(dustBlock.getInstance(), 1, i));
				
			}
		}
		
	}
	/** Validates and registers a rune in the RunesOfWizardry system.
	 * 
	 * @param rune the rune to register
	 * @throws InvalidRuneException if the given rune is invalid
	 */
	public static void registerRune(final IRune rune){
		RunesUtil.RuneStats stats = RunesUtil.validateRune(rune);
		String modID = Utils.getCurrentModID();
		String name=modID+":"+rune.createRune(new ItemStack[][]{},EnumFacing.NORTH, null, null).getRuneID();
		//maybe do crash report (or skip registration)
		if(runes.containsKey(name))throw new IllegalArgumentException("A rune with the name: "+name+" Already exists!");
		runes.put(name,rune);
		duststats.put(name, stats);
	}
	/**
	 * 
	 * @param materials the recipe to infuse the dust 
	 * @param blockIn the block/item to be infused (usually a block of inert dust)
	 * @param blockOut the result (usually a block of your dust)
	 */
	public static void registerBlockInfusion(ItemStack[] materials, ItemStack blockIn, ItemStack blockOut){
		//FUTURE temporary until we figure out what we want
		ItemStack[] recipe = new ItemStack[materials.length+1];
		for(int i=0;i<materials.length;i++){
			recipe[i]=materials[i];
		}
		recipe[materials.length]=blockIn;
		GameRegistry.addShapelessRecipe(blockOut, (Object[])recipe);
	}

	/** Returns the dust class from an ItemStack
	 * @return the IDust in the ItemStack
	 * @throws IllegalArgumentException if the ItemStack is not a dust
	 */
	public static IDust getDustFromItemStack(ItemStack stack){
		Item item = stack.getItem();
		if(item instanceof IDust){
			return (IDust)item;
		}else{
			throw new IllegalArgumentException("The Item is not a dust");
		}
	}

	/** Find if a given Block is placed Dust
	 * @param blockState the block to check
	 * @return {@code true} if the argument is placed dust
	 **/
	public static boolean isDust(IBlockState blockState){
		if(blockState.getBlock() == WizardryRegistry.dust_placed){
			return true;
		}
		else{
			return false;
		}
	}

}


