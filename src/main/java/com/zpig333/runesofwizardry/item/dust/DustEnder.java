package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		return 0x390B53;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x024844;
	}

	@Override
	public int getPlacedColor(ItemStack stack) {
		return 0x0B4D42;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//TODO Ender infusion
		return new ItemStack[]{new ItemStack(Items.ender_pearl),new ItemStack(Blocks.obsidian)};
	}
	@Override
	public String getDescription(int meta) {
		return "runesofwizardry.guide.description.dust_ender";
	}
}