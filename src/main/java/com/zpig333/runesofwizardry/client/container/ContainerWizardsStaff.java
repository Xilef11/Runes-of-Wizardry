package com.zpig333.runesofwizardry.client.container;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.zip.CheckedOutputStream;

/**
 * Created by zombiepig333 on 24-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class ContainerWizardsStaff extends Container {

    private ItemStack staff_open;
    public ContainerWizardsStaff(IInventory inventoryPlayer, IInventory inventoryStaff, ItemStack staff){

    }

    protected void bindPlayerInventory(IInventory inventoryPlayer)
    {
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().isItemEqual(staff_open);
    }
}
