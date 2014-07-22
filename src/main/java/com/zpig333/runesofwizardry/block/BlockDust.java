package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;

/**
 * Created by zombiepig333 on 22-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class BlockDust extends Block {

    //@SideOnly(Side.CLIENT)
    //private IIcon icon_side;

    public BlockDust(){
        super(Material.circuits);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    /*@Override
    public IIcon getIcon(int side, int meta){
        return side == 0 ? icon_side : this.blockIcon;
    }*/

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 65535;
    }

    @Override
    public void registerBlockIcons(IIconRegister ireg){

        this.blockIcon = ireg.registerIcon(References.texture_path + "dust_top");
        //icon_side = ireg.registerIcon(References.texture_path + "dust_side");
    }
}
