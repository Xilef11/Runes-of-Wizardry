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

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

    /** List of all registered dusts **/
    private static List<IDust> dusts = new LinkedList<IDust>();
    
    /** Map of all the infusion recipes **/
    private static Map<ItemStack[], ItemStack> recipes = new HashMap<ItemStack[], ItemStack>();
    /** returns a list of all the registered dusts
     * 
     * @return
     */
    public static List<IDust> getAllDusts(){
    	return new LinkedList<IDust>(dusts);
    }
    /**
     * Registers a valid dust into the RunesOfWizardry system.  MUST EXTEND IDUST!!
     * <br/>Note: also registers it as an Item in the GameRegistry.
     */ 
    public static void registerDust(final IDust dustclass) {
        //get the last avaliable ID
        //int nextId=dusts.size();
        //dustclass.setId(nextId);
        dusts.add(dustclass);
        //FIXME this should not use unlocalized name
        GameRegistry.registerItem(dustclass, "dust_"+dustclass.getDustName());
        //list of subItems
        List<ItemStack> subDusts = new ArrayList<ItemStack>(15);
        //get the subDusts. hopefully, tabAllSearch is the right one
        dustclass.getSubItems(dustclass, CreativeTabs.tabAllSearch, subDusts);
        //create the block form of the dust
        if(!dustclass.usesCustomBlock()){
        	Block dustBlock = new IDustStorageBlock(Material.sand) {
        		//XXX hopefully this will work
        		@Override
        		public IDust getIDust() {
        			return dustclass;
        		}
        		
        	};
        	dustBlock.setHardness(0.5F).setCreativeTab(RunesOfWizardry.wizardry_tab)
        	.setStepSound(Block.soundTypeSand).setHarvestLevel("shovel", 0);
        	dustBlock.setUnlocalizedName(References.modid+"_dust_storage_"+dustclass.getDustName());
        	GameRegistry.registerBlock(dustBlock, References.modid+"_dust_storage_"+dustclass.getDustName());
        	//Crafting
        	//XXX hopefully this is enough for metadata
        	for(ItemStack i:subDusts){
        		GameRegistry.addShapedRecipe(new ItemStack(dustBlock, 1, i.getItemDamage()), 
        				new Object[]{"XXX","XXX","XXX",'X',i});
        		GameRegistry.addShapelessRecipe(i, new ItemStack(dustBlock, 1, i.getItemDamage()));
        	}
        	
        }
        //register the recipes for this dust
        
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
    
    public static boolean isDust(IBlockState blockState){
        if(blockState.getBlock() == WizardryRegistry.dust_placed){
            return true;
        }
        else{
            return false;
        }
    }
    //XXX might not be needed
    public static int getPrimaryColor(int value) {
        if(value < 0)
            return 0x8F25A2;
//        if (value > dusts.size())
            return 0;
//        return dusts.get(value).getPrimaryColor();
    }
    //XXX might not be needed
    public static int getSecondaryColor(int value) {
        if (value < 0)
            return 0xDB73ED1;
//        if (value > dusts.size())
            return 0;
//        return dusts.get(value).getSecondaryColor();
    }
    //XXX might not be needed
    public static int getPlacedColor(int value)
    {
        if (value < 0)
            return 0xCE00E0;
//        if (value > dusts.size())
            return 0;
//        return dusts.get(value).getPlacedColor();
    }
    public static int [] getFloorColorRGB(IDust dust){
        return getFloorColorRGB(dust.getId());
    }
    public static int[] getFloorColorRGB(int value)
    {
        
        if (value < 0)
            return new int[] { 206, 0, 224 }; // 00CE00E0 variable

//        if (value > dusts.size())
            return new int[] { 0, 0, 0 };

//        int[] rtn = new int[3];
//        int col = dusts.get(value).getPlacedColor();
//        rtn[0] = (col & 0xFF0000) >> 16;
//        rtn[1] = (col & 0xFF00) >> 8;
//        rtn[2] = (col & 0xFF);

//        return rtn;
    }

}


