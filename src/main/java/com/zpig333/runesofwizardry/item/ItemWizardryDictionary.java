package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class ItemWizardryDictionary extends Item {
	private final String name="wizardry_dictionary";
	
    public ItemWizardryDictionary(){
    	GameRegistry.registerItem(this, name);
        this.setMaxStackSize(1);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName(References.modid+"_"+name);
    }
    public String getName(){
    	return name;
    }

}
