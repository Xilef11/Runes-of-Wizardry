package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.core.References;


public class ItemRunicDictionary extends WizardryItem {
	private final String name="runic_dictionary";

	public ItemRunicDictionary(){
		super();
		this.setMaxStackSize(1);
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
	
	 @Override
	    public void registerIcons(IIconRegister ireg){
	        this.itemIcon = ireg.registerIcon(References.texture_path + "wizardry_dictionary");
	    }
}
