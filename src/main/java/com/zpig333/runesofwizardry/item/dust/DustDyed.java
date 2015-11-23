package com.zpig333.runesofwizardry.item.dust;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.guide.GuideWizardry;

public class DustDyed extends IDust{

	//add tooltip
	@SuppressWarnings({ "rawtypes", "unchecked" })//data is a list of Strings, but Item does not use generics
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List data, boolean bool){
		//if the stack has no tag compound, create one and set the color to white
		if(stack.getTagCompound()==null){
			NBTTagCompound compound = new NBTTagCompound();
			compound.setInteger("color", 0xffffff);
			stack.setTagCompound(compound);
		}
		String color = String.format("#%06X", stack.getTagCompound().getInteger("color"));
		data.add(color);

	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getDustName()
	 */
	@Override
	public String getDustName() {
		return "dyed";
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getPrimaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getPrimaryColor(ItemStack stack) {
		NBTTagCompound tag=stack.getTagCompound();
		if(tag==null){
			return 0xffffff;
		}
		return tag.getInteger("color");
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getSecondaryColor(net.minecraft.item.ItemStack)
	 */
	@Override
	public int getSecondaryColor(ItemStack stack) {
		//Only 1 color
		return getPrimaryColor(stack);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getInfusionItems(net.minecraft.item.ItemStack)
	 */
	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//This dust is crafted via other mechanics
		return null;
	}
	@Override
	public boolean hasCustomBlock(){
		return true;//no block at all, actually...
	}

	@Override
	public String getDescription(int meta) {
		return GuideWizardry.DESC+".dust_dyed";
	}
	@Override
	public boolean appearsInGuideBook(){
		return false;
	}
}
