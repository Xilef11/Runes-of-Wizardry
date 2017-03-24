package com.zpig333.runesofwizardry.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.item.ItemDustPouch;

public class PickupEventHandler {
	@SubscribeEvent
	public void onPickupDust(EntityItemPickupEvent event){
		ItemStack dust = event.getItem().getEntityItem();
		//WizardryLogger.logInfo(dust);
		if(dust.getItem() instanceof IDust && dust.getCount()>0){
			EntityPlayer player = event.getEntityPlayer();
			//Couldn't get a zombie to pick up dust, but adding the check doen't hurt
			if(player==null)return;
			InventoryPlayer inv = player.inventory;
			for(int i=0;i<inv.getSizeInventory();i++){
				//if(i==inv.currentItem)continue;//supposedly avoids deleting items
				ItemStack stack = inv.getStackInSlot(i);
				if(!stack.isEmpty() && stack.getItem() instanceof ItemDustPouch){
					ItemDustPouch pouch = (ItemDustPouch)stack.getItem();
					ItemStack contents = pouch.getDustStack(stack, 0);//get the dust type
					if(ItemStack.areItemsEqual(dust, contents)&&ItemStack.areItemStackTagsEqual(dust, contents)){
						boolean ok = pouch.addDust(stack, dust);
						event.setResult(Result.ALLOW);
						if(ok){
							dust.setCount(0);
							return;
						}
					}
				}
			}
		}
	}
}
