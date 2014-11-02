package com.zpig333.runesofwizardry.api;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
/** Dust API registry.  All dust registry methods are found here. */
public class DustRegistry {

    /** List of all registered dusts **/
    public static List<IDust> dusts = new ArrayList<IDust>();

    /**
     * Registers a valid dust into the RunesOfWizardry system.  MUST EXTEND IDUST!!
     * <br/>Note: also registers it as an Item in the GameRegistry.
     */ 
    public static void registerDust(IDust dustclass){
        //get the last avaliable ID
        int nextId=dusts.size();
        dustclass.setId(nextId);
        dusts.add(nextId, dustclass);
        GameRegistry.registerItem(dustclass, dustclass.getUnlocalizedName());
        //TODO- craftability and whatnot
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
    public static boolean isDust(Block block){
        if(block == WizardryRegistry.dust_placed){
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
        if (value > dusts.size())
            return 0;
        return dusts.get(value).getPrimaryColor();
    }
    //XXX might not be needed
    public static int getSecondaryColor(int value) {
        if (value < 0)
            return 0xDB73ED1;
        if (value > dusts.size())
            return 0;
        return dusts.get(value).getSecondaryColor();
    }
    //XXX might not be needed
    public static int getPlacedColor(int value)
    {
        if (value < 0)
            return 0xCE00E0;
        if (value > dusts.size())
            return 0;
        return dusts.get(value).getPlacedColor();
    }
    public static int [] getFloorColorRGB(IDust dust){
        return getFloorColorRGB(dust.getId());
    }
    public static int[] getFloorColorRGB(int value)
    {
        
        if (value < 0)
            return new int[] { 206, 0, 224 }; // 00CE00E0 variable

        if (value > dusts.size())
            return new int[] { 0, 0, 0 };

        int[] rtn = new int[3];
        int col = dusts.get(value).getPlacedColor();
        rtn[0] = (col & 0xFF0000) >> 16;
        rtn[1] = (col & 0xFF00) >> 8;
        rtn[2] = (col & 0xFF);

        return rtn;
    }
}


