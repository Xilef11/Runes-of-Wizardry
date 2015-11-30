package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import amerifrance.guideapi.api.registry.GuideRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.guide.GuideWizardry;


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
		if(!RunesOfWizardry.guideApiLoaded){
			tooltip.add("Install Guide-API to activate");
		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#onItemRightClick(net.minecraft.item.ItemStack, net.minecraft.world.World, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn,EntityPlayer playerIn) {
		if(RunesOfWizardry.guideApiLoaded){
			//trade this for the Guide-API version, and open it.
			ItemStack guideBook = GuideRegistry.getItemStackForBook(GuideWizardry.guideBook);
			return guideBook.getItem().onItemRightClick(guideBook, worldIn, playerIn);
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	
	

}
