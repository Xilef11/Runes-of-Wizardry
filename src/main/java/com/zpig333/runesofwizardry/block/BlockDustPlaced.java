package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
/**
 * This class creates the block that holds placed dust
 * [refactor] to be fixed when we figure out how to place dusts
 */
//XXX not sure what most of this does
//public class BlockDustPlaced extends BlockContainer {
public class BlockDustPlaced extends Block implements ITileEntityProvider{
	//TODO BlockDustPlaced for 1.8

    public BlockDustPlaced(){
        super(Material.circuits);
        this.setStepSound(Block.soundTypeSand);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setHardness(0.2F);
        this.disableStats();
    }


    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
	public boolean isOpaqueCube()
    {
        return false;
    }
//
//    /**
//     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
//     */
//    public boolean renderAsNormalBlock()
//    {
//        return false;
//    }
//
//    /**
//     * Returns which pass should this block be rendered on. 0 for solids and 1
//     * for alpha
//     */
//    public int getRenderBlockPass()
//    {
//        return 1;
//    }

    public boolean canHarvestBlock(net.minecraft.world.IBlockAccess world, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
    	//this block is never harvested
    	return false;
    };
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
    	//get the block 1 lower
        Block block = world.getBlockState(pos.down()).getBlock();
        if (block == null)
        {
            return false;
        } else{
        	//FUTURE maybe tweak to use the oredict to allow other types of glass
            return world.isSideSolid(pos.down(), EnumFacing.UP) || block == Blocks.glass;
        }
    }
   
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDustPlaced();
    }


	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side,	float hitX, float hitY, float hitZ) {
		// TODO Auto-generated method stub
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY,
				hitZ);
	}
}
