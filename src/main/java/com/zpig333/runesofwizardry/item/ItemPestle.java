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
	
	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#doesContainerItemLeaveCraftingGrid(net.minecraft.item.ItemStack)
	 */
	@Override
	public boolean doesContainerItemLeaveCraftingGrid(ItemStack p_77630_1_) {
		return false;
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack){
		//itemStack.setMetadata(itemStack.getMetadata() + 1);
		itemStack.stackSize = 1;
		return itemStack;
	}

}
