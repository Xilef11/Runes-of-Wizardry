package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.util.Utils;

import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CategoryBasic {
	
	public static final String  NAME = "basic",
								ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		EntryAbstract basicEntry = new Entry(ENTRY_KEY+"general");
		basicEntry.addPageList(PageHelper.pagesForLongText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"general.text"),308));
		basicEntry.addPageList(PageHelper.pagesForLongText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"general.text.1"),308));
		basicEntry.addPageList(PageHelper.pagesForLongText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"general.text.2"),308));
		entries.put(new ResourceLocation(ENTRY_KEY+"general"), basicEntry);
		
		EntryAbstract pestleEntry = new EntryItemStack(WizardryRegistry.pestle.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.pestle));
		pestleEntry.addPage(new PageItemStack(ENTRY_KEY+"pestle.description", WizardryRegistry.pestle));
		for(IRecipe recipe : Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.pestle))) pestleEntry.addPage(new PageIRecipe(recipe));
		entries.put(new ResourceLocation(ENTRY_KEY+"pestle"), pestleEntry);
		
		EntryAbstract staffEntry = new EntryItemStack(WizardryRegistry.runic_staff.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.runic_staff));
		staffEntry.addPage(new PageItemStack(ENTRY_KEY+"runic_staff.description",WizardryRegistry.runic_staff));
		for(IRecipe recipe : Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.runic_staff))) staffEntry.addPage(new PageIRecipe(recipe));
		entries.put(new ResourceLocation(ENTRY_KEY+"runic_staff"), staffEntry);
		
		EntryAbstract dictionnaryEntry = new EntryItemStack(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.runic_dictionary));
		dictionnaryEntry.addPage(new PageItemStack(ENTRY_KEY+"runic_dictionnary.description",WizardryRegistry.runic_dictionary));
		//FIXME until Guide-API updates...
//		for(IRecipe recipe : Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.runic_dictionary))) dictionnaryEntry.addPage(new PageIRecipe(recipe));
//		entries.put(new ResourceLocation(ENTRY_KEY+"runic_dictionnary"), dictionnaryEntry);
		
		EntryAbstract broomEntry = new EntryItemStack(WizardryRegistry.broom.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.broom));
		broomEntry.addPage(new PageItemStack(ENTRY_KEY+"broom.description",WizardryRegistry.broom));
		for(IRecipe recipe : Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.broom))) broomEntry.addPage(new PageIRecipe(recipe));
		entries.put(new ResourceLocation(ENTRY_KEY+"broom"), broomEntry);
		
		EntryAbstract pouchEntry = new EntryItemStack(WizardryRegistry.dust_pouch.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.dust_pouch));
		pouchEntry.addPage(new PageItemStack(ENTRY_KEY+"dust_pouch.description",WizardryRegistry.dust_pouch));
		pouchEntry.addPage(new PageText(ENTRY_KEY+"dust_pouch.description.1"));
		// only add the first recipe (craft the pouch from wool)
		pouchEntry.addPage(new PageIRecipe(Utils.getRecipesForOutput(WizardryRegistry.dust_pouch.getDefaultInstance()).get(0)));
		entries.put(new ResourceLocation(ENTRY_KEY+"dust_pouch"), pouchEntry);

		EntryAbstract negatorEntry = new EntryItemStack(WizardryRegistry.sacrifice_negator.getUnlocalizedName()+".name",new ItemStack(WizardryRegistry.sacrifice_negator));
		negatorEntry.addPage(new PageItemStack(ENTRY_KEY+"sacrifice_negator.description",WizardryRegistry.sacrifice_negator));
		entries.put(new ResourceLocation(ENTRY_KEY+"sacrifice_negator"), negatorEntry);

		EntryAbstract commandEntry = new Entry(ENTRY_KEY+"commands");
		commandEntry.addPageList(PageHelper.pagesForLongText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"commands.export"),308));
		commandEntry.addPageList(PageHelper.pagesForLongText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"commands.import"),205));
		entries.put(new ResourceLocation(ENTRY_KEY+"commands"), commandEntry);
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME,new ItemStack(WizardryRegistry.pestle));
	}
	
}
