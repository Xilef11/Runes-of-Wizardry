/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-02-17
 */
package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;

/**
 * @author Xilef11
 *
 */
public class DustStorageItemBlock extends ItemBlock {
	public DustStorageItemBlock(Block block) {
		super(block);
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if(block instanceof ADustStorageBlock){
			IDust dust = ((ADustStorageBlock)block).getIDust();
			return dust.getColorFromItemStack(new ItemStack(dust, 1,stack.getMetadata()),renderPass);
		}
		return super.getColorFromItemStack(stack, renderPass);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemBlock#getUnlocalizedName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String base = super.getUnlocalizedName(stack);
		if(this.getHasSubtypes()){
			base += "."+stack.getMetadata();
		}
		return base;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getHasSubtypes()
	 */
	@Override
	public boolean getHasSubtypes() {
		if(block instanceof IDustStorageBlock){
			return ((IDustStorageBlock)block).getIDust().getHasSubtypes();
		}
		return super.getHasSubtypes();
	}
	

}
