package com.zpig333.runesofwizardry.recipe;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.ItemDustPouch;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
//FIXME sometimes crafting a pouch by itself will break the crafting grid (it doesn't even call "matches")
public class RecipeDustPouch extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	
	//http://www.minecraftforge.net/forum/index.php/topic,23133.0.html
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack pouch=ItemStack.EMPTY;
		ItemStack dust = ItemStack.EMPTY;//only 1 dust stack for now
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem() instanceof ItemDustPouch){
					if(pouch.isEmpty()){
						pouch=stack.copy();
					}else{//if we have more than one pouch
						return false;
					}
				}else if(stack.getItem()instanceof IDust){
					if(dust.isEmpty()){
						dust=stack;
					}else{
						return false;//if we already have dust
					}
				}else{
					return false;//not a pouch or dust
				}
			}
		}
		return !pouch.isEmpty() && (dust.isEmpty() || ((ItemDustPouch)pouch.getItem()).canAddDust(pouch, dust));//all we have is a single pouch and (possibly) dust
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack pouch = ItemStack.EMPTY;
		ItemStack dust = ItemStack.EMPTY;
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty())continue;
			if(stack.getItem() instanceof ItemDustPouch){
				pouch=stack.copy();
			}else if(stack.getItem() instanceof IDust){
				dust=stack;
			}
		}
		if(!dust.isEmpty()){
			//putting dust in
			((ItemDustPouch)pouch.getItem()).addDust(pouch, dust);
			return pouch.copy();
		}else{//taking dust out or clearing
			if(((ItemDustPouch)pouch.getItem()).getDustAmount(pouch)==0){
				((ItemDustPouch)pouch.getItem()).clear(pouch);
				return pouch;
			}
			dust = ((ItemDustPouch)pouch.getItem()).getDustStack(pouch, Integer.MAX_VALUE);
			if(dust.isEmpty()){//clear the pouch
				((ItemDustPouch)pouch.getItem()).clear(pouch);
				return pouch;
			}else{
				return dust;
			}
		}
	}

	@Override
	public ItemStack getRecipeOutput() {
		return new ItemStack(WizardryRegistry.dust_pouch);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		ItemStack pouch = ItemStack.EMPTY;
		int slot=0;
		boolean remainder = false;
		NonNullList<ItemStack> r = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
		for(int i=0;i<inv.getSizeInventory();i++){
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()){
				if(stack.getItem()instanceof IDust){
					inv.setInventorySlotContents(i, ItemStack.EMPTY);
					return r;//no remainder if we have dust
				}
				if(stack.getItem() instanceof ItemDustPouch){
					slot=i;
					pouch=stack.copy();
					remainder = ((ItemDustPouch)pouch.getItem()).getDustAmount(pouch)==0;
					ItemStack s = ((ItemDustPouch)pouch.getItem()).getDustStack(pouch, Integer.MAX_VALUE);
					remainder=!(s.isEmpty()||remainder);
				}
			}
		}
		if(remainder)r.set(slot,pouch);
		return r;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width*height>2;
	}

}
