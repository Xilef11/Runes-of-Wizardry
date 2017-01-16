package com.zpig333.runesofwizardry.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.ItemDustPouch;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class ContainerDustDye extends Container {

	protected TileEntityDustDye tileEntity;

	public ContainerDustDye(InventoryPlayer inventoryPlayer, TileEntityDustDye te) {
		tileEntity = te;

		//the Slot constructor takes the IInventory and the slot number in that it binds to
		//and the x-y coordinates it resides on-screen
		addSlotToContainer(new ContainerDustDye.SlotDustDye(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 0, 116,30));
		//this is if we had a grid of slots. leaving it here for reference reasons
		/*for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                addSlotToContainer(new Slot(tileEntity, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }*/

		//commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return player.getDistanceSqToCenter(tileEntity.getPos())<64;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = inventorySlots.get(slot);

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			//merges the item into player inventory since it's in the tileEntity
			if (slot < 9) {
				if (!this.mergeItemStack(stackInSlot, 0, 35, true)) {
					return null;
				}
			} //places it into the tileEntity if possible since its in the player inventory
			else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
				return null;
			}

			if (stackInSlot.getCount() == 0) {
				slotObject.putStack(null);
			} else {
				slotObject.onSlotChanged();
			}

			if (stackInSlot.getCount() == stack.getCount()) {
				return null;
			}
			slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	static class SlotDustDye extends SlotItemHandler{
		public SlotDustDye(IItemHandler itemHandler, int index, int xPosition,int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}
		@Override
		public boolean isItemValid(ItemStack stack){
			//only allow dyed dusts in the slot
			if(stack!=null){
				if(stack.getItem()instanceof ItemDustPouch){
					ItemStack dust = ((ItemDustPouch)stack.getItem()).getDustStack(stack, 0);
					if(dust!=null)stack=dust;
					else return false;
				}
				if(stack.getItem()==WizardryRegistry.dust_dyed)return true;
			}
			return false;
		}
	}
}
