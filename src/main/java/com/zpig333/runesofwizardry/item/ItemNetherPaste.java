package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import java.lang.ref.Reference;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;


public class ItemNetherPaste extends Item{
    public ItemNetherPaste(){
        super();
        setCreativeTab(RunesOfWizardry.wizardry_tab);
        setMaxStackSize(64);
        setUnlocalizedName("nether_paste");
    }
    @Override
    public void registerIcons(IIconRegister ireg){
        //TODO make the image
        this.itemIcon=ireg.registerIcon(References.texture_path+"nether_paste");
    }
    
            
}
