package com.zpig333.runesofwizardry.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemPlantBalls extends WizardryItem {
	private final String name="plantball";
	private final String[] metaName={"small","large"};
	public ItemPlantBalls(){
		super();
		this.setHasSubtypes(true);
	}
	@Override
	public String getName(){
		return name;
	}
	public String getFullName(int meta){
		return name+"_"+metaName[meta];
	}
	@Override
	public String getUnlocalizedName(ItemStack itemStack){
		int meta = itemStack.getItemDamage();
		return super.getUnlocalizedName() + "_" + metaName[meta];
	}



	@Override
	public void getSubItems(CreativeTabs tabs, NonNullList<ItemStack> list){
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
	}


}
