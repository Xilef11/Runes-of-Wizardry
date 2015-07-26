package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
//[refactor] nothing to see here
public class ItemNetherPaste extends Item {
	private final String name="nether_paste";
	public ItemNetherPaste(){
		super();
		GameRegistry.registerItem(this, name);
		this.setUnlocalizedName(References.modid+"_"+name);
		this.setCreativeTab(RunesOfWizardry.wizardry_tab);
	}
	
	public String getName(){
		return name;
	}
}
