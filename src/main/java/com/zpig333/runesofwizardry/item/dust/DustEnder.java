package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

public class DustEnder extends IDust {
	public DustEnder(){
		super();
	}
	@Override
	public String getDustName() {
		return "ender";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0x105E51;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x0B4D42;
	}

	@Override
	public int getPlacedColor(ItemStack stack) {
		return 0x0B4D42;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//TODO Ender infusion
		return null;
	}
}