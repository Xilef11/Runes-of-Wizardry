package com.zpig333.runesofwizardry.item;

import net.minecraft.item.Item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class ItemWizardryDictionary extends Item {

    public ItemWizardryDictionary(){
        this.setMaxStackSize(1);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
    }

    @Override
    public void registerIcons(IIconRegister ireg){
        this.itemIcon = ireg.registerIcon(References.texture_path + "wizardry_dictionary");
    }

}
