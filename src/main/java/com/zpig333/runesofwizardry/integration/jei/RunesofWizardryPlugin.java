/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-20
 */
package com.zpig333.runesofwizardry.integration.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.core.WizardryRegistry;

/** This class configures JEI
 * @author Xilef11
 *
 */
@JEIPlugin
public class RunesofWizardryPlugin implements IModPlugin {

	private IJeiHelpers helper;
	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onJeiHelpersAvailable(mezz.jei.api.IJeiHelpers)
	 */
	@Override
	public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
		//grab the helper
		helper=jeiHelpers;
	}

	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onItemRegistryAvailable(mezz.jei.api.IItemRegistry)
	 */
	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
	}

	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#register(mezz.jei.api.IModRegistry)
	 */
	@Override
	public void register(IModRegistry registry) {
		//blacklist stuff
		helper.getItemBlacklist().addItemToBlacklist(new ItemStack(WizardryRegistry.dust_placed,1,OreDictionary.WILDCARD_VALUE));
		helper.getItemBlacklist().addItemToBlacklist(new ItemStack(WizardryRegistry.dust_dead));
	}

	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onRecipeRegistryAvailable(mezz.jei.api.IRecipeRegistry)
	 */
	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {
	}

}