/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-20
 */
package com.zpig333.runesofwizardry.integration.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;

/** This class configures JEI
 * @author Xilef11
 *
 */
@JEIPlugin
public class RunesofWizardryPlugin implements IModPlugin {

	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#register(mezz.jei.api.IModRegistry)
	 */
	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers helper = registry.getJeiHelpers();
		//blacklist stuff
		helper.getItemBlacklist().addItemToBlacklist(new ItemStack(WizardryRegistry.dust_placed,1,OreDictionary.WILDCARD_VALUE));
		if(!ConfigHandler.showPlaceholders){
			for(DustPlaceholder d:DustRegistry.getPlaceholders()){
				for(int meta:d.getMetaValues()){
					helper.getItemBlacklist().addItemToBlacklist(new ItemStack(d,1,meta));
				}
			}
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}
	
	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onJeiHelpersAvailable(mezz.jei.api.IJeiHelpers)
	 */
	@Deprecated
	@Override
	public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers) {
	}

	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onItemRegistryAvailable(mezz.jei.api.IItemRegistry)
	 */
	@Deprecated
	@Override
	public void onItemRegistryAvailable(IItemRegistry itemRegistry) {
	}
	
	/* (non-Javadoc)
	 * @see mezz.jei.api.IModPlugin#onRecipeRegistryAvailable(mezz.jei.api.IRecipeRegistry)
	 */
	@Deprecated
	@Override
	public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry) {
	}

}
