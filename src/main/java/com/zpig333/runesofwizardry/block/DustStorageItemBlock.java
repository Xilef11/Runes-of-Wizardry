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

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getMetadata(int)
	 * 
	 *This is called with the meta value of the Block's ItemStack when placing the block.
	 *The result is then passed to Block#getStateFromMeta
	 *The super implementation returns 0 FSR
	 */
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
}
