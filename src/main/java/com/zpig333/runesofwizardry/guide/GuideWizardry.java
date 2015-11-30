/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-19
 */
package com.zpig333.runesofwizardry.guide;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.base.CategoryBase;
import amerifrance.guideapi.api.registry.GuideRegistry;
import amerifrance.guideapi.api.util.BookBuilder;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryText;
import amerifrance.guideapi.pages.PageFurnaceRecipe;
import amerifrance.guideapi.pages.PageIRecipe;
import amerifrance.guideapi.pages.PageUnlocItemStack;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.dust.RWDusts;

/** Builds a basic book from Guide-API
 * @author Xilef11
 *
 */
public class GuideWizardry {
	public final static String GUIDE = "runesofwizardry.guide",
								CATEGORIES = GUIDE+".categories",
								DESC = GUIDE+".descriptions";
	public static Book guideBook;
	
	public static void buildGuide(){
		List<CategoryAbstract> categories = new LinkedList<CategoryAbstract>();
		List<EntryAbstract> basicEntries = getBasicsEntries();
		//basics
		categories.add(new CategoryItemStack(basicEntries, CATEGORIES+".basics", new ItemStack(WizardryRegistry.pestle)));
		//dusts
		categories.add(new CategoryItemStack(getDustEntries(), CATEGORIES+".dusts", new ItemStack(RWDusts.dust_inert)));
		//decoration
		categories.add(new CategoryItemStack(getDecorationEntries(), CATEGORIES+".decoration", new ItemStack(WizardryRegistry.dust_dye)));
		//CategoryBase isn't drawn...
		categories.add(new CategoryBase(getDecorationEntries(), CATEGORIES+".decoration"));
		
		BookBuilder builder = new BookBuilder();
		builder.setCategories(categories);
		builder.setUnlocBookTitle(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name");
		builder.setUnlocDisplayName(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name");
		builder.setUnlocWelcomeMessage(GUIDE+".welcomemessage");
		builder.setHasCustomModel(true);
		guideBook = builder.build();
		GuideRegistry.registerBook(guideBook);
	}
	//returns the entries for the "basics" category
	private static List<EntryAbstract> getBasicsEntries(){
		//list of entries for the basics
		List<EntryAbstract> entries = new LinkedList<EntryAbstract>();
		
		//list of pages for the pestle
		List<IPage> pestlePages = new LinkedList<IPage>();
		pestlePages.add(new PageUnlocItemStack(DESC+".pestle", WizardryRegistry.pestle));
		pestlePages.add(new PageIRecipe(new ShapedOreRecipe(WizardryRegistry.pestle, new Object[]{
			" Y ", "X X", " X ", 'X',new ItemStack(Blocks.stone),'Y',new ItemStack(Items.bone)
		})));
		entries.add(new EntryText(pestlePages, WizardryRegistry.pestle.getUnlocalizedName()+".name"));
		//inert dust
		List<IPage> inertPages = new LinkedList<IPage>();
		inertPages.add(new PageUnlocItemStack(RWDusts.dust_inert.getDescription(0), RWDusts.dust_inert));
		inertPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(RWDusts.dust_inert), new ItemStack(Items.clay_ball),new ItemStack(Items.dye,1,15),new ItemStack(WizardryRegistry.pestle))));
		inertPages.add(new PageIRecipe(new ShapedOreRecipe(DustRegistry.getBlock(RWDusts.dust_inert).getInstance(), 
				new Object[]{"XXX","XXX","XXX",'X',RWDusts.dust_inert})));
		inertPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(RWDusts.dust_inert,9), DustRegistry.getBlock(RWDusts.dust_inert))));
		entries.add(new EntryText(inertPages, RWDusts.dust_inert.getUnlocalizedName()+".name"));
		//staff
		List<IPage> staffPages = new LinkedList<IPage>();
		staffPages.add(new PageUnlocItemStack(DESC+".runic_staff", WizardryRegistry.runic_staff));
		staffPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(WizardryRegistry.runic_staff), " WX", " YW", "Z  ",
				'X',new ItemStack(Items.diamond),'Y',new ItemStack(Items.stick),
				'Z',new ItemStack(Items.gold_ingot),'W',new ItemStack(Items.gold_nugget))));
		staffPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.runic_dictionary), new ItemStack(Items.book),new ItemStack(WizardryRegistry.runic_staff))));
		entries.add(new EntryText(staffPages, WizardryRegistry.runic_staff.getUnlocalizedName()+".name"));
		return entries;
	}
	//returns the entries for all dusts
	private static List<EntryAbstract> getDustEntries(){
		List<EntryAbstract> entries = new LinkedList<EntryAbstract>();
		for(IDust dust: DustRegistry.getAllDusts()){
			if(dust.appearsInGuideBook()){
				for(int meta:dust.getMetaValues()){
					ItemStack dustStack = new ItemStack(dust,1,meta);
					List<IPage> dustPages = new LinkedList<IPage>();
					dustPages.add(new PageUnlocItemStack(dust.getDescription(meta),dustStack));
					dustPages.addAll(dust.getAdditionalCraftingPages(meta));
					if(dust.getInfusionItems(dustStack)!=null){
						if(dust.hasCustomBlock() && dust.getCustomBlock()==null){
							//NOP
						}else{
							IDustStorageBlock block;
							if(dust.hasCustomBlock()){
								block=dust.getCustomBlock();
							}
							else{
								block = DustRegistry.getBlock(dust);
							}
							//XXX temporary until we figure out what we want
							//(should really be done with a custom page)
							ItemStack[] materials = dust.getInfusionItems(dustStack);
							ItemStack[] recipe = new ItemStack[materials.length+1];
							for(int i=0;i<materials.length;i++){
								recipe[i]=materials[i];
							}
							recipe[materials.length]=new ItemStack(DustRegistry.getBlock(RWDusts.dust_inert).getInstance());
							//FIXME shows up as "shaped crafting"
							dustPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(block.getInstance(),1,meta), (Object[])recipe)));
							//add block <-> dust recipe pages
							//FIXME the *9 dosen't appear for the result
							dustPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(dust,9,meta), new ItemStack(block.getInstance(), 1, meta))));
							dustPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(block.getInstance(), 1, meta), 
											new Object[]{"XXX","XXX","XXX",'X',dustStack})));
						}
					}
					entries.add(new EntryText(dustPages, dust.getUnlocalizedName(dustStack)+".name"));
				}
			}
		}
		return entries;
	}
	//return entries for decorative stuff
	private static List<EntryAbstract> getDecorationEntries(){
		List<EntryAbstract> entries = new LinkedList<EntryAbstract>();
		//Chalk dust
		List<IPage> chalkPages = new LinkedList<IPage>();
		chalkPages.add(new PageUnlocItemStack(WizardryRegistry.dust_dyed.getDescription(0), WizardryRegistry.dust_dyed));
		chalkPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.dust_dyed,32), new ItemStack(Items.brick, 1), new ItemStack(Items.dye, 1, 15), new ItemStack(WizardryRegistry.pestle))));
		entries.add(new EntryText(chalkPages, WizardryRegistry.dust_dyed.getUnlocalizedName()+".name"));
		//dust dye
		List<IPage> dyePages = new LinkedList<IPage>();
		dyePages.add(new PageUnlocItemStack(DESC+".dustDye", WizardryRegistry.dust_dye));
		dyePages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(WizardryRegistry.dust_dye), "XXX","XYX","XXX",'X',new ItemStack(Items.dye),'Y',new ItemStack(WizardryRegistry.dust_dyed))));
		entries.add(new EntryText(dyePages, WizardryRegistry.dust_dye.getUnlocalizedName()+".name"));
		//lavastone bricks
		List<IPage> bricksPages = new LinkedList<IPage>();
		bricksPages.add(new PageUnlocItemStack(DESC+".lavastone_bricks", WizardryRegistry.lavastone_bricks));
		bricksPages.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(WizardryRegistry.lavastone_bricks,4),new Object[]{
			"XX","XX",'X',new ItemStack(WizardryRegistry.lavastone,1)
		})));
		bricksPages.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.nether_paste,1),
				new ItemStack(Blocks.netherrack),new ItemStack(WizardryRegistry.pestle),new ItemStack(Items.blaze_powder))));
		bricksPages.add(new PageFurnaceRecipe(WizardryRegistry.nether_paste));
		entries.add(new EntryText(bricksPages,WizardryRegistry.lavastone_bricks.getUnlocalizedName()+".name"));
		
		return entries;
	}
}
