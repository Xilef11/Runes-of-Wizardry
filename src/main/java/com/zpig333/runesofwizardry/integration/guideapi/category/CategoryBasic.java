package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.util.Utils;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryBasic {
	
	public static final String  NAME = "basic",
								ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		List<IPage> pestlePages = new LinkedList<>();
		pestlePages.add(new PageItemStack(ENTRY_KEY+"pestle.description", WizardryRegistry.pestle));
		pestlePages.add(new PageIRecipe(Utils.getRecipeForOutput(new ItemStack(WizardryRegistry.pestle))));
		entries.put(new ResourceLocation(ENTRY_KEY+"pestle"), new EntryItemStack(pestlePages,ENTRY_KEY+"pestle",new ItemStack(WizardryRegistry.pestle)));
		
//		pestlePages.add(new PageItemStack(ENTRY_KEY+".runic_staff.description", WizardryRegistry.runic_staff));
//		pestlePages.add(new PageItemStack(ENTRY_KEY+".runic_dictionary.description", WizardryRegistry.runic_dictionary));
//		pestlePages.add(new PageItemStack(ENTRY_KEY+".broom.description", WizardryRegistry.broom));
//		pestlePages.add(new PageItemStack(ENTRY_KEY+".dust_pouch.description", WizardryRegistry.dust_pouch));
//		pestlePages.add(new PageItemStack(ENTRY_KEY+".sacrifice_negator.description", WizardryRegistry.sacrifice_negator));
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME,new ItemStack(WizardryRegistry.pestle));
	}
	
}
