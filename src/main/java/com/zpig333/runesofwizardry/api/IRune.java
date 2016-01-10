/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import java.util.Set;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

/** This Interface defines a rune created by placing patterns of arcane dust.<br>
 * This should be a singleton class like Items and Blocks.
 * @author Xilef11
 *
 */
public interface IRune {
	/** Returns the name of this rune
	 * 
	 * @return the (unlocalized) name of this rune
	 */
	public String getName();
	
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
	/** returns the position of the entity for this Rune, 
	 * as an offset from the top-left corner (0,0) in the pattern<br/>
	 * Note that these are NOT using the same axis as Minecraft.
	 * <br/>Returning (0,0,0) will place the entity at the top-left corner of the top-left block 
	 * of dust, on the same level as the ground. The z element will be ignored.
	 * @return a vector where the X element is the horizontal offset from the top-left
	 * corner of the pattern, the y element is the vertical offset from that corner and
	 * the z element is the offset on the axis normal to the pattern
	 */
	public Vec3i getEntityPosition();
	/** Returns the items needed to activate this Rune
	 * 
	 * @return the ItemStacks that must be dropped on the Rune for it to activate
	 */
	public ItemStack[] getSacrifice();
	/**
	 * Returns a new instance of the RuneEntity that is created when this rune is formed and activated.
	 *<br/> Note that the sacrifice Items will be consumed before the TileEntity is created.
	 * @return
	 */
	public RuneEntity createRune(ItemStack[][] actualPattern, Set<BlockPos> dusts, TileEntityDustActive entity);
	
}
