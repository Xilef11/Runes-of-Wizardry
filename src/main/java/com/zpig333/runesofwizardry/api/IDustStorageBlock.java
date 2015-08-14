package com.zpig333.runesofwizardry.api;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;

public abstract class IDustStorageBlock extends BlockFalling {
    
    public IDustStorageBlock(Material mat){
        super(mat);
    }
    /** returns the dust that forms this block **/
    public abstract IDust getIDust();

    //TODO custom block textures in 1.8
    //We want to create images on the fly, save them to disk and use those as textures

}
