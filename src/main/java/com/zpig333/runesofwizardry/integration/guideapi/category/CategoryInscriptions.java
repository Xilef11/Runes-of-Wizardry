package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.Inscription;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.util.Utils;

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

public class CategoryInscriptions {
	
	public static final String  NAME = "inscriptions",
			ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		EntryAbstract blank = new EntryItemStack(WizardryRegistry.inscription.getUnlocalizedName()+".name", new ItemStack(WizardryRegistry.inscription));
		blank.addPage(new PageItemStack(ENTRY_KEY+"blank.description", WizardryRegistry.inscription));
		for(IRecipe r:Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.inscription))){
			blank.addPage(new PageIRecipe(r));
		}
		entries.put(new ResourceLocation(ENTRY_KEY+"blank"),blank);
		
		for(String id:DustRegistry.getInscIDs()){
			Inscription insc = DustRegistry.getInscriptionByID(id);
			
			EntryAbstract insEntry = new EntryItemStack(insc.getName(),DustRegistry.getStackForInscription(id));
			insEntry.addPage(new PageText(insc.getShortDesc()));
			
			StringBuilder text = new StringBuilder();
			//sacrifice
			text.append(RunesOfWizardry.proxy.translate(References.Lang.SACRIFICE)+"\n");
			ItemStack[] items = insc.getChargeItems();
			if(items!=null){
				for(ItemStack s:items){
					text.append("-"+(s.getCount()>=0? (s.getCount()<10?" ":"")+s.getCount()+"x " : RunesOfWizardry.proxy.translate(References.Lang.ANY_AMOUNT)+" ")+s.getDisplayName()+"\n");
				}
			}
			//extra sacrifice info
			String extraInfo = insc.getExtraChargeInfo();
			if(extraInfo!=null){
				text.append("  "+RunesOfWizardry.proxy.translate(extraInfo)+"\n");
			}else if(items==null){
				text.append("  "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING)+"\n");
			}
			
			insEntry.addPageList(PageHelper.pagesForLongText(text.toString(),100));
			
			//TODO pattern + requirements
			insEntry.addPage(new PageText(RunesOfWizardry.proxy.translate(ENTRY_KEY+"see_rune", RunesOfWizardry.proxy.translate(insc.getName()))));
			
			entries.put(new ResourceLocation(insc.getName()), insEntry);
		}
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME,new ItemStack(WizardryRegistry.inscription));
	}
	
}
