/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-08-14
 */
package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

import net.minecraft.item.Item;

/** Class for all (simple) Items from this mod
 * @author Xilef11
 *
 */
public abstract class WizardryItem extends Item {

	public WizardryItem(){
		super();
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+getName());
		setRegistryName(References.modid,getName());
	}

	/** returns the internal name of this item**/
	public abstract String getName();
}
