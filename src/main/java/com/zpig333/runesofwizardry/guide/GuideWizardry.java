/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-11-19
 */
package com.zpig333.runesofwizardry.guide;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

/** Builds a basic book from Guide-API
 * @author Xilef11
 *
 */
public class GuideWizardry {
	public static Book myBook;
	//a very basic book, not so customizable in terms of icon + modid
	//maybe if we extend ItemGuideBook we can do better
	//but if we do that we may need to also have our own version of guideRegistry and use that one in the book class.
	public static void buildGuide() {
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
//		myBook = new Book(categories, "My Book title", "My welcome message", "book name", Color.green);
		
		GuideRegistry.registerBook(myBook); // Register your book with Guide-API
	}
}
