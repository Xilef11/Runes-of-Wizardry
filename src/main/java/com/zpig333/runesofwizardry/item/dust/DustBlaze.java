package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class DustBlaze extends IDust{
	public DustBlaze(){
		super();
	}
	@Override
	public String getDustName() {
		return "fire";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0xEB490F;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x991519;
	}

	@Override
	public int getPlacedColor(ItemStack stack) {
		return 0xFF6E1E;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		ItemStack[] items = {new ItemStack(WizardryRegistry.lavastone,1)};
		return items;
	}
}