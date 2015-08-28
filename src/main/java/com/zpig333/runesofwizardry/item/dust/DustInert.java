package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

public class DustInert extends IDust {
	public DustInert(){
		super();
	}
	@Override
	public String getDustName() {
		return "inert";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0xE5E6E8;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0xC5C6C8;
	}


	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		return null;
	}
}