package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.References;


public class ItemRunicStaff extends WizardryItem {
	private final String name = "runic_staff";
	public ItemRunicStaff(){
		super();
		this.setMaxDurability(50);
		this.setFull3D();
	}
	@Override
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#addInformation(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, java.util.List, boolean)
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,List tooltip, boolean advanced) {
		tooltip.add("Unimplemented");
	}
	//the following methods are for rendering and may be temporary
	//XXX still not happy with 3rd person attack animation
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.block;
	}
	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}
	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	 @Override
	    public void registerIcons(IIconRegister ireg){
	        this.itemIcon = ireg.registerIcon(References.texture_path + "wizards_staff");
	    }
}
