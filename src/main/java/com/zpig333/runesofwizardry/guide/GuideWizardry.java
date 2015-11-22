/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-19
 */
package com.zpig333.runesofwizardry.guide;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.item.dust.RWDusts;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.api.registry.GuideRegistry;
import amerifrance.guideapi.api.util.BookBuilder;
import amerifrance.guideapi.categories.CategoryItemStack;
import amerifrance.guideapi.entries.EntryText;
import amerifrance.guideapi.pages.PageFurnaceRecipe;
import amerifrance.guideapi.pages.PageIRecipe;
import amerifrance.guideapi.pages.PageLocText;
import amerifrance.guideapi.pages.PageUnlocText;

/** Builds a basic book from Guide-API
 * @author Xilef11
 *
 */
public class GuideWizardry {
	private final static String GUIDE = "runesofwizardry.guide",
								CATEGORIES = GUIDE+".categories",
								DESC = GUIDE+".descriptions";
	public static Book myBook;
	//a very basic book
	public static void buildBasicGuide() {
		List<EntryAbstract> entries = new ArrayList<EntryAbstract>(); // Create the list for this categories entries.

		ArrayList<IPage> pages1 = new ArrayList<IPage>(); // Create the list for this entries pages.
		pages1.add(new PageLocText("This is a page in my guide!")); // Create a page with text and add it to your pages1 list.
		entries.add(new EntryText(pages1, "My entry 1")); // Add your pages1 list to the entry list.

		ArrayList<IPage> pages2 = new ArrayList<IPage>(); // Create the list for this entries pages.
		pages2.add(new PageIRecipe(new ShapedOreRecipe(Items.apple, "AAA", "BBB", "CCC", 'A', "ingotIron", 'B', Blocks.anvil, 'C', Items.potato))); // Create a recipe page and add it to your pages2 list.
		pages2.add(new PageFurnaceRecipe("oreGold")); // Create a furnace recipe page and add it to your pages2 list.
		entries.add(new EntryText(pages2, "My entry 2")); // Add your pages2 list to the entry list.

		ArrayList<CategoryAbstract> categories = new ArrayList<CategoryAbstract>(); // Create the list for this book's categories
		categories.add(new CategoryItemStack(entries, "My category", new ItemStack(Items.painting))); // Add your entry list to the category list.

		BookBuilder builder =  new BookBuilder(); // Create a new instance of the book builder
		builder.setCategories(categories); // Set the category list of the book
		builder.setUnlocBookTitle("My book title"); // Set the unlocalized book title
		builder.setUnlocWelcomeMessage("My welcome message"); // Set the unlocalized welcome message
		builder.setUnlocDisplayName("My book name"); // Set the unlocalized item display name
		builder.setBookColor(Color.GREEN); // Set the book color
		myBook = builder.build(); // Create your book from the information provided with your BookBuilder

		GuideRegistry.registerBook(myBook); // Register your book with Guide-API
	}
	public static void buildGuide(){
		List<CategoryAbstract> categories = new LinkedList<CategoryAbstract>();
		List<EntryAbstract> basicEntries = getBasicsEntries();
		//basics
		categories.add(new CategoryItemStack(basicEntries, CATEGORIES+".basics", new ItemStack(WizardryRegistry.pestle)));
		//dusts
		categories.add(new CategoryItemStack(getDustEntries(), CATEGORIES+".dusts", new ItemStack(RWDusts.dust_inert)));
		
		BookBuilder builder = new BookBuilder();
		builder.setCategories(categories);
		builder.setUnlocBookTitle(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name");
		builder.setUnlocDisplayName(WizardryRegistry.runic_dictionary.getUnlocalizedName()+".name");
		builder.setUnlocWelcomeMessage(GUIDE+".welcomemessage");
		myBook = builder.build();
		GuideRegistry.registerBook(myBook);
	}
	//returns the entries for the "basics" category
	private static List<EntryAbstract> getBasicsEntries(){
		//list of entries for the basics
		List<EntryAbstract> entries = new LinkedList<EntryAbstract>();
		
		//list of pages for the pestle
		List<IPage> pestlePages = new LinkedList<IPage>();
		pestlePages.add(new PageUnlocText(DESC+".pestle"));
		//hopefully this works
		pestlePages.add(new PageIRecipe(new ShapedOreRecipe(WizardryRegistry.pestle, new Object[]{
			" Y ", "X X", " X ", 'X',new ItemStack(Blocks.stone),'Y',new ItemStack(Items.bone)
		})));
		entries.add(new EntryText(pestlePages, WizardryRegistry.pestle.getUnlocalizedName()+".name"));
		//TODO inert dust should be here
		
		
		return entries;
	}
	//returns the entries for all dusts
	private static List<EntryAbstract> getDustEntries(){
		List<EntryAbstract> entries = new LinkedList<EntryAbstract>();
		for(IDust dust: DustRegistry.getAllDusts()){
			if(dust.appearsInGuideBook()){
				for(int meta:dust.getMetaValues()){
					List<IPage> dustPages = new LinkedList<IPage>();
					dustPages.add(new PageUnlocText(dust.getDescription(meta)));
					dustPages.addAll(dust.getAdditionalCraftingPages(meta));
					ItemStack dustStack = new ItemStack(dust,1,meta);
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
							dustPages.add(new PageIRecipe(new ShapelessOreRecipe(block.getInstance(), (Object[])recipe)));
							//TODO add block <-> dust recipe pages
						}
					}
					entries.add(new EntryText(dustPages, dust.getUnlocalizedName(dustStack)));
				}
			}
		}
		return entries;
	}
}
