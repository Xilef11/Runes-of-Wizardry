package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

public class DustAqua extends IDust {
	@Override
	public String getDustName() {
		return "aqua";
	}
	public DustAqua(){
		super();
	}
	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0x32A3FF;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x96D1FF;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//TODO Aqua infusion
		return null;
	}
}