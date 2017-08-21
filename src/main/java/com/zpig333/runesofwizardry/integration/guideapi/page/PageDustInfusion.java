package com.zpig333.runesofwizardry.integration.guideapi.page;

import amerifrance.guideapi.page.PageIRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class PageDustInfusion extends PageIRecipe {
	
	public PageDustInfusion(ItemStack[] materials, ItemStack blockIn, ItemStack blockOut){
		super(makeRecipe(materials, blockIn, blockOut));
	}
	
	private static ShapelessRecipes makeRecipe(ItemStack[] materials, ItemStack blockIn, ItemStack blockOut){
		// FUTURE temporary until we figure out what we want
		NonNullList<Ingredient> recipe = NonNullList.create();
		for(ItemStack s:materials)recipe.add(Ingredient.fromStacks(s));
		recipe.add(Ingredient.fromStacks(blockIn));
		return new ShapelessRecipes("",blockOut,recipe);
	}
}
