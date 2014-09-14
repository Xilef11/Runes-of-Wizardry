package com.zpig333.runesofwizardry.tileentity;

import com.zpig333.runesofwizardry.item.ItemDyedDust;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;


public class TileEntityDustDye extends TileEntity implements IInventory{
    //only 1 slot for now, might change if dyes are required as input
    private ItemStack[] contents = new ItemStack[1];
    //the currently selected color
    private String colorString;
    
    public TileEntityDustDye(){
        super();
        colorString="Color";
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
                contents[slot] = stack;
                if (stack != null && stack.stackSize > getInventoryStackLimit()) {
                        stack.stackSize = getInventoryStackLimit();
                }              
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
    public String getInventoryName()
    {
        return "RunesWiz.DustDye";
    }


    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
                player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack stack) {
        //only allow dyed dust in the dyer
        return stack.getItem() instanceof ItemDyedDust;
        //TODO auto-generated method: isItemValidForSlot
        //throw new UnsupportedOperationException("Not supported yet: isItemValidForSlot");
    }
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean hasCustomInventoryName() {
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
        //FIXME fsr, the color string does not save between game sessions
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

}
