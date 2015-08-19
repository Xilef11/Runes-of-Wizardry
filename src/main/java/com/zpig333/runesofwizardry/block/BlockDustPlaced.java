package com.zpig333.runesofwizardry.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
/**
 * This class creates the block that holds placed dust
 * [refactor] to be fixed when we figure out how to place dusts
 */
//public class BlockDustPlaced extends BlockContainer {
public class BlockDustPlaced extends Block implements ITileEntityProvider{
	//TODO rendering placed dust
    public BlockDustPlaced(){
        super(Material.circuits);
        this.setStepSound(Block.soundTypeSand);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setHardness(0.2F);
        this.disableStats();
        //TODO remove break particles / make unbreakable by player
        GameRegistry.registerBlock(this, "dust_placed");
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
    @Override
    public boolean isFullCube(){
    	return false;
    }
    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {	//No collision
        return null;
    }

    @Override
    public int getRenderType(){
    	return -1;//don't render normally (-1) (FSR "normal" (3) render always renders a full block...)
    	//FIXME can't get normal render to render partial block
    }
    
    @Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}


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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {	//drop the items
    	TileEntityDustPlaced tileentityDustPlaced = (TileEntityDustPlaced) worldIn.getTileEntity(pos);

        if (tileentityDustPlaced != null) {
        	Random random = new Random();
            for (int i1 = 0; i1 < tileentityDustPlaced.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentityDustPlaced.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; worldIn.spawnEntityInWorld(entityitem)) {
                        int j1 = random.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) random.nextGaussian() * f3;
                        entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                    }
                }
            }
            worldIn.notifyBlockOfStateChange(pos, state.getBlock());
        }
    	
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side,	float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(playerIn.isSneaking() || tile==null){
			return false;
		}
		
		WizardryLogger.logInfo("DustPlaced block activated. pos= "+pos+" hitX: "+hitX+" hitY: "+hitY+" hitZ: "+hitZ);
		if(! (tile instanceof TileEntityDustPlaced)){
			//something is wrong
			WizardryLogger.logError("The TileEntity attached to the BlockDustPlaced at "+pos+" has bad type: "+tile.getClass());
			return false;
		}
		TileEntityDustPlaced tileDust = (TileEntityDustPlaced) tile;
		//NW corner has hitX:0.09 hitZ:0.09
		//NE corner has hitX:0.9 hitZ 0.09
		//SE Corner has hitX 0.9 hitZ 0.9
		//SW corner has hitX:0.02 hitZ 0.9
		float posX = hitX * 4;
		float posZ = hitZ * 4;
		int row = (int) posZ;
		int col = (int) posX;
		
		WizardryLogger.logInfo("Slot coords is "+row+" "+col);
		//make sure we are within bounds
		if(row<0)row=0;
		if(row>3)row=3;
		if(col<0)col=0;
		if(col>3)col=3;

		int slotID = TileEntityDustPlaced.getSlotIDfromPosition(row, col);
		
		ItemStack playerStack = playerIn.getCurrentEquippedItem();
		ItemStack dustStack = tileDust.getStackInSlot(slotID);
		
		if(playerStack==null){
			if (dustStack !=null){
				//XXX removing dusts with left-click would be better
				//drop the dust piece
				tileDust.setInventorySlotContents(slotID, null);
				//drop the itemStack
				//FIXME not in creative...
				spawnAsEntity(worldIn, pos, dustStack);
				if(tileDust.isEmpty()){//if there is no more dust, break the block
					//FIXME this does not break the block
					this.breakBlock(worldIn, pos, state);
				}
				return true;
			}else{
				return false;
			}
		}

		if(playerStack.getItem() instanceof IDust && dustStack ==null){
			//place dust in the inventory
			ItemStack newItem = playerStack.splitStack(1);//grab one item from the stack
			tileDust.setInventorySlotContents(slotID, newItem);
			return true;
		}
		
		return false;
	}
}
