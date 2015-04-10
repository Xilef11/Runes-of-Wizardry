package com.zpig333.runesofwizardry.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;

import com.zpig333.runesofwizardry.item.ItemDyedDust;


public class TileEntityDustDye extends TileEntity implements IInventory{
    //only 1 slot for now, might change if dyes are required as input
    private ItemStack[] contents = new ItemStack[1];
    //the currently selected color
    private String colorString;
    
    public TileEntityDustDye(){
        super();
        colorString="Color";
    }
    public void dye(int color){
        contents[0].getTagCompound().setInteger("color", color);
        setColor(Integer.toHexString(color));
    }
    /**
     * 
     * @return the currently selected Color of this block as a String
     */
    public String getColor(){
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
        contents[slot] = stack;
        /*        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                        stack.stackSize = getInventoryStackLimit();
                }              
        */
    }

    @Override
    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int slot) {
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
        return "RunesWiz.DustDye";
    }


    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(pos) == this &&
                player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
    }

    @Override
    public void openInventory(EntityPlayer p) {
    }

    @Override
    public void closeInventory(EntityPlayer p) {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        
        //only allow dyed dust in the dyer
        return slot==0 ? stack.getItem() instanceof ItemDyedDust : false;
    }
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }
    
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
