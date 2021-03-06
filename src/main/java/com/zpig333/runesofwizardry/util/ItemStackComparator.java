/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-10
 */
package com.zpig333.runesofwizardry.util;

import java.util.Comparator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Simple comparator for ItemStacks. orders by registered item name, then meta, then quantity
 * @author Xilef11
 *
 */
public class ItemStackComparator implements Comparator<ItemStack> {

	@Override
	public int compare(ItemStack arg0, ItemStack arg1) {
		String item0 = Item.REGISTRY.getNameForObject(arg0.getItem()).toString();
		String item1 = Item.REGISTRY.getNameForObject(arg1.getItem()).toString();
		int result = item0.compareTo(item1);
		if(result==0){
			Integer meta0 = arg0.getMetadata();
			Integer meta1 = arg1.getMetadata();
			result = meta0.compareTo(meta1);
			if(result==0){
				Integer amount0 = arg0.getCount();
				Integer amount1 = arg1.getCount();
				result = amount0.compareTo(amount1);
			}
		}
		return result;
	}

}
