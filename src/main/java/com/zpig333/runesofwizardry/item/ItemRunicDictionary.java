package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
//[refactor] simple, nothing in here (name change)
public class ItemRunicDictionary extends Item {
	private final String name="runic_dictionary";
	
    public ItemRunicDictionary(){
    	GameRegistry.registerItem(this, name);
        this.setMaxStackSize(1);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName(References.modid+"_"+name);
    }
    public String getName(){
    	return name;
    }

}
