/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-18
 */
package com.zpig333.runesofwizardry.item;

/**
 * @author Xilef11
 *
 */
public class ItemSacrificeNegator extends WizardryItem {
	private final String name="sacrifice_negator";
	
	public ItemSacrificeNegator() {
		super();
		this.setMaxStackSize(64);
		this.setNoRepair();
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.item.WizardryItem#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

}
