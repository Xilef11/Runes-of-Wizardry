package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class ItemPestle extends Item {
	
	private final String name="pestle";

    public ItemPestle(){
        super();
        GameRegistry.registerItem(this, name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setMaxDamage(63);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(References.modid+"_"+name);
        
    }
    
    public String getName(){
    	return name;
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack){
        return true;
    }
    /* FIXME not sure what to do with this for 1.8
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack){
        return false;
    }
    */

    @Override
    public ItemStack getContainerItem(ItemStack itemStack){
        itemStack.setItemDamage(itemStack.getItemDamage() + 1);
        itemStack.stackSize = 1;
        return itemStack;
    }

    /* NOT used in 1.8
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ireg){
        this.itemIcon = ireg.registerIcon(References.texture_path + "pestle");
    }
    */

}
