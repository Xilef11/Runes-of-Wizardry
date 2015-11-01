package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.item.dust.DustDyed;
import com.zpig333.runesofwizardry.network.guipackets.DustDyeUpdatePacket;


public class TileEntityDustDye extends TileEntity implements IInventory{
	//only 1 slot for now, might change if dyes are required as input
	private ItemStack[] contents = new ItemStack[1];
	//the currently selected color
	private String colorString;

	public TileEntityDustDye(){
		super();
		colorString="Color";
		//colorString=StatCollector.translateToLocal(References.Lang.COLOR);
	}
	public void dye(int color){
		if(contents[0]==null)return;
		//NPE was because tagCompound is null...
		NBTTagCompound compound = contents[0].getTagCompound();
		if(compound == null){
			compound = new NBTTagCompound();
			contents[0].setTagCompound(compound);
		}
		compound.setInteger("color", color);
		setColor(Integer.toHexString(color));
	}
	/**
	 * 
	 * @return the currently selected Color of this block as a String
	 */
	public String getColorString(){
		return colorString;
	}
	/**
	 * 
	 * @param color the selected color
	 */
	public void setColor(String color){
		colorString=color;
	}
	@Override
	public int getSizeInventory() {
		return contents.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i1) {
		return contents[i1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slot, int number)
	{
		if (this.contents[slot] != null)
		{
			ItemStack itemstack;

			if (this.contents[slot].stackSize <= number)
			{
				itemstack = this.contents[slot];
				this.contents[slot] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.contents[slot].splitStack(number);

				if (this.contents[slot].stackSize == 0)
				{
					this.contents[slot] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		contents[slot]=stack;
		//if stacksize > InventoryStackLimit && Shift-clicking the stack in, the "extra" items are deleted...
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}              
	}

	@Override
	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 * <br/>NOT to be used for the Dust Dye container
	 */
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord,yCoord,zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		//only allow dyed dust in the dyer
		return slot==0 ? stack.getItem() instanceof DustDyed : false;
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isCustomInventoryName() {
		return true;
	}
	//might want to change the tag names to be variables for "safety" (nah, "safety" is overrated)
	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory",10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < contents.length) {
				contents[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		this.colorString=tagCompound.getString("Color");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < contents.length; i++) {
			ItemStack stack = contents[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setString("Color", colorString);

	}

	@Override
	public String getInventoryName() {
		return References.modid+".DustDye";
	}

	@Override
	public void openChest() {
		// Not using this?

	}
	@Override
	public void closeChest() {
		// not using this?

	}

}
