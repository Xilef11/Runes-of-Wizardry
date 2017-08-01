package com.zpig333.runesofwizardry.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.core.WizardryLogger;

public class Utils {
	private Utils(){}
	/**
	 * 
	 * @param in
	 * @return
	 */
	public static List<ItemStack> sortAndMergeStacks(List<ItemStack> in){
		List<ItemStack> sorted = new LinkedList<>(in);
		Comparator<ItemStack> cmp = new ItemStackComparator();
		Collections.sort(sorted, cmp);
		WizardryLogger.logDebug("Sorted list: "+Arrays.deepToString(sorted.toArray(new ItemStack[0])));
		//merge not happening correctly (fixed?)
		List<ItemStack> merged = new LinkedList<>();
		int i=0;
		while(i<sorted.size()){
			ItemStack current = sorted.get(i);
			i++;
			if(i<sorted.size()){
				ItemStack is2 = sorted.get(i);
				while(i<sorted.size()&&ItemStack.areItemsEqual(current, is2)&&ItemStack.areItemStackTagsEqual(current, is2)){
					int maxSize = current.getMaxStackSize();
					if(current.getCount()+is2.getCount()>maxSize){
						int toAdd = maxSize-current.getCount();
						current.setCount(maxSize);
						is2.setCount(is2.getCount()-toAdd);
						merged.add(current);
						current=is2;
					}else{
						current.setCount(current.getCount()+is2.getCount());
					}
					i++;
					if(i<sorted.size())is2=sorted.get(i);
				}
			}else{
				i++;
			}
			merged.add(current);
		}
		WizardryLogger.logDebug("Merged list: "+Arrays.deepToString(merged.toArray(new ItemStack[0])));
		return merged;
	}
	public static String getCurrentModID(){
		ModContainer mc = Loader.instance().activeModContainer();
		String prefix;
		if (mc != null)
		{
			prefix = mc.getModId().toLowerCase();
		}
		else // no mod container,  assume minecraft
		{
			prefix = "minecraft";
		}
		return prefix;
	}
	/**
	 * Checks if a "found" stack matches a "wanted" stack. a stack size <0 in the wanted stack will match any stack size in the found stack
	 * @param wanted the ItemStack we want
	 * @param found the ItemStack to check if it matches
	 * @param oredict check for OreDictionnary match?
	 * @return
	 */
	public static boolean stacksEqualWildcardSize(ItemStack wanted, ItemStack toCheck, boolean oredict){
		if(oredict){
			boolean haveIDs=false;
			for(int wantedID:OreDictionary.getOreIDs(wanted)){
				for(int checkID:OreDictionary.getOreIDs(toCheck)){
					haveIDs=true;//both items have IDs, so we can check them using oreDict
					if(wantedID==checkID){
						if(wanted.getCount()==toCheck.getCount()||wanted.getCount()<0){
							return true;
						}
					}
				}
			}
			if(!haveIDs){
				//recall without using oredict
				return stacksEqualWildcardSize(wanted, toCheck, false);
			}else{
				return false;//both items have oredict IDs and none matched
			}
		}else{
			if(!OreDictionary.itemMatches(wanted, toCheck, false))return false;
			if(!ItemStack.areItemStackTagsEqual(wanted, toCheck))return false;
			return wanted.getCount()==toCheck.getCount() || wanted.getCount()<0;
		}
	}
}
