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
public abstract class RuneEntity{
	/**
	 * The pattern that was actually placed in the world to create this Rune. This contains real dusts instead of the special constants
	 */
	public final ItemStack[][] placedPattern;
	public final EnumFacing face;
	public final TileEntityDustActive entity;
	public final Set<BlockPos> dustPositions;
	/**
	 * This constructor is called during normal activation of a rune, and may be called with null values
	 * @param actualPattern the pattern of ItemStacks that was found
	 * @param dusts the positions of all placed dust blocks in this rune
	 * @param entity the TileEntity that hosts this rune
	 */
	public RuneEntity(ItemStack[][] actualPattern,EnumFacing facing, Set<BlockPos> dusts, TileEntityDustActive entity){
		this.placedPattern=actualPattern;
		this.entity = entity;
		this.dustPositions=dusts;
		this.face=facing;
		if(entity!=null)entity.setRune(this);
	}
	/**
	 * Returns a unique identifier for this type of rune. it should be prefixed with your modid.
	 * @return the unique ID for this rune
	 */
	public abstract String getRuneID();//this should be static, but Java can't do that 
	
	/**
	 * This will be called once when the rune is activated.
	 * @param player The player that activated the rune
	 * @param sacrifice the itemStacks used to activate this rune. they have already been taken, but you can return them if activation requires specific conditions (i.e nighttime)
	 */
	public abstract void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice);
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
	public void update() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void readFromNBT(NBTTagCompound compound) {
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	public void writeToNBT(NBTTagCompound compound) {
	}
	/* (non-Javadoc)
	 * @see net.minecraft.tileentity.TileEntity#getPos()
	 */
	public BlockPos getPos() {
		return entity.getPos();
	}
	
}
