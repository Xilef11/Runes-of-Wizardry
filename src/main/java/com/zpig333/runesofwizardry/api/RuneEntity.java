/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-08
 */
package com.zpig333.runesofwizardry.api;

import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

/** This class is the superclass for all "Entities" that create the effects of runes.
 * @author Xilef11
 *
 */
public abstract class RuneEntity{
	/**
	 * The pattern that was actually placed in the world to create this Rune. This contains real dusts instead of the special constants, but is rotated so the "facing" side of this rune is on top
	 */
	public final ItemStack[][] placedPattern;
	/** The direction the "top" of the pattern is facing**/
	public final EnumFacing face;
	/** the TileEntity that backs this rune**/
	public final TileEntityDustActive entity;
	/** The positions of all placed dust blocks in this rune**/
	public final Set<BlockPos> dustPositions;
	/** the IRune that created this RuneEntity**/
	public final IRune creator;
	/**Should this rune have effects when active?**/
	public boolean renderActive=true;
	/**
	 * This constructor is called during normal activation of a rune, and may be called with null values
	 * @param actualPattern the pattern of ItemStacks that was found
	 * @param dusts the positions of all placed dust blocks in this rune
	 * @param entity the TileEntity that hosts this rune
	 */
	public RuneEntity(ItemStack[][] actualPattern,EnumFacing facing, Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator){
		this.placedPattern=actualPattern;
		this.entity = entity;
		this.dustPositions=dusts;
		this.face=facing;
		this.creator=creator;
		if(entity!=null)entity.setRune(this);
	}
	/**
	 * Returns a mod-unique identifier for this type of rune. it will be prefixed with your modid.
	 * @return the unique ID for this rune
	 * @deprecated use DustRegistry.getRuneID(RuneEntity.creator) instead
	 */
	//Note: this is here instead of in IRune to be able to link the rune entity (in placed dust TE) to the IRune class
	@Deprecated
	public final String getRuneID(){
		return DustRegistry.getRuneID(creator);
	}
	
	/**
	 * This will be called once when the rune is activated.
	 * @param player The player that activated the rune
	 * @param sacrifice the itemStacks used to activate this rune. they have already been taken, but you can return them if activation requires specific conditions (i.e nighttime)
	 * @param negated was the sacrifice negator used?
	 */
	public abstract void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice,boolean negated);
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
	/**
	 * This is called from Block#onEntityCollidedWithBlock if the block is part of this rune.
	 * @return true to prevent normal collision handling (i.e items sticking) (true by default)
	 */
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn){
		return true;
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
		//deactivate rune
		RunesUtil.killAllDustsInRune(this);
		RunesUtil.deactivateRune(this);
	}
	
	//Normal TE methods
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	public abstract void update();
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
