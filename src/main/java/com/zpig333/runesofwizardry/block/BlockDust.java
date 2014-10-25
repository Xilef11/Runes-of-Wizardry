package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.RunesOfWizardryAPI;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDust;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;

public class BlockDust extends BlockContainer {

    @SideOnly(Side.CLIENT)
    private IIcon icon_side;
    @SideOnly(Side.CLIENT)
    private IIcon icon_top;

    public BlockDust(){
        super(Material.circuits);
        this.setStepSound(Block.soundTypeSand);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setHardness(0.2F);
        this.disableStats();
    }

    @Override
    public IIcon getIcon(int side, int meta){
        return side == 1 ? icon_top : icon_side;
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

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1
     * for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        Block block = world.getBlock(i, j - 1, k);

        if (block == null)
        {
            return false;
        } else{
            return world.isSideSolid(i, j - 1, k, ForgeDirection.UP) || block == Blocks.glass;
        }
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        int[] colors = RunesOfWizardryAPI.getFloorColorRGB(0);
        return new Color(colors[0], colors[1], colors[2]).getRGB();
    }

    @Override
    public void registerBlockIcons(IIconRegister ireg){

        icon_top = ireg.registerIcon(References.texture_path + "dust_top");
        icon_side = ireg.registerIcon(References.texture_path + "dust_side");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDust();
    }
}
