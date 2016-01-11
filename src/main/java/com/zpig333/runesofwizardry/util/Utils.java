package com.zpig333.runesofwizardry.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.zpig333.runesofwizardry.core.WizardryLogger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {
	private Utils(){}
	/**
	 * 
	 * @param in
	 * @return
	 */
	public static List<ItemStack> sortAndMergeStacks(List<ItemStack> in){
		List<ItemStack> sorted = new LinkedList<ItemStack>(in);
		sorted.sort(new ItemStackComparator());
		WizardryLogger.logInfo("Sorted list: "+Arrays.deepToString(sorted.toArray(new ItemStack[0])));
		//merge not happening correctly (fixed?)
		List merged = new LinkedList();
		int i=0;
		while(i<sorted.size()){
			ItemStack current = sorted.get(i);
			i++;
			if(i<sorted.size()){
				ItemStack is2 = sorted.get(i);
				while(i<sorted.size()&&ItemStack.areItemsEqual(current, is2)&&ItemStack.areItemStackTagsEqual(current, is2)){
					int maxSize = current.getMaxStackSize();
					if(current.stackSize+is2.stackSize>maxSize){
						int toAdd = maxSize-current.stackSize;
						current.stackSize=maxSize;
						is2.stackSize-=toAdd;
						merged.add(current);
						current=is2;
					}else{
						current.stackSize+=is2.stackSize;
					}
					i++;
					if(i<sorted.size())is2=sorted.get(i);
				}
			}else{
				i++;
			}
			merged.add(current);
		}
		WizardryLogger.logInfo("Merged list: "+Arrays.deepToString(merged.toArray(new ItemStack[0])));
		return merged;
	}
}
