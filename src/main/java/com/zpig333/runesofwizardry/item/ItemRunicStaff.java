package com.zpig333.runesofwizardry.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemRunicStaff extends WizardryItem {
	private final String name = "runic_staff";
    public ItemRunicStaff(){
    	super();
        this.setMaxDamage(50);
        this.setFull3D();
    }
	public String getName(){
    	return name;
    }
	//the following methods are for rendering and may be temporary
	//XXX still not happy with 3rd person attack animation
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

}
