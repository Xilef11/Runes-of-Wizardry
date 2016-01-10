/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-09
 */
package com.zpig333.runesofwizardry.tileentity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryLogger;

/** This TileEntity replaces a TileEntityDustPlaced when a rune is formed.
 * @author Xilef11
 *
 */
public class TileEntityDustActive extends TileEntityDustPlaced implements IUpdatePlayerListBox {

	public TileEntityDustActive(TileEntityDustPlaced oldTE){
		//copy all fields from old TE
		this.contents=oldTE.contents;
		this.rune=oldTE.rune;
		//get the rendering
		this.updateCenterColors();
		this.updateExternalConnectors();
		this.updateInternalConnectors();
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.server.gui.IUpdatePlayerListBox#update()
	 */
	@Override
	public void update() {
		rune.update();
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#readFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		String runeID = tagCompound.getString("runeID");
		//read the blockpos
		Set<BlockPos> posSet = new LinkedHashSet();
		NBTTagList positions = tagCompound.getTagList("dustPositions", 11);//11 is int array
		for(int i=0;i<positions.tagCount();i++){
			int[] p = positions.getIntArrayAt(i);
			posSet.add(new BlockPos(p[0], p[1], p[2]));
		}
		//read the Rune's ItemStacks
		NBTTagList tagList = tagCompound.getTagList("Pattern",10);
		ArrayList<ArrayList<ItemStack>> pattern = new ArrayList<ArrayList<ItemStack>>();
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			int r = tag.getInteger("Row");
			int c = tag.getInteger("Col");
			if(pattern.get(r)==null)pattern.set(r, new ArrayList<ItemStack>());
			pattern.get(r).set(c, ItemStack.loadItemStackFromNBT(tag));
		}
		pattern.trimToSize();
		ItemStack[][] stacks = new ItemStack[pattern.size()][pattern.get(0).size()];
		for(int i=0;i<stacks.length;i++){
			pattern.get(i).trimToSize();
			stacks[i]=pattern.get(i).toArray(new ItemStack[]{});
		}
		
		//re-create the rune
		IRune rune = DustRegistry.getRuneByID(runeID);
		Class<? extends RuneEntity> clazz = rune.getRune();
		RuneEntity entity = null;
		try {
			clazz.getConstructor(ItemStack[][].class, Set.class,TileEntityDustActive.class).newInstance(stacks,posSet,this);
		} catch (Exception e) {
			WizardryLogger.logException(Level.ERROR, e, "Couldn't create RuneEntity of type: "+clazz.getSimpleName()+" while reading from NBT");
			//crash report or something
		}
		this.rune=entity;
		for(BlockPos p : posSet){
			TileEntity te = worldObj.getTileEntity(p);
			if(te instanceof TileEntityDustPlaced){
				((TileEntityDustPlaced)te).setRune(entity);
			}else{
				WizardryLogger.logError("TileEntity at pos: "+p+" wasn't placed dust! (TEDustActive#readFromNBT)");
			}
		}
		this.rune.readFromNBT(tagCompound);
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced#writeToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		rune.writeToNBT(tagCompound);
		tagCompound.setString("runeID", rune.getRuneID());
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
				if (stack != null) {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setInteger("Row", r);
					tag.setInteger("Col", c);
					stack.writeToNBT(tag);
					itemList.appendTag(tag);
				}
			}
		}
		tagCompound.setTag("Pattern", itemList);
	}
	//
	
}
