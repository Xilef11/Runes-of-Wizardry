package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

/**
 * Created by zombiepig333 on 23-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
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
