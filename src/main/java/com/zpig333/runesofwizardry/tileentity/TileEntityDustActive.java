/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-09
 */
package com.zpig333.runesofwizardry.tileentity;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/** This TileEntity replaces a TileEntityDustPlaced when a rune is formed.
 * @author Xilef11
 *
 */
public class TileEntityDustActive extends TileEntityDustPlaced implements ITickable {
	private long ticksExisted=0;
	public TileEntityDustActive() {
		super();
	}
	/**
	 * Returns the number of ticks since this was created
	 * @return the number of times the update() method was called on this.
	 */
	public long ticksExisted(){
		return ticksExisted;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	@Override
	public void update() {
		if(!initialised)init();
		ticksExisted++;
		if(rune!=null)rune.update();
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		ticksExisted = tagCompound.getLong("ticksExisted");
		String runeID = tagCompound.getString("runeID");
		//read the blockpos
		Set<BlockPos> posSet = new LinkedHashSet<BlockPos>();
		NBTTagList positions = tagCompound.getTagList("dustPositions", 11);//11 is int array
		for(int i=0;i<positions.tagCount();i++){
			int[] p = positions.getIntArrayAt(i);
			posSet.add(new BlockPos(p[0], p[1], p[2]));
		}
		//read the Rune's ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Pattern",10);
		List<ArrayElement> items = new LinkedList<ArrayElement>();
		int maxRow=0,maxCol=0;
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			int r = tag.getInteger("Row");
			int c = tag.getInteger("Col");
			ItemStack s = ItemStack.loadItemStackFromNBT(tag);
			items.add(new ArrayElement(r, c, s));
			if(r>maxRow)maxRow=r;
			if(c>maxCol)maxCol=c;
		}
		//make sure its the right size
		int modR = maxRow%TileEntityDustPlaced.ROWS;
		if(modR!=0)maxRow+=(TileEntityDustPlaced.ROWS-modR);
		int modC = maxCol%TileEntityDustPlaced.COLS;
		if(modC!=0)maxCol+=(TileEntityDustPlaced.COLS-modC);
		ItemStack[][] stacks = new ItemStack[maxRow+1][maxCol+1];
		
		for(ArrayElement a:items){
			stacks[a.row][a.col]=a.stack;
		}
		EnumFacing facing = EnumFacing.byName(tagCompound.getString("Facing"));
		boolean renders = tagCompound.getBoolean("renderActive");
		//re-create the rune
		IRune rune = DustRegistry.getRuneByID(runeID);
		if(rune==null){
			WizardryLogger.logError("Active Dust at "+getPos()+" loaded invalid runeID "+runeID+" from NBT");
			this.rune=null;//this should make the rune do nothing.
		}else{
			RuneEntity entity = rune.createRune(stacks,facing, posSet, this);
			entity.renderActive=renders;
			this.rune=entity;
			//init should get called on the first tick to set up the other dust blocks
			initialised=false;
			this.rune.readFromNBT(tagCompound);
		}
	}
	private boolean initialised=false;
	private void init(){
		if(initialised || worldObj==null)return;
		for(BlockPos p : rune.dustPositions){
			TileEntity te = worldObj.getTileEntity(p);
			if(te instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te).setRune(rune);
			}else{
				WizardryLogger.logError("TileEntity at pos: "+p+" wasn't placed dust! (TEDustActive#init)");
			}
		}
		initialised=true;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		if(rune!=null){
			rune.writeToNBT(tagCompound);
		tagCompound.setString("runeID", DustRegistry.getRuneID(rune.creator));
		//write the rune's blockpos set
		NBTTagList positions = new NBTTagList();
		for(BlockPos p: rune.dustPositions){
			NBTTagIntArray current = new NBTTagIntArray(new int[]{p.getX(),p.getY(),p.getZ()});
			positions.appendTag(current);
		}
		tagCompound.setTag("dustPositions",positions);
		//write the rune's ItemStacks
		NBTTagList itemList = new NBTTagList();
		for (int r = 0; r < rune.placedPattern.length; r++) {
			for(int c=0;c<rune.placedPattern[r].length;c++){
				ItemStack stack = rune.placedPattern[r][c];
				NBTTagCompound tag = new NBTTagCompound();
				if (stack != null) {
					stack.writeToNBT(tag);
					tag.setInteger("Row", r);
					tag.setInteger("Col", c);
					itemList.appendTag(tag);
				}
			}
		}
		tagCompound.setTag("Pattern", itemList);
		tagCompound.setTag("Facing", new NBTTagString(rune.face.getName()));
		tagCompound.setBoolean("renderActive", rune.renderActive);
		}
		tagCompound.setLong("ticksExisted", ticksExisted);
	}
	//
	private class ArrayElement{
		private int row;
		private int col;
		private ItemStack stack;
		private ArrayElement(int r, int c, ItemStack s){
			row=r;
			col=c;
			stack=s;
		}
	}
	
}
