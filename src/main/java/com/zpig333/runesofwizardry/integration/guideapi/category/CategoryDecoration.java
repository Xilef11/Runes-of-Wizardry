package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.util.Utils;

import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CategoryDecoration {
	
	public static final String  NAME = "decoration",
			ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		EntryAbstract chalk = new EntryItemStack(WizardryRegistry.dust_dyed.getUnlocalizedName()+".name", new ItemStack(WizardryRegistry.dust_dyed));
		chalk.addPage(new PageItemStack(ENTRY_KEY+".dust_dyed", WizardryRegistry.dust_dyed));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.dust_dyed))){
			chalk.addPage(new PageIRecipe(r));
		}
		entries.put(new ResourceLocation(ENTRY_KEY+"dust_dyed"),chalk);
		
		EntryAbstract dustdye = new EntryItemStack(WizardryRegistry.dust_dye.getUnlocalizedName()+".name", new ItemStack(WizardryRegistry.dust_dye));
		dustdye.addPage(new PageItemStack(ENTRY_KEY+"dust_dye.description", WizardryRegistry.dust_dye));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.dust_dye))){
			dustdye.addPage(new PageIRecipe(r));
		}
		entries.put(new ResourceLocation(ENTRY_KEY+"dust_dye"), dustdye);
		
		EntryAbstract lavabricks = new EntryItemStack(WizardryRegistry.lavastone_bricks.getUnlocalizedName()+".name", new ItemStack(WizardryRegistry.lavastone_bricks));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.lavastone_bricks))){
			lavabricks.addPage(new PageIRecipe(r));
		}
		lavabricks.addPage(new PageFurnaceRecipe(WizardryRegistry.nether_paste));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.lavastone))){
			lavabricks.addPage(new PageIRecipe(r));
		}
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.nether_paste))){
			lavabricks.addPage(new PageIRecipe(r));
		}
		entries.put(new ResourceLocation(ENTRY_KEY+"lavastone"), lavabricks);
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME,new ItemStack(WizardryRegistry.dust_dye));
	}
	
}
