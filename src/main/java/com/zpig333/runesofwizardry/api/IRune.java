/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import net.minecraft.item.ItemStack;

/** This Interface defines a rune created by placing patterns of arcane dust.<br>
 * This should be a singleton class like Items and Blocks.
 * @author Xilef11
 *
 */
public interface IRune {
	/** Returns a n*4 by m*4 array of ItemStacks, where n and m are the number of 
	 *  rows and columns of Minecraft Blocks that make up this Rune.<br/>
	 *  Note that the ItemStacks MUST be stacks of IDust.
	 *  <br/>
	 *  Example: This array would represent the outer border of a block<br/>
	 *  	ItemStack dust = new ItemStack(my_IDust)
	 *  	 {
	 *  		{dust,dust,dust,dust},<br/>
	 *  		{dust,null,null,dust},<br/>
	 *  		{dust,null,null,dust},<br/>
	 *  		{dust,dust,dust,dust}<br/>
	 *  	} 
	 * 
	 * @return an ItemStack(IDust) matrix that represents the pattern to place to create the rune
	 */
	public ItemStack[][] getPattern();
	
	/** Returns the items needed to activate this Rune
	 * 
	 * @return the ItemStacks that must be dropped on the Rune for it to activate
	 */
	public ItemStack[] getSacrifice();
	/**
	 * Returns the TERune TileEntity that is created when this rune is formed and activated.
	 *<br/> Note that the sacrifice Items will be consumed before the TileEntity is created.
	 * @return
	 */
	public Class<TERune> getRune();
	
}
