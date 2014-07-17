package com.zpig333.runesofwizardry.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

/**
 * Created by zombiepig333 on 15-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class BlockDustBlock extends Block {

    public BlockDustBlock(Material material){
        super(material);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundTypeSand);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ireg){
        this.blockIcon = ireg.registerIcon("runesofwizardry:block_storage");
    }

}
