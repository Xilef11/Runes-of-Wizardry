package com.zpig333.runesofwizardry.item.dust;

import com.zpig333.runesofwizardry.api.IDust;

import net.minecraft.item.ItemStack;

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
		return 0xDEDFE2;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0xF9FAF5;
	}


	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		return null;
	}
	@Override
	public boolean isMagicDust(ItemStack stack){
		return false;
	}
}