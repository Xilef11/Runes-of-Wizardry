package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.RunesOfWizardryAPI;
import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.awt.*;

public class BlockDust extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon icon_side;

    public BlockDust(){
        super(Material.circuits);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setStepSound(Block.soundTypeSand);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    @Override
    public IIcon getIcon(int side, int meta){
        return side == 0 ? icon_side : this.blockIcon;
    }

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
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        int[] colors = RunesOfWizardryAPI.getFloorColorRGB(0);
        return new Color(colors[0], colors[1], colors[2]).getRGB();
    }

    @Override
    public void registerBlockIcons(IIconRegister ireg){

        this.blockIcon = ireg.registerIcon(References.texture_path + "dust_top");
        icon_side = ireg.registerIcon(References.texture_path + "dust_side");
    }
}
