package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.zpig333.runesofwizardry.api.IDust;

public class DustGlowstone extends IDust {
	public DustGlowstone(){
		super();
	}
	@Override
	public String getDustName() {
		return "glowstone";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0xB3B919;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0xEBEE00;
	}

	@Override
	public int getPlacedColor(ItemStack stack) {
		return 0xD2D200;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//FUTURE Glowstone infusion
		return new ItemStack[]{new ItemStack(Blocks.GLOWSTONE)};
	}
}