package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.RunesUtil.RuneStats;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.integration.guideapi.page.PageDustPattern;

import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CategoryRunes {
	
	public static final String  NAME = "runes",
			ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		for(IRune rune:DustRegistry.getAllRunes()){
			EntryAbstract runeEntry = new Entry(rune.getName());
			runeEntry.addPage(new PageText(rune.getShortDesc()));
			
			//dusts needed
			RuneStats stats = DustRegistry.getRuneStats(DustRegistry.getRuneID(rune));
			StringBuilder text = new StringBuilder();
			text.append(RunesOfWizardry.proxy.translate(References.Lang.REQUIRES)+"\n");
			for(ItemStack s:stats.dustCosts){
				text.append("-"+(s.getCount()<10?" ":"")+s.getCount()+"x "+s.getDisplayName()+"\n");
			}
			runeEntry.addPageList(PageHelper.pagesForLongText(text.toString(),100));
			
			//sacrifice
			text = new StringBuilder();
			text.append(RunesOfWizardry.proxy.translate(References.Lang.SACRIFICE)+"\n");
			ItemStack[][] possibilities = rune.getSacrifice();
			if(possibilities!=null){
				for(int i=0;i<possibilities.length;i++){
					ItemStack[] sac = possibilities[i];
					if(i>0)text.append(" "+RunesOfWizardry.proxy.translate(References.Lang.OR)+"\n");
					if(sac!=null){
						for(ItemStack s:sac){
							if(IRune.isWildcardStack(s)){
								if(s.getCount()>1){
									text.append("- "+RunesOfWizardry.proxy.translate(References.Lang.MULTIPLES)+" "+s.getCount()+"x "+s.getDisplayName()+"\n");
								}else{
									text.append("- "+RunesOfWizardry.proxy.translate(References.Lang.ANY_AMOUNT)+" "+s.getDisplayName()+"\n");
								}
							}else{
								text.append("- "+s.getCount()+"x "+s.getDisplayName()+"\n");
							}
						}
					}else{
						text.append("  "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING)+"\n");
					}
				}
			}
			//extra sacrifice info
			String extraInfo = rune.getExtraSacrificeInfo();
			if(extraInfo!=null){
				text.append(" "+RunesOfWizardry.proxy.translate(extraInfo)+"\n");
			}else if(possibilities==null){
				text.append(" "+RunesOfWizardry.proxy.translate(References.Lang.NOTHING)+"\n");
			}
			runeEntry.addPageList(PageHelper.pagesForLongText(text.toString(),100));
			
			runeEntry.addPage(new PageDustPattern(rune.getPattern()));
			
			entries.put(new ResourceLocation(rune.getName()),runeEntry);
		}
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME, new ItemStack(WizardryRegistry.runic_dictionary));
	}
	
}
