/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/** This class is the superclass for all TileEntities that create the effects of runes.
 * @author Xilef11
 *
 */
public abstract class TERune extends TileEntity{
	/**
	 * The pattern that was actually placed in the world to create this Rune. This contains real dusts instead of the special constants
	 */
	public final ItemStack[][] placedPattern;
	
	public TERune(ItemStack[][] actualPattern){
		this.placedPattern=actualPattern;
	}
	/**
	 * Returns the Rune associated with this TileEntity
	 * @return the IRune associated with this TileEntity
	 */
	public abstract IRune getRune();//this should be static, but Java can't do that 
	
	/**
	 * This will be called once when the rune is activated.
	 * @param player The player that activated the rune
	 */
	public abstract void onRuneActivatedbyPlayer(EntityPlayer player);
	
	//TODO
}
