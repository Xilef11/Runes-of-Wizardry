package com.zpig333.runesofwizardry.item.dust;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.pages.PageIRecipe;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.guide.GuideWizardry;

public class DustPlant extends IDust {
	public DustPlant(){
		super();
	}
	@Override
	public String getDustName() {
		return "plant";
	}

	@Override
	public int getPrimaryColor(ItemStack stack) {
		return 0x188615;
	}

	@Override
	public int getSecondaryColor(ItemStack stack) {
		return 0x504C00;
	}


	@Override
	public ItemStack[] getInfusionItems(ItemStack stack) {
		ItemStack[] items={new ItemStack(WizardryRegistry.plantballs, 1, 1)};
		return items;
	}
	@Override
	public String getDescription(int meta) {
		return GuideWizardry.DESC+".dust_plant";
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDust#getAdditionalCraftingPages(int)
	 */
	@Override
	public List<IPage> getAdditionalCraftingPages(int meta) {
		List<IPage> craftingSteps = new LinkedList<IPage>();
		
		craftingSteps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 0), new ItemStack(Blocks.red_flower), new ItemStack(WizardryRegistry.pestle))));
		//craftingSteps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 0), new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(WizardryRegistry.pestle))));
		//tall grass
		craftingSteps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 0), new ItemStack(Blocks.tallgrass, 1, BlockTallGrass.EnumType.GRASS.getMeta()), new ItemStack(WizardryRegistry.pestle))));
		//Leaves
		//oredict name dosen't work for icon
		craftingSteps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 0), Blocks.leaves, new ItemStack(WizardryRegistry.pestle))));
		//saplings
		craftingSteps.add(new PageIRecipe(new ShapelessOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 0), Blocks.sapling, new ItemStack(WizardryRegistry.pestle))));
		//small -> large
		craftingSteps.add(new PageIRecipe(new ShapedOreRecipe(new ItemStack(WizardryRegistry.plantballs, 1, 1), new Object[]{
			"XXX", "XXX", "XXX", 'X', new ItemStack(WizardryRegistry.plantballs, 1, 0)
		})));
		return craftingSteps;
	}
	
}