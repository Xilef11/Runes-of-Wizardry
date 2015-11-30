package com.zpig333.runesofwizardry.item.dust;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.pages.PageFurnaceRecipe;
import amerifrance.guideapi.pages.PageIRecipe;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.guide.GuideWizardry;

public class DustBlaze extends IDust{
	public DustBlaze(){
		super();
	}
	@Override
	public String getDustName() {
		return "fire";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0xEB490F;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x991519;
	}

	@Override
	public int getPlacedColor(ItemStack stack) {
		return 0xFF6E1E;
	}

	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		ItemStack[] items = {new ItemStack(WizardryRegistry.lavastone,1)};
		return items;
	}
	@Override
	public String getDescription(int meta) {
		return GuideWizardry.DESC+".dust_blaze";
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getAdditionalCraftingPages(int)
	 */
	@Override
	public List<IPage> getAdditionalCraftingPages(int meta) {
		List<IPage> steps = new LinkedList<IPage>();
		steps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.nether_paste,1),
				new ItemStack(Blocks.netherrack),new ItemStack(WizardryRegistry.pestle),new ItemStack(Items.blaze_powder))));
		steps.add(new PageFurnaceRecipe(WizardryRegistry.nether_paste));
		return steps;
	}
	
}