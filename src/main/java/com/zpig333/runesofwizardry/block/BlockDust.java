package com.zpig333.runesofwizardry.block;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDust;

public class BlockDust extends BlockContainer {
	//TODO BlockDust for 1.8
    /** Dust block metadata states.  To be api-ified **/
    public static int DUST_UNUSED = 0;
    public static int DUST_ACTIVATING = 1;
    public static int DUST_ACTIVE = 2;
    public static int DUST_DEAD = 3;


//    @SideOnly(Side.CLIENT)
//    private IIcon icon_side;
//    @SideOnly(Side.CLIENT)
//    private IIcon icon_top;

    public BlockDust(){
        super(Material.circuits);
        this.setStepSound(Block.soundTypeSand);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setHardness(0.2F);
        this.disableStats();
    }

//    @Override
//    public IIcon getIcon(int side, int meta){
//        return side == 1 ? icon_top : icon_side;
//    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
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
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
    	//get the block 1 lower
        Block block = world.getBlockState(pos.down()).getBlock();
        if (block == null)
        {
            return false;
        } else{
            return world.isSideSolid(pos.down(), EnumFacing.UP) || block == Blocks.glass;
        }
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
        int[] colors = DustRegistry.getFloorColorRGB(3);
        return new Color(colors[0], colors[1], colors[2]).getRGB();
    }

    //public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer p, int face, float x, float y, float z)
    @Override
	public boolean onBlockActivated(World world, BlockPos pos,IBlockState state, EntityPlayer player, EnumFacing side,float hitX, float hitY, float hitZ)
    {
        if (!world.canMineBlockBody(player, pos)){
            return false;
        }

        ItemStack item = player.getCurrentEquippedItem();
        int dust = item.getItemDamage();
        //FIXME metadata stuff for BlockDust
//        if (world.getBlockMetadata(pos) > 1){
//            return false;
//        }

        int rx = (int) Math.floor(hitX * TileEntityDust.size);
        int rz = (int) Math.floor(hitZ * TileEntityDust.size);
        rx = Math.min(TileEntityDust.size - 1, rx);
        rz = Math.min(TileEntityDust.size - 1, rz);

        TileEntityDust ted = (TileEntityDust) world.getTileEntity(pos);

        if (ted.getDust(rx, rz) <= 0)
        {
            if (ted.getDust(rx, rz) == -2)
            {
                setVariableDust(ted, rx, rz, player, dust);
            } else
            {
                ted.setDust(player, rx, rz, dust);
            }

            world.notifyBlockOfStateChange(pos, Blocks.air);
            //XXX check this, not sure about stepSound.soundName
            world.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, stepSound.soundName, (stepSound.getVolume() + 1.0F) / 6.0F, stepSound.getFrequency() * 0.99F);
        }

        /*
        if (world.getBlockMetadata(i, j, k) == ACTIVE_DUST)
        {
            TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i,
                    j, k);
            ted.onRightClick(p);
            return true;
        } */
        return true;
    }

    private void setVariableDust(TileEntityDust ted, int x, int z, EntityPlayer p, int dust)
    {
        if (ted.getDust(x, z) != -2)
        {
            return;
        }
        ted.setDust(p, x, z, dust);

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                if (i == 0 || j == 0)
                {
                	//XXX check the pos, not sure. was xCoord and zCoord
                    int wx = ted.getPos().getX();
                    int wz = ted.getPos().getZ();
                    int ix = x + i;
                    int iz = z + j;

                    if (ix < 0)
                    {
                        ix = TileEntityDust.size - 1;
                        wx--;
                    } else if (ix >= TileEntityDust.size)
                    {
                        ix = 0;
                        wx++;
                    }

                    if (iz < 0)
                    {
                        iz = TileEntityDust.size - 1;
                        wz--;
                    } else if (iz >= TileEntityDust.size)
                    {
                        iz = 0;
                        wz++;
                    }
                    //XXX position
                    TileEntity te = p.worldObj.getTileEntity(new BlockPos(wx, ted.getPos().getY(), wz));

                    if (!(te instanceof TileEntityDust))
                    {
                        continue;
                    }

                    TileEntityDust nted = (TileEntityDust) te;
                    setVariableDust(nted, ix, iz, p, dust);
                }
            }
        }
    }

//TODO don't forget the icons
//    @Override
//    public void registerBlockIcons(IIconRegister ireg){
//
//        icon_top = ireg.registerIcon(References.texture_path + "dust_top");
//        icon_side = ireg.registerIcon(References.texture_path + "dust_side");
//    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDust();
    }
}
