package com.zpig333.runesofwizardry.item.dust;

import java.util.List;

import com.zpig333.runesofwizardry.api.IDust;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DustDyed extends IDust{

	//add tooltip
	@Override
	public void addInformation(ItemStack stack, World world, List<String> data, ITooltipFlag flag){
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
	public boolean isMagicDust(ItemStack stack){
		return false;
	}
}
