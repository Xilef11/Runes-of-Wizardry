package com.zpig333.runesofwizardry.integration.guideapi;

import java.awt.Color;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryBasic;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryDecoration;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryDusts;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryInscriptions;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryRunes;
import com.zpig333.runesofwizardry.item.ItemRunicDictionary;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@GuideBook
public class WizardryGuide implements IGuideBook{
	
	public static final String BASE_LOC = References.modid+".guidebook",
							   CAT_LOC = BASE_LOC+".category.",
							   ENTRY_LOC = BASE_LOC+".entry.";
	public static final ResourceLocation LOCATION = new ResourceLocation(References.modid, "guide_book");

	private static final BookBinder BOOK = new BookBinder(LOCATION);
	@Override
	public Book buildBook() {
		BOOK.setHasCustomModel();
		BOOK.setColor(Color.WHITE);
		BOOK.setGuideTitle(BASE_LOC+".title");
		BOOK.setItemName(BASE_LOC+".title");
		//BOOK.setDisplayName(BASE_LOC+".display");
		BOOK.setHeader(BASE_LOC+".welcomemessage");
		BOOK.setAuthor(BASE_LOC+".author");
		
		return BOOK.build();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleModel(ItemStack bookStack) {
		ModelLoader.setCustomModelResourceLocation(
				bookStack.getItem(),
				bookStack.getMetadata(),
				new ModelResourceLocation(
						References.texture_path
						+ ((ItemRunicDictionary) WizardryRegistry.runic_dictionary)
						.getName(), "inventory"));
	}

	@Override
	public void handlePost(ItemStack bookStack) {
		RunesOfWizardry.log().info("Building guide book: basic category");
		BOOK.addCategory(CategoryBasic.getCategory());
		RunesOfWizardry.log().info("Building guide book: dusts category");
		BOOK.addCategory(CategoryDusts.getCategory());
		RunesOfWizardry.log().info("Building guide book: runes category");
		BOOK.addCategory(CategoryRunes.getCategory());
		RunesOfWizardry.log().info("Building guide book: inscriptions category");
		BOOK.addCategory(CategoryInscriptions.getCategory());
		RunesOfWizardry.log().info("Building guide book: decoration category");
		BOOK.addCategory(CategoryDecoration.getCategory());
	}
	
}
