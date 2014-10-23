package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

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
