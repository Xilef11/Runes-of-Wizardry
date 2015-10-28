/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-10-28
 */
package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.core.References;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;

/** For the purposes of having an appropriate texture in NEI/WAILA
 * 
 * @author Xilef11
 *
 */
public class ItemBlockDustPlaced extends ItemBlock {

	public ItemBlockDustPlaced(Block p_i45328_1_) {
		super(p_i45328_1_);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.minecraft.item.ItemBlock#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	public void registerIcons(IIconRegister register) {
		this.itemIcon=register.registerIcon(References.texture_path+"dust_top");
	}
	
	

}
