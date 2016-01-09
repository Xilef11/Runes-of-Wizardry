/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/** This class is the superclass for all TileEntities that create the effects of runes.
 * TODO do not extend /implement anything, just provide the interface + defaults
 * @author Xilef11
 *
 */
public abstract class RuneEntity extends TileEntity implements IUpdatePlayerListBox{
	/**
	 * The pattern that was actually placed in the world to create this Rune. This contains real dusts instead of the special constants
	 */
	public final ItemStack[][] placedPattern;
	public final TileEntityDustActive entity;
	private final Set<BlockPos> dustPositions;
	
	protected RuneEntity(ItemStack[][] actualPattern, Set<BlockPos> dusts, TileEntityDustActive entity){
		this.placedPattern=actualPattern;
		this.entity = entity;
		this.dustPositions=dusts;
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
	/** This is called when right-clicking on a block of placed dust that is part of this rune.
	 * If this returns true, the normal right-click handling will not happen.
	 * @return true to prevent normal right-click handling (false by default)
	 */
	public boolean handleRightClick(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side,	float hitX, float hitY, float hitZ){
		return false;
	}
	/** This is called when left-clicking on a block of placed dust that is part of this rune.
	 * If this returns true, the normal left-click handling will not happen.
	 * @param hit position at which the block was hit (same as hitX, hitY, hitZ)
	 * @return true to prevent normal left-click handling (false by default)
	 */
	public boolean handleLeftClick(World worldIn, BlockPos pos,	EntityPlayer playerIn, Vec3 hit){
		return false;
	}
	/** Called when the pattern changes after this rune is formed
	 * 
	 * @param player the player that changed the pattern
	 */
	public void onPatternBrokenByPlayer(EntityPlayer player){
		this.onPatternBroken();
	}
	/** Called when the pattern changes after this rune is formed **/
	public void onPatternBroken(){
		//TODO change all dusts in pattern to "dead dust" (in RunesUtils)
	}
	//TODO
	
	//Normal TE methods
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.readFromNBT(compound);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		super.writeToNBT(compound);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getPos()
	 */
	@Override
	public BlockPos getPos() {
		return entity.getPos();
	}
	
}
