package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
//[refactor] removed unused method, name change
public class ItemRunicStaff extends Item {
	private final String name = "runic_staff";
    public ItemRunicStaff(){
    	GameRegistry.registerItem(this, name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setMaxDamage(50);
        this.setUnlocalizedName(References.modid+"_"+name);
    }
    public String getName(){
    	return name;
    }
}
