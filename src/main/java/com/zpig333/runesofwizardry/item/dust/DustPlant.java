package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

public class DustPlant extends IDust {
	public DustPlant(){
		super();
	}
	@Override
	public String getDustName() {
		return "plant";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0x068F08;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x045C05;
	}


	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		ItemStack[] items={new ItemStack(WizardryRegistry.plantballs, 1, 1)};
		return items;
	}
}