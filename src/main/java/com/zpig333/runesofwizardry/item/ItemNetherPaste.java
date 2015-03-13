package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
