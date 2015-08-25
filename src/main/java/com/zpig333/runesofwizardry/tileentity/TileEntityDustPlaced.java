/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-18
 */
package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;

/**The TileEntity that holds placed dust
 * @author Xilef11
 *
 */
public class TileEntityDustPlaced extends TileEntity implements IInventory{
	public static final int ROWS=4, COLS=4;
	
	//the dusts placed in this block
	private ItemStack[][] contents = new ItemStack[ROWS][COLS];
	/* return the coordinates of a slot based on its id
	 * NORTH (Z-)
	 * [0][1][2][3]
	 * [4][5][6][7]		EAST (X+)
	 * [8][9][10][11]
	 * [12][13][14][15]
	 */
	public static int[] getPositionFromSlotID(int id){
		int row = id / ROWS;
		int col = id % COLS;
		return new int[]{row,col};
	}
	//the other way around
	public static int getSlotIDfromPosition(int row, int col){
		return row * ROWS + col;
	}
	
	public TileEntityDustPlaced() {
		super();
	}
	/**returns the color of all center points**/
	public int[][] getCenterColors(){
		int[][]result = new int[ROWS][COLS];
		for(int i=0;i<result.length;i++){
			for(int j=0;j<result[i].length;j++){
				if(contents[i][j]!=null){
					result[i][j]=DustRegistry.getDustFromItemStack(contents[i][j]).getPlacedColor(contents[i][j]);
				}else{
					result[i][j]=-1;
				}
			}
		}
		return result;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		return 32*32;
	};
	@SideOnly(Side.CLIENT)
	@Override
	public net.minecraft.util.AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	};
	
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
		return ROWS*COLS;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		int[] coords=getPositionFromSlotID(index);
		return contents[coords[0]][coords[1]];
	}
	/** returns true if there are no more itemStacks in**/
	public boolean isEmpty(){
		for(int i=0;i<contents.length;i++){
			for(int j=0;j<contents[i].length;j++){
				if(contents[i][j]!=null)return false;
			}
		}
		return true;
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
	@Override
	public void readFromNBT(NBTTagCompound tagCompound){
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory()) {
				int[] coords = getPositionFromSlotID(slot);
				contents[coords[0]][coords[1]] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);

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
}
