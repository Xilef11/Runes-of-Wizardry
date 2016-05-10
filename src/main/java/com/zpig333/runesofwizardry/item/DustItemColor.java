package com.zpig333.runesofwizardry.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

public class DustItemColor implements IItemColor {
	private static DustItemColor instance=null;
	private DustItemColor(){}
	public static DustItemColor instance(){
		if(instance==null)instance=new DustItemColor();
		return instance;
	}
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if(stack.getItem() instanceof IDust){
			IDust dust = (IDust)stack.getItem();
			//otherwise, return the colors of the dust
			return tintIndex == 0 ? dust.getPrimaryColor(stack) : dust.getSecondaryColor(stack);
		}
		return 0xFFFFFF;
	}

}
