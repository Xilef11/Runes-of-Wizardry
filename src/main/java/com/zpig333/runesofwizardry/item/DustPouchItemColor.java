package com.zpig333.runesofwizardry.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class DustPouchItemColor implements IItemColor {
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(stack.getItem() instanceof ItemDustPouch){
			ItemDustPouch pouch = (ItemDustPouch)stack.getItem();
			ItemStack dust = pouch.getDustStack(stack, 0);
			if(dust.isEmpty() || tintIndex==0)return 0xFFFFFF;
			return Minecraft.getMinecraft().getItemColors().colorMultiplier(dust, tintIndex-1);
		}
		return 0xFFFFFF;
	}

}
