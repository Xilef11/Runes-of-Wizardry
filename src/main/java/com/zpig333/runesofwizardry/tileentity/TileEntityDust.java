package com.zpig333.runesofwizardry.tileentity;

import com.zpig333.runesofwizardry.api.RunesOfWizardryAPI;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDust extends TileEntity implements IInventory{

    public static final int size = 4;
    private int[][] pattern;

    public int getDust(int i, int j)
    {
        int rtn = pattern[i][j];
        return rtn;
    }

    public int[][][] getRendArrays()
    {
        int[][][] rtn = new int[3][size + 1][size + 1];
        int[][] n = new int[size + 2][size + 2]; // neighbors

        for (int x = 0; x < size; x++)
        {
            for (int z = 0; z < size; z++)
            {
                n[x + 1][z + 1] = getDust(x, z);
                rtn[0][x][z] = getDust(x, z);
            }
        }

        if (RunesOfWizardryAPI.isDust(worldObj.getBlock(xCoord - 1, yCoord, zCoord)))
        {
            TileEntityDust ted = (TileEntityDust) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);

            for (int i = 0; i < size; i++)
            {
                n[0][i + 1] = ted.getDust(size - 1, i);
            }
        }

        if (RunesOfWizardryAPI.isDust(worldObj.getBlock(xCoord + 1, yCoord, zCoord)))
        {
            TileEntityDust ted = (TileEntityDust) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);

            for (int i = 0; i < size; i++)
            {
                n[size + 1][i + 1] = ted.getDust(0, i);
            }
        }

        if (RunesOfWizardryAPI.isDust(worldObj.getBlock(xCoord, yCoord, zCoord - 1)))
        {
            TileEntityDust ted = (TileEntityDust) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);

            for (int i = 0; i < size; i++)
            {
                n[i + 1][0] = ted.getDust(i, size - 1);
            }
        }

        if (RunesOfWizardryAPI.isDust(worldObj.getBlock(xCoord, yCoord, zCoord + 1)))
        {
            TileEntityDust ted = (TileEntityDust) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);

            for (int i = 0; i < size; i++)
            {
                n[i + 1][size + 1] = ted.getDust(i, 0);
            }
        }

        // horiz
        for (int x = 0; x < size; x++)
        {
            for (int y = 0; y < size + 1; y++)
            {
                if (n[x + 1][y] == n[x + 1][y + 1])
                {
                    rtn[1][x][y] = n[x + 1][y];
                }
            }
        }

        // vert
        for (int x = 0; x < size + 1; x++)
        {
            for (int y = 0; y < size; y++)
            {
                if (n[x][y + 1] == n[x + 1][y + 1])
                {
                    rtn[2][x][y] = n[x][y + 1];
                }
            }
        }

        return rtn;
    }


    /* Inventory stuffs */

    @Override
    public int getSizeInventory() {
        return size * size;
    }

    @Override
    public ItemStack getStackInSlot(int lock) {
        int y = lock % size;
        int x = (lock - size) / size;
        return new ItemStack(WizardryRegistry.dust_item, 1, pattern[x][y]);
    }

    @Override
    public ItemStack decrStackSize(int lock, int amount) {
        int y = lock % size;
        int x = (lock - size) / size;
        pattern[x][y] = 0;
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int lock, ItemStack stack) {

        int x = (lock - size) / size;
        int y = lock % size;
        int size_stack = stack.stackSize;
        int meta = stack.getItemDamage();
        Item item = stack.getItem();
        if(item == WizardryRegistry.dust_item && size_stack > 0){
            pattern[x][y] = meta;
        }

    }

    @Override
    public String getInventoryName() {
        return "rw.tile_dust";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }
}