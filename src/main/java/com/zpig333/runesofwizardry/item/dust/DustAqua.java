package com.zpig333.runesofwizardry.item.dust;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
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
		return 0x1752FD;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x00B5E8;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		//FUTURE Aqua infusion
		return new ItemStack[]{new ItemStack(Items.dye,1,EnumDyeColor.BLUE.getDyeDamage()),new ItemStack(Items.water_bucket)};
	}
}