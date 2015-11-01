package com.zpig333.runesofwizardry.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

import cpw.mods.fml.common.registry.GameRegistry;
/**
 * This class creates the block that holds placed dust
 * [refactor] to be fixed when we figure out how to place dusts
 */
//public class BlockDustPlaced extends BlockContainer {
public class BlockDustPlaced extends Block implements ITileEntityProvider{
	public BlockDustPlaced(){
		super(Material.circuits);
		this.setStepSound(Block.soundTypeSand);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		this.disableStats();
		this.setBlockUnbreakable();
		this.setUnlocalizedName(References.modid+"_dust_placed");
		//Could also register with null ItemBlock instead of hiding it in NEI
		GameRegistry.registerBlock(this,ItemBlockDustPlaced.class, "dust_placed");
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
	public boolean isFullBlock(){
		return false;
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#getCollisionBoundingBoxFromPool(net.minecraft.world.World, int, int, int)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x,int y, int z) {
		return null;
	}


	@Override
	public int getRenderType(){
		return -1;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int posX,int posY,int posZ) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#canHarvestBlock(net.minecraft.entity.player.EntityPlayer, int)
	 */
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		return false;
	}


	@Override
	public boolean canPlaceBlockAt(World world, int posX,int posY,int posZ)
	{
		//get the block 1 lower
		Block block = world.getBlock(posX,posY,posZ);
		if (block == null)
		{
			return false;
		} else{
			//FUTURE maybe tweak to use the oredict to allow other types of glass
			return World.doesBlockHaveSolidTopSurface(world, posX,posY,posZ) || block == Blocks.glass;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDustPlaced();
	}
	@Override
	public void breakBlock(World worldIn, int posX,int posY,int posZ,Block block, int meta)
	{	//drop the items
		TileEntityDustPlaced tileentityDustPlaced = (TileEntityDustPlaced) worldIn.getTileEntity(posX,posY,posZ);
		if (tileentityDustPlaced != null) {
			Random random = new Random();
			for (int i1 = 0; i1 < tileentityDustPlaced.getSizeInventory(); i1++) {
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
						entityitem = new EntityItem(worldIn, posX + f, posY + f1, posZ + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getMetadata()));
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
			worldIn.updateNeighborsAboutBlockChange(posX, posY, posZ, block);
		}

		super.breakBlock(worldIn, posX, posY, posZ, block, meta);
		worldIn.removeTileEntity(posX,posY,posZ);
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, posX,posY,posZ);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockDestroyedByPlayer(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState)
	 */
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, int posX,int posY,int posZ,int meta) {
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, posX,posY,posZ);
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockDestroyedByExplosion(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.world.Explosion)
	 */
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, int posX, int posY, int posZ,Explosion explosionIn) {
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, posX,posY,posZ);
	}


	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return null;
	}


	@Override
	public boolean onBlockActivated(World worldIn, int posX, int posY, int posZ, EntityPlayer playerIn, int side,	float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(posX,posY,posZ);
		if(playerIn.isSneaking() || tile==null){
			return false;
		}
		//WizardryLogger.logInfo("DustPlaced block activated. pos= "+pos+" hitX: "+hitX+" hitY: "+hitY+" hitZ: "+hitZ);
		if(! (tile instanceof TileEntityDustPlaced)){
			//something is wrong
			WizardryLogger.logError("The TileEntity attached to the BlockDustPlaced at "+posX+" "+posY+" "+posZ+" has bad type: "+tile.getClass());
			return false;
		}
		TileEntityDustPlaced tileDust = (TileEntityDustPlaced) tile;
		//NW corner has hitX:0.09 hitZ:0.09
		//NE corner has hitX:0.9 hitZ 0.09
		//SE Corner has hitX 0.9 hitZ 0.9
		//SW corner has hitX:0.02 hitZ 0.9
		float subposX = hitX * TileEntityDustPlaced.COLS;
		float subposZ = hitZ * TileEntityDustPlaced.ROWS;
		int row = (int) subposZ;
		int col = (int) subposX;

		//WizardryLogger.logInfo("Slot coords is "+row+" "+col);
		//make sure we are within bounds
		if(row<0)row=0;
		if(row>TileEntityDustPlaced.ROWS-1)row=TileEntityDustPlaced.ROWS-1;
		if(col<0)col=0;
		if(col>TileEntityDustPlaced.COLS-1)col=TileEntityDustPlaced.COLS-1;

		int slotID = TileEntityDustPlaced.getSlotIDfromPosition(row, col);

		ItemStack playerStack = playerIn.getCurrentEquippedItem();
		ItemStack dustStack = tileDust.getStackInSlot(slotID);

		if(playerStack==null){
			if (dustStack !=null){
				//XXX removing dusts with left-click would be better
				//drop the dust piece
				tileDust.setInventorySlotContents(slotID, null);
				worldIn.playSoundEffect(posX + 0.5F, posY + 0.5F, posZ + 0.5F, Block.soundTypeSand.soundName, (Block.soundTypeSand.getVolume() + 1.0F) / 2.0F, Block.soundTypeGrass.getFrequency() * 0.8F);
				//drop the itemStack
				if(!playerIn.capabilities.isCreativeMode && !worldIn.isRemote){
					EntityItem entityitem = new EntityItem(worldIn, posX + hitX,posY+hitY,posZ+hitZ, dustStack);
					entityitem.motionX=0;
					entityitem.motionY=0;
					entityitem.motionZ=0;
					worldIn.spawnEntityInWorld(entityitem);
				}
				//if(!playerIn.capabilities.isCreativeMode)playerIn.dropPlayerItemWithRandomChoice(dustStack, true);
				if(tileDust.isEmpty()){//if there is no more dust, break the block
					this.breakBlock(worldIn, posX, posY, posZ, worldIn.getBlock(posX, posY, posZ), worldIn.getBlockMetadata(posX, posY, posZ));
					worldIn.setBlockToAir(posX,posY,posZ);
				}
				return true;
			}else{
				return false;
			}
		}

		if(playerStack.getItem() instanceof IDust && dustStack ==null){
			//place dust in the inventory
			ItemStack newItem=null;
			if(!playerIn.capabilities.isCreativeMode){
				newItem= playerStack.splitStack(1);//grab one item from the stack
			}else{
				newItem = playerStack.copy();
				newItem.stackSize=1;
			}
			tileDust.setInventorySlotContents(slotID, newItem);
			worldIn.playSoundEffect(posX + 0.5F, posY + 0.5F, posZ + 0.5F, Block.soundTypeSand.getPlaceSound(), (Block.soundTypeSand.getVolume() + 1.0F) / 2.0F, Block.soundTypeGrass.getFrequency() * 0.8F);
			return true;
		}

		return false;
	}


	@Override
	public void onNeighborBlockChange(World worldIn, int x, int y, int z,Block neighbor) {
		if(worldIn.isAirBlock(x,y-1,z)){
			this.breakBlock(worldIn, x,y,z,worldIn.getBlock(x, y, z),worldIn.getBlockMetadata(x, y, z));
			worldIn.setBlockToAir(x,y,z);
		}
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockClicked(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void onBlockClicked(World worldIn, int posX, int posT, int posZ,	EntityPlayer playerIn) {
		//called when the block is left-clicked, but does not have hitX Y Z ...
		// TODO Auto-generated method stub: onBlockClicked
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#addDestroyEffects(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.client.particle.EffectRenderer)
	 */
	@Override
	public boolean addDestroyEffects(World world, int posX,int posY,int posZ,int meta,EffectRenderer effectRenderer) {
		return true;//should remove the break particles
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#addHitEffects(net.minecraft.world.World, net.minecraft.util.MovingObjectPosition, net.minecraft.client.particle.EffectRenderer)
	 */
	@Override
	public boolean addHitEffects(World worldObj, MovingObjectPosition target,EffectRenderer effectRenderer) {
		return true;//should remove "breaking" particles
	}


	@Override
	public boolean canDropFromExplosion(Explosion explosionIn)
	{
		return false;
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	public void registerIcons(IIconRegister reg) {
		this.blockIcon= reg.registerIcon(References.texture_path+"dust_top");
	}



}
