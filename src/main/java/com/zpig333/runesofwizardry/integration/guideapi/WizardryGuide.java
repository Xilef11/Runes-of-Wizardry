package com.zpig333.runesofwizardry.integration.guideapi;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryBasic;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryDusts;
import com.zpig333.runesofwizardry.integration.guideapi.category.CategoryRunes;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@GuideBook
public class WizardryGuide implements IGuideBook{
	
	public static final String BASE_LOC = References.modid+".guidebook",
							   CAT_LOC = BASE_LOC+".category.",
							   ENTRY_LOC = BASE_LOC+".entry.";

	public static final Book BOOK = new Book();
	@Override
	public Book buildBook() {
		BOOK.setTitle(BASE_LOC+".title");
		BOOK.setDisplayName(BASE_LOC+".display");
		BOOK.setWelcomeMessage(BASE_LOC+".welcomemessage");
		BOOK.setAuthor(BASE_LOC+".author");
		BOOK.setRegistryName(new ResourceLocation(References.modid,"guide_book"));
		
		return BOOK;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleModel(ItemStack bookStack) {
		// TODO Auto-generated method stub
		IGuideBook.super.handleModel(bookStack);
	}

	@Override
	public void handlePost(ItemStack bookStack) {
		RunesOfWizardry.log().info("Building guide book: basic category");
		BOOK.addCategory(CategoryBasic.getCategory());
		RunesOfWizardry.log().info("Building guide book: dusts category");
		BOOK.addCategory(CategoryDusts.getCategory());
		RunesOfWizardry.log().info("Building guide book: runes category");
		BOOK.addCategory(CategoryRunes.getCategory());
	}
	
}
