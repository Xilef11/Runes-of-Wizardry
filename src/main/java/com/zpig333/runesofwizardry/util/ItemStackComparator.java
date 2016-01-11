/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-10
 */
package com.zpig333.runesofwizardry.util;

import java.util.Comparator;

import com.zpig333.runesofwizardry.core.WizardryLogger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/** Simple comparator for ItemStacks. orders by registered item name, then meta, then quantity
 * @author Xilef11
 *
 */
public class ItemStackComparator implements Comparator<ItemStack> {

	@Override
	public int compare(ItemStack arg0, ItemStack arg1) {
		String item0 = Item.itemRegistry.getNameForObject(arg0.getItem()).toString();
		String item1 = Item.itemRegistry.getNameForObject(arg1.getItem()).toString();
		WizardryLogger.logInfo("Comparing items: "+item0+" and "+item1);
		int result = item0.compareTo(item1);
		if(result==0){
			Integer meta0 = arg0.getItemDamage();
			Integer meta1 = arg0.getItemDamage();
			result = meta0.compareTo(meta1);
			if(result==0){
				Integer amount0 = arg0.stackSize;
				Integer amount1 = arg1.stackSize;
				result = amount0.compareTo(amount1);
			}
		}
		return result;
	}

}
