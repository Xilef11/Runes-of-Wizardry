package com.zpig333.runesofwizardry.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.core.WizardryRegistry;

/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

    /** List of all registered dusts **/
    private static List<IDust> dusts = new LinkedList<IDust>();
    
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
        List<ItemStack> subDusts = new ArrayList<ItemStack>(15);
        //get the subDusts. hopefully, tabAllSearch is the right one
        dustclass.getSubItems(dustclass, CreativeTabs.tabAllSearch, subDusts);
        
        //create the block form of the dust
        if(!dustclass.hasCustomBlock()){
        	Block dustBlock = new IDustStorageBlock(Material.sand) {

        		@Override
        		public IDust getIDust() {
        			return dustclass;
        		}
        		
        	};
        	
        	//Crafting
        	//XXX hopefully this is enough for metadata
        	for(ItemStack i:subDusts){
        		GameRegistry.addShapedRecipe(new ItemStack(dustBlock, 1, i.getItemDamage()), 
        				new Object[]{"XXX","XXX","XXX",'X',i});
        		GameRegistry.addShapelessRecipe(i, new ItemStack(dustBlock, 1, i.getItemDamage()));
        	}
        	
        }
        
        //register the recipes for this dust
        //TODO use a custom RecipeHandler
        for (ItemStack i : subDusts) {
            ItemStack[] items = dustclass.getInfusionItems(i);
            if (items != null) {
                recipes.put(items, i);
            }
        }
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


