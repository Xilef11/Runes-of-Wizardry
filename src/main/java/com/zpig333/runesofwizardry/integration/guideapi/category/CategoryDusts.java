package com.zpig333.runesofwizardry.integration.guideapi.category;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.WizardryGuide;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;
import com.zpig333.runesofwizardry.item.dust.RWDusts;
import com.zpig333.runesofwizardry.util.Utils;

import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageItemStack;
import amerifrance.guideapi.page.PageText;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public class CategoryDusts {
	
	public static final String  NAME = "dusts",
			ENTRY_KEY = WizardryGuide.ENTRY_LOC+NAME+".";
	
	public static Map<ResourceLocation, EntryAbstract> buildEntries(){
		Map<ResourceLocation, EntryAbstract> entries = new LinkedHashMap<>();
		
		EntryAbstract placeholders = new Entry(ENTRY_KEY+"placeholders");
		placeholders.addPage(new PageText(ENTRY_KEY+"placeholders.general"));
		placeholders.addPage(new PageItemStack(ENTRY_KEY+"placeholders.any",DustRegistry.ANY_DUST));
		placeholders.addPage(new PageItemStack(ENTRY_KEY+"placeholders.magic",DustRegistry.MAGIC_DUST));
		entries.put(new ResourceLocation(ENTRY_KEY+"placeholders"), placeholders);
		
		for(IDust dust: DustRegistry.getAllDusts()){
			if(!(dust instanceof DustPlaceholder)){
				for(int meta:dust.getMetaValues()){
					ItemStack stack = new ItemStack(dust,1,meta);
					EntryAbstract entry = new EntryItemStack(dust.getUnlocalizedName(stack)+".name", stack);
					for(IRecipe r:Utils.getRecipesForOutput(stack)){
						entry.addPage(new PageIRecipe(r));
					}
					IDustStorageBlock block = DustRegistry.getBlock(dust);
					if(block!=null){
						for(IRecipe r:Utils.getRecipesForOutput(new ItemStack(block.getInstance(),1,meta))){
							entry.addPage(new PageIRecipe(r));
						}
					}
					entries.put(new ResourceLocation(dust.getUnlocalizedName(stack)),entry);
				}
			}
		}
		
		//Tweaks for our dusts
		
		entries.remove(new ResourceLocation(WizardryRegistry.dust_dyed.getUnlocalizedName()));
		
		EntryAbstract firedust = entries.get(new ResourceLocation(RWDusts.dust_blaze.getUnlocalizedName()));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.lavastone))){
			firedust.addPage(new PageIRecipe(r));
		}
		firedust.addPage(new PageFurnaceRecipe(WizardryRegistry.nether_paste));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.nether_paste))){
			firedust.addPage(new PageIRecipe(r));
		}
		
		EntryAbstract plantdust = entries.get(new ResourceLocation(RWDusts.dust_plant.getUnlocalizedName()));
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.plantballs,1,0))){
			plantdust.addPage(new PageIRecipe(r));
		}
		for(IRecipe r: Utils.getRecipesForOutput(new ItemStack(WizardryRegistry.plantballs,1,1))){
			plantdust.addPage(new PageIRecipe(r));
		}
		
		return entries;
	}
	
	public static CategoryAbstract getCategory(){
		return new CategoryItemStack(buildEntries(),WizardryGuide.CAT_LOC+NAME,new ItemStack(RWDusts.dust_inert));
	}
	
}
