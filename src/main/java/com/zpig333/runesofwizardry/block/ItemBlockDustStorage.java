/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-10-30
 */
package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/** required for having different names for each meta.
 * @author Xilef11
 *
 */
public class ItemBlockDustStorage extends ItemBlock {

	public ItemBlockDustStorage(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemBlock#getUnlocalizedName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName()+"."+BlockDustStorage.dustTypes.values()[stack.getMetadata()];
	}
	
	

}
