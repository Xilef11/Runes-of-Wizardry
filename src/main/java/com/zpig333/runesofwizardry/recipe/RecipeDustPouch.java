package com.zpig333.runesofwizardry.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.ItemDustPouch;

public class RecipeDustPouch implements IRecipe {
	//http://www.minecraftforge.net/forum/index.php/topic,23133.0.html
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack pouch=null;
		ItemStack dust = null;//only 1 dust stack for now
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack!=null){
				if(stack.getItem() instanceof ItemDustPouch){
					if(pouch==null){
						pouch=ItemStack.copyItemStack(stack);
					}else{//if we have more than one pouch
						return false;
					}
				}else if(stack.getItem()instanceof IDust){
					if(dust==null){
						dust=stack;
					}else{
						return false;//if we already have dust
					}
				}else{
					return false;//not a pouch or dust
				}
			}
		}
		return pouch!=null && (dust==null || ((ItemDustPouch)pouch.getItem()).canAddDust(pouch, dust));//all we have is a single pouch and (possibly) dust
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack pouch = null;
		ItemStack dust = null;
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack==null)continue;
			if(stack.getItem() instanceof ItemDustPouch){
				pouch=ItemStack.copyItemStack(stack);
			}else if(stack.getItem() instanceof IDust){
				dust=stack;
			}
		}
		if(dust!=null){
			//putting dust in
			((ItemDustPouch)pouch.getItem()).addDust(pouch, dust);
			return ItemStack.copyItemStack(pouch);
		}else{//taking dust out or clearing
			dust = ((ItemDustPouch)pouch.getItem()).getDustStack(pouch, Integer.MAX_VALUE);
			if(dust==null || dust.getCount()==0){//clear the pouch
				((ItemDustPouch)pouch.getItem()).clear(pouch);
				return pouch;
			}else{
				return dust;
			}
		}
	}

	@Override
	public int getRecipeSize() {
		return 4;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(WizardryRegistry.dust_pouch);
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack pouch = null;
		int slot=0;
		boolean remainder = false;
		ItemStack[] r = new ItemStack[inv.getSizeInventory()];
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack!=null){
				if(stack.getItem()instanceof IDust){
					inv.setInventorySlotContents(i, null);
					return r;//no remainder if we have dust
				}
				if(stack.getItem() instanceof ItemDustPouch){
					slot=i;
					pouch=ItemStack.copyItemStack(stack);
					ItemStack s = ((ItemDustPouch)pouch.getItem()).getDustStack(pouch, Integer.MAX_VALUE);
					remainder = !(s==null || s.getCount()==0);
				}
			}
		}
		if(remainder)r[slot]=pouch;
		return r;
	}

}
