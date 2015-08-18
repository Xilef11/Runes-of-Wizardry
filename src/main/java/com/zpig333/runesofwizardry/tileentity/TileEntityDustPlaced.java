/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-18
 */
package com.zpig333.runesofwizardry.tileentity;

import java.util.HashMap;
import java.util.Map;

import scala.collection.mutable.FlatHashTable.Contents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;

/**The TileEntity that holds placed dust
 * @author Xilef11
 *
 */
public class TileEntityDustPlaced extends TileEntity implements IInventory{
 
	
	//the dusts placed in this block
	private ItemStack[][] contents = new ItemStack[4][4];
	/* return the coordinates of a slot based on its id
	 * NORTH
	 * [0][1][2][3]
	 * [4][5][6][7]
	 * [8][9][10][11]
	 * [12][13][14][15]
	 */
	private static int[] getPositionFromSlotID(int id){
		int row = id / 4;
		int col = id % 4;
		return new int[]{row,col};
	}
	//the other way around
	private static int getSlotIDfromPosition(int row, int col){
		return row * 4 + col;
	}
	
	public TileEntityDustPlaced() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getName() {
		return References.modid+".DustPlaced";
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public IChatComponent getDisplayName() {
		//see TileEntityDustDye
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		int[] coords=getPositionFromSlotID(index);
		return contents[coords[0]][coords[1]];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		int[] co=getPositionFromSlotID(index);
		 if (this.contents[co[0]][co[1]] != null)
	        {
	            ItemStack itemstack;

	            if (this.contents[co[0]][co[1]].stackSize <= count)
	            {
	                itemstack = this.contents[co[0]][co[1]];
	                this.contents[co[0]][co[1]] = null;
	                return itemstack;
	            }
	            else
	            {
	                itemstack = this.contents[co[0]][co[1]].splitStack(count);

	                if (this.contents[co[0]][co[1]].stackSize == 0)
	                {
	                    this.contents[co[0]][co[1]] = null;
	                }

	                return itemstack;
	            }
	        }
	        else
	        {
	            return null;
	        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = getStackInSlot(index);
        if (stack != null) {
                setInventorySlotContents(index, null);
        }
        return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		int[] co=getPositionFromSlotID(index);
		contents[co[0]][co[1]]=stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
        	stack.stackSize = getInventoryStackLimit();
        } 
	}

	@Override
	public int getInventoryStackLimit() {
		// only 1 item per slot (does this even do anything?)
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// Handled with onBlockActivated?
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// not using this
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		//not using this
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// only let dust in
		return stack.getItem() instanceof IDust;
	}

	//NOT using the following field methods
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		// let's just do the same thing as inventoryBasic
		for(int i=0;i<contents.length;i++){
			for(@SuppressWarnings("unused") ItemStack j:contents[i]){
				j=null;
			}
		}
	}
	//TODO 
}
