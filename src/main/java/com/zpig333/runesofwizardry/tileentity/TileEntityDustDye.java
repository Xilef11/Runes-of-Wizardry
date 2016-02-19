package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.item.dust.DustDyed;


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
	public ItemStack removeStackFromSlot(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	/**
	 * Returns the name of the inventory
	 */
	public String getName()
	{
		return References.modid+".DustDye";
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(pos) == this &&
				player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
	}
	//No clue what the next 2 methods 2 (NOT run when inv. opened)
	@Override
	public void openInventory(EntityPlayer p) {

	}

	@Override
	public void closeInventory(EntityPlayer p) {
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
	public boolean hasCustomName() {
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
	public IChatComponent getDisplayName() {
		//line from InventoryBasic
		//might want to always return the translated version? (dosen't seem much used, leave as is.)
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}
	/*[refactor] the following methods are used to change the fields of the TileEntity.
	 * we are already doing this in other ways, but we might want to switch to them eventually
	 */
	@Override
	public int getField(int id) {
		//not using this
		return 0;
	}
	@Override
	public void setField(int id, int value) {
		//not using this?
	}
	@Override
	public int getFieldCount() {
		//Not using this?
		return 0;
	}
	@Override
	public void clear() {
		// let's just do the same thing as inventoryBasic
		for(ItemStack i:contents){
			i=null;
		}
	}

}
