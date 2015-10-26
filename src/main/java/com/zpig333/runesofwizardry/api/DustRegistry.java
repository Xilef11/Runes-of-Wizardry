package com.zpig333.runesofwizardry.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.core.WizardryRegistry;

import cpw.mods.fml.common.registry.GameRegistry;

/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

	/** List of all registered dusts **/
	private static List<IDust> dusts = new LinkedList<IDust>();
	/** map of all the autogenerated blocks**/
	private static Map<IDust,IDustStorageBlock> blocks = new HashMap<IDust,IDustStorageBlock>();
	/** Map of all the infusion recipes **/
	//TODO use a RecipeHandler for this
	private static Map<ItemStack[], ItemStack> recipes = new HashMap<ItemStack[], ItemStack>();

	/** returns a list of all the registered dusts
	 * 
	 * @return a LinkedList of all the dusts, in the order they were registered
	 */
	public static List<IDust> getAllDusts(){
		return new LinkedList<IDust>(dusts);
	}
	/** Given a dust, returns the block that was created in registerDust
	 * 
	 * @param dust the dust to find the block for
	 * @return the block created from this dust, or {@code null} if it has a custom block 
	 */
	public static Block getDefaultBlock(IDust dust){
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

		dustclass.setUnlocalizedName(dustclass.getmodid()+"_"+dustclass.getName());
		dustclass.setCreativeTab(dustclass.creativeTab());

		GameRegistry.registerItem(dustclass, dustclass.getName());

		//list of subItems
		int[] metavalues = dustclass.getMetaValues();
		//create the block form of the dust
		if(!dustclass.hasCustomBlock()){
			IDustStorageBlock dustBlock = new IDustStorageBlock(Material.sand) {

				@Override
				public IDust getIDust() {
					return dustclass;
				}

			};
			blocks.put(dustclass, dustBlock);
			//Crafting the blocks
			for(int i:metavalues){
				ItemStack dust = new ItemStack(dustclass,1,i);
				GameRegistry.addShapedRecipe(new ItemStack(dustBlock, 1, i), 
						new Object[]{"XXX","XXX","XXX",'X',dust});
				GameRegistry.addShapelessRecipe(new ItemStack(dustclass,9,i), new ItemStack(dustBlock, 1, i));
				
			}
		}
	}
	/**
	 * 
	 * @param materials the recipe to infuse the dust 
	 * @param blockIn the block/item to be infused (usually a block of inert dust)
	 * @param blockOut the result (usually a block of your dust)
	 */
	public static void registerBlockInfusion(ItemStack[] materials, ItemStack blockIn, ItemStack blockOut){
		//XXX temporary until we figure out what we want
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

	/** Returns the dust associated with an infusion recipe
	 * @param recipe the infusion recipe to look up
	 * @return the (dust) ItemStack associated with this recipe
	 */
	public static ItemStack getDustFromRecipe(ItemStack[] recipe){
		return recipes.get(recipe);
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


