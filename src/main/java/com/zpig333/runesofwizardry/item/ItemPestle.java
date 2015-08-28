package com.zpig333.runesofwizardry.item;

import net.minecraft.item.ItemStack;

public class ItemPestle extends WizardryItem {

	private final String name="pestle";

	public ItemPestle(){
		super();
		this.setMaxDamage(63);
		this.setMaxStackSize(1);
		this.setNoRepair();

	}

	@Override
	public String getName(){
		return name;
	}

	@Override
	public boolean hasContainerItem(ItemStack itemStack){
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack){
		itemStack.setItemDamage(itemStack.getItemDamage() + 1);
		itemStack.stackSize = 1;
		return itemStack;
	}

}
