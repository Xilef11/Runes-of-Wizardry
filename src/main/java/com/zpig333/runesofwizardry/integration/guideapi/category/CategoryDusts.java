package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;

import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryDusts {
	
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+"basic",new ItemStack(WizardryRegistry.pestle));
	}
	
}
