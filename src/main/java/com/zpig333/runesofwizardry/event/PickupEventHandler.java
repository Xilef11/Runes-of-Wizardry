package com.zpig333.runesofwizardry.event;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.item.ItemDustPouch;

public class PickupEventHandler {
	@SubscribeEvent
	public void onPickupDust(ItemPickupEvent event){
		//if(event.player.worldObj.isRemote)return;
		if(event.pickedUp.getEntityItem().getItem() instanceof IDust){
			ItemStack dust = event.pickedUp.getEntityItem();
			InventoryPlayer inv = event.player.inventory;
			for(int i=0;i<inv.getSizeInventory();i++){
				//if(i==inv.currentItem)continue;//supposedly avoids deleting items
				ItemStack stack = inv.getStackInSlot(i);
				if(stack!=null && stack.getItem() instanceof ItemDustPouch){
					ItemDustPouch pouch = (ItemDustPouch)stack.getItem();
					ItemStack contents = pouch.getDustStack(stack, 0);//get the dust type
					if(contents!=null && ItemStack.areItemsEqual(dust, contents)&&ItemStack.areItemStackTagsEqual(dust, contents)){
						//FIXME this runs but does not modify anything???
						boolean ok = pouch.addDust(stack, dust);
						int size = dust.stackSize;//always 0?
						dust.stackSize=0;
						return;
					}
				}
			}
		}
	}
}
