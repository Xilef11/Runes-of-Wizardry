/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-10-30
 */
package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** required for having different names for each meta.
 * @author Xilef11
 *
 */
public class ItemBlockDustStorage extends ItemBlock {

	public ItemBlockDustStorage(Block p_i45328_1_) {
		super(p_i45328_1_);
		setHasSubtypes(true);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemBlock#getUnlocalizedName(net.minecraft.item.ItemStack)
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName()+"."+BlockDustStorage.dustTypes.values()[stack.getMetadata()];
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemBlock#placeBlockAt(net.minecraft.item.ItemStack, net.minecraft.entity.player.EntityPlayer, net.minecraft.world.World, int, int, int, int, float, float, float, int)
	 */
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ, int metadata) {
		//Fixes issue where the wrong block would be placed
		return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY,
				hitZ, stack.getMetadata());
	}



}
