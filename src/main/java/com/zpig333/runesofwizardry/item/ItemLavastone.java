package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;


public class ItemLavastone extends Item{
    public ItemLavastone(){
        super();
        setCreativeTab(RunesOfWizardry.wizardry_tab);
        setMaxStackSize(64);
        setUnlocalizedName("lavastone");
    }
     @Override
    public void registerIcons(IIconRegister ireg){
         //TODO draw the icon...
        this.itemIcon=ireg.registerIcon(References.texture_path+"lavastone");
    }

}
