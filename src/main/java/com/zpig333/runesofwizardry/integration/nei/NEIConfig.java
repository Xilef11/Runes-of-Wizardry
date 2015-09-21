/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-09-21
 */
package com.zpig333.runesofwizardry.integration.nei;

import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

import net.minecraft.item.ItemStack;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

/**
 * @author Xilef11
 *
 */
public class NEIConfig implements IConfigureNEI {

	/* (non-Javadoc)
	 * @see codechicken.nei.api.IConfigureNEI#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return References.modid+"NEI_integration";
	}

	/* (non-Javadoc)
	 * @see codechicken.nei.api.IConfigureNEI#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	/* (non-Javadoc)
	 * @see codechicken.nei.api.IConfigureNEI#loadConfig()
	 */
	@Override
	public void loadConfig() {
		// We do our stuff here
		API.hideItem(new ItemStack(WizardryRegistry.dust_placed));
	}

}
