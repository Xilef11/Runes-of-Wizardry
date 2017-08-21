package com.zpig333.runesofwizardry.integration.guideapi;

import com.zpig333.runesofwizardry.core.References;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@GuideBook
public class WizardryGuide implements IGuideBook{
	
	public static final String BASE_LOC = References.modid+".guidebook";

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
	public IRecipe getRecipe(ItemStack bookStack) {
		// TODO Auto-generated method stub
		return IGuideBook.super.getRecipe(bookStack);
	}

	@Override
	public void handlePost(ItemStack bookStack) {
		// TODO Auto-generated method stub
		IGuideBook.super.handlePost(bookStack);
	}
	
}
