/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2016-01-20
 */
package com.zpig333.runesofwizardry.integration.jei;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.ConfigHandler;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
		helper.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(WizardryRegistry.dust_placed,1,OreDictionary.WILDCARD_VALUE));
		if(!ConfigHandler.showPlaceholders){
			for(DustPlaceholder d:DustRegistry.getPlaceholders()){
				for(int meta:d.getMetaValues()){
					helper.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(d,1,meta));
				}
			}
		}
		//registry.addIngredientInfo(ingredient, ingredientClass, descriptionKeys);
		registry.addIngredientInfo(new ItemStack(WizardryRegistry.runic_dictionary),ItemStack.class, References.Lang.Jei.DICT);
		registry.addIngredientInfo(new ItemStack(WizardryRegistry.broom),ItemStack.class, References.Lang.Jei.BROOM);
		registry.addIngredientInfo(new ItemStack(WizardryRegistry.dust_pouch),ItemStack.class, References.Lang.Jei.POUCH);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		//NOP
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		//probably not needed
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
		//no need to do anything fancy here for now, we just use vanilla recipes
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
	}

}
