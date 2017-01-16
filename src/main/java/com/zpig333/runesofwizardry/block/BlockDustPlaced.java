package com.zpig333.runesofwizardry.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryLogger;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.RunesUtil;
import com.zpig333.runesofwizardry.item.ItemBroom;
import com.zpig333.runesofwizardry.item.ItemDustPouch;
import com.zpig333.runesofwizardry.item.ItemRunicStaff;
import com.zpig333.runesofwizardry.item.dust.DustPlaceholder;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDead;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;
import com.zpig333.runesofwizardry.util.RayTracer;
/**
 * This class creates the block that holds placed dust
 * 
 */
public class BlockDustPlaced extends Block{
	public BlockDustPlaced(){
		super(Material.CIRCUITS);
		this.setSoundType(SoundType.SAND);
		this.disableStats();
		this.setBlockUnbreakable();
		this.setUnlocalizedName(References.modid+"_dust_placed");
		//Could also register with null ItemBlock instead of hiding it in NEI
		ResourceLocation res = new ResourceLocation(References.modid,"dust_placed");
		GameRegistry.register(this, res);
		GameRegistry.register(new ItemBlock(this), res);
		this.setDefaultState(getDefaultState().withProperty(PROPERTYSTATE, STATE_NORMAL));
	}


	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 * @deprecated
	 */
	@Deprecated
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Deprecated
	@Override
	public boolean isFullCube(IBlockState state){
		return false;
	}
	@Deprecated
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state,World worldIn, BlockPos pos)
	{	//No collision
		//return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+0.0625F, pos.getZ()+1.0F);
		//return new AxisAlignedBB(pos, pos.add(1,1,1));
		return null;
		//return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onEntityCollidedWithBlock(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityDustPlaced){
			TileEntityDustPlaced ted = (TileEntityDustPlaced)te;
			if(ted.isInRune() && ted.getRune().handleEntityCollision(worldIn, pos, state, entityIn))return;
		}
		if(entityIn instanceof EntityItem){
			EntityItem ei = (EntityItem) entityIn;
			//these make the item look "stuck" and glitchy
			//ReflectionHelper.setPrivateValue(EntityItem.class, ei, 0, "age","field_70292_b");
			//ei.setNoDespawn();
			EntityPlayer p = worldIn.getClosestPlayerToEntity(ei, 0.4);

			if (p == null) {//if there is no player near enough, keep resetting the pickup delay
				ei.setPickupDelay(20);
			}else{
				//double dist = p.getDistanceToEntity(ei);
				//WizardryLogger.logInfo("Distance: "+dist);
				Integer pickupDelay = ReflectionHelper.getPrivateValue(EntityItem.class, ei, "delayBeforeCanPickup","field_145804_b");
				if (pickupDelay > 10) {
					ei.setPickupDelay(10);//10 is the default, but there's no getDefaultPickupDelay, so its better to hardcode it in both uses
					//ei.setDefaultPickupDelay();
				}
			}
		}else{
			super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		}
	}

	@Deprecated
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;//render type 2 is TESR
	}

	@Deprecated
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source,	BlockPos pos) {
		return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}


	@Override
	public boolean canHarvestBlock(net.minecraft.world.IBlockAccess world, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
		//this block is never harvested
		return false;
	};
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		//get the block 1 lower
		IBlockState state = world.getBlockState(pos.down());
		Block block = state.getBlock();
		if (block == null)
		{
			return false;
		} else{
			//FUTURE maybe tweak to use the oredict to allow other types of glass
			return block.isSideSolid(state, world, pos.down(), EnumFacing.UP) || block == Blocks.GLASS;
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#hasTileEntity(net.minecraft.block.state.IBlockState)
	 */
	@Override
	public boolean hasTileEntity(IBlockState state) {
		if(state.getBlock()==this)return true;
		return false;
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#createTileEntity(net.minecraft.world.World, net.minecraft.block.state.IBlockState)
	 */
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if(state.getBlock()==this){
			switch(state.getValue(PROPERTYSTATE)){
			case STATE_ACTIVE: return new TileEntityDustActive();
			case STATE_DEAD:	return new TileEntityDustDead();
			default:	return new TileEntityDustPlaced();
			}
		}
		return super.createTileEntity(world, state);
	}
	public static final int STATE_NORMAL=0,STATE_ACTIVE=1,STATE_DEAD=2;
	//this block has 1 property: active or not.
	public static final PropertyInteger PROPERTYSTATE = PropertyInteger.create("state", 0, 2);
	@Deprecated
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PROPERTYSTATE,meta);
	}
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTYSTATE);
	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTYSTATE);
	}


	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{	//drop the items
		TileEntityDustPlaced tileentityDustPlaced = (TileEntityDustPlaced) worldIn.getTileEntity(pos);
		if (tileentityDustPlaced != null) {
			if(tileentityDustPlaced.isInRune()&&!worldIn.restoringBlockSnapshots){
				tileentityDustPlaced.getRune().onPatternBroken();
			}
			Random random = new Random();
			for (int i1 = 0; i1 < tileentityDustPlaced.getSizeInventory(); i1++) {
				ItemStack itemstack = tileentityDustPlaced.getStackInSlot(i1);
				if (itemstack != null && itemstack.getItem()!=WizardryRegistry.dust_dead) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.getCount() > 0; worldIn.spawnEntityInWorld(entityitem)) {
						int j1 = random.nextInt(21) + 10;

						if (j1 > itemstack.getCount()) {
							j1 = itemstack.getCount();
						}

						itemstack.setCount(itemstack.getCount() - j1);
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
			worldIn.notifyNeighborsOfStateChange(pos, state.getBlock());
		}

		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, pos);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockDestroyedByPlayer(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState)
	 */
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos,IBlockState state) {
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, pos);
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockDestroyedByExplosion(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.world.Explosion)
	 */
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos,Explosion explosionIn) {
		TileEntityDustPlaced.updateNeighborConnectors(worldIn, pos);
	}


	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return null;//this block should not be dropped!
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(playerIn.isSneaking() || tile==null){
			return false;
		}

		//WizardryLogger.logInfo("DustPlaced block activated. pos= "+pos+" hitX: "+hitX+" hitY: "+hitY+" hitZ: "+hitZ);
		if(! (tile instanceof TileEntityDustPlaced)){
			//something is wrong
			WizardryLogger.logError("The TileEntity attached to the BlockDustPlaced at "+pos+" has bad type: "+tile.getClass());
			return false;
		}
		TileEntityDustPlaced tileDust = (TileEntityDustPlaced) tile;
		if(tileDust.isInRune()){
			RuneEntity rune = tileDust.getRune();
			if(rune.handleRightClick(worldIn,pos, state, playerIn, side, hitX, hitY, hitZ)) return true;
		}
		//NW corner has hitX:0.09 hitZ:0.09
		//NE corner has hitX:0.9 hitZ 0.09
		//SE Corner has hitX 0.9 hitZ 0.9
		//SW corner has hitX:0.02 hitZ 0.9
		float posX = hitX * TileEntityDustPlaced.COLS;
		float posZ = hitZ * TileEntityDustPlaced.ROWS;
		int row = (int) posZ;
		int col = (int) posX;

		//WizardryLogger.logInfo("Slot coords is "+row+" "+col);
		//make sure we are within bounds
		if(row<0)row=0;
		if(row>TileEntityDustPlaced.ROWS-1)row=TileEntityDustPlaced.ROWS-1;
		if(col<0)col=0;
		if(col>TileEntityDustPlaced.COLS-1)col=TileEntityDustPlaced.COLS-1;

		int slotID = TileEntityDustPlaced.getSlotIDfromPosition(row, col);

		ItemStack dustStack = tileDust.getStackInSlot(slotID);

		if(heldItem==null){
			if (dustStack !=null){
				//drop the dust piece
				if(tileDust.isInRune()){
					tileDust.getRune().onPatternBrokenByPlayer(playerIn);
					dustStack = tileDust.getStackInSlot(slotID);//re-grab the stack in case the rune changed it
				}
				tileDust.setInventorySlotContents(slotID, null);
				worldIn.playSound(null,pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundType.SAND.getBreakSound(), SoundCategory.BLOCKS, (SoundType.SAND.getVolume() + 1.0F) / 2.0F, SoundType.GROUND.getPitch() * 0.8F);
				//drop the itemStack
				if(!playerIn.capabilities.isCreativeMode&& dustStack!=null && !(dustStack.getItem() instanceof DustPlaceholder))spawnAsEntity(worldIn, pos, dustStack);
				if(tileDust.isEmpty()){//if there is no more dust, break the block
					this.breakBlock(worldIn, pos, state);
					worldIn.setBlockToAir(pos);
				}
				return true;
			}else{
				return false;
			}
		}
		//convert dust pouch to dust
		if(heldItem.getItem() instanceof ItemDustPouch&& (dustStack ==null||dustStack.getItem() instanceof DustPlaceholder)){//XXX could be switched to a capability
			heldItem = ((ItemDustPouch)heldItem.getItem()).getDustStack(heldItem, 1);
			if(heldItem==null || heldItem.getCount()<1)return false;
		}
		if(heldItem.getItem() instanceof IDust && (dustStack ==null||dustStack.getItem() instanceof DustPlaceholder)){
			//place dust in the inventory
			ItemStack newItem=null;
			if(!playerIn.capabilities.isCreativeMode){
				newItem= heldItem.splitStack(1);//grab one item from the stack
			}else{
				newItem = heldItem.copy();
				newItem.setCount(1);
			}
			tileDust.setInventorySlotContents(slotID, newItem);
			worldIn.playSound(null,pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundType.SAND.getPlaceSound(),SoundCategory.BLOCKS, (SoundType.SAND.getVolume() + 1.0F) / 2.0F, SoundType.GROUND.getPitch() * 0.8F);
			//if the dust block was "dead", mark it as normal (and change the TE)
			if(state.getValue(PROPERTYSTATE)==STATE_DEAD){
				ItemStack[][] contents = tileDust.getContents();
				worldIn.removeTileEntity(pos);
				worldIn.setBlockState(pos, getDefaultState().withProperty(PROPERTYSTATE, STATE_NORMAL));
				TileEntity te = worldIn.getTileEntity(pos);
				if(!(te instanceof TileEntityDustPlaced))throw new IllegalStateException("TileEntity not formed!");
				tileDust=(TileEntityDustPlaced)te;
				tileDust.setContents(contents);
			}
			if(tileDust.isInRune()){
				tileDust.getRune().onPatternBrokenByPlayer(playerIn);
			}
			return true;
		}
		if(heldItem.getItem() instanceof ItemBroom){
			if(! worldIn.isRemote){
				if(tileDust.isInRune()){
					tileDust.getRune().onPatternBrokenByPlayer(playerIn);
				}
				worldIn.playSound(null,pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundType.SAND.getBreakSound(),SoundCategory.BLOCKS, (SoundType.SAND.getVolume() + 1.0F) / 2.0F, SoundType.GROUND.getPitch() * 0.8F);
				if(playerIn.capabilities.isCreativeMode)RunesUtil.killDusts(worldIn, pos);
				this.breakBlock(worldIn, pos, state);
				worldIn.setBlockToAir(pos);
			}
			tileDust.clear();
			TileEntityDustPlaced.updateNeighborConnectors(worldIn, pos);
		}
		//activate the rune with the staff
		if(!tileDust.isInRune() && heldItem.getItem() instanceof ItemRunicStaff){
			RunesUtil.activateRune(worldIn, pos, playerIn);
			return true;
		}

		return false;
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onNeighborBlockChange(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.block.Block)
	 */
	@Deprecated
	@Override
	public void neighborChanged(IBlockState state,World worldIn, BlockPos pos, Block neighborBlock) {
		if(worldIn.isAirBlock(pos.down())&&!worldIn.restoringBlockSnapshots){
			this.breakBlock(worldIn, pos, state);
			worldIn.setBlockToAir(pos);
			return;
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityDustPlaced){
			RuneEntity rune = ((TileEntityDustPlaced)te).getRune();
			if(rune!=null){
				rune.handleBlockUpdate(worldIn, pos,state, neighborBlock);
			}
		}
	}


	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#onBlockClicked(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos,	EntityPlayer playerIn) {
		//called when the block is left-clicked, but does not have hitX Y Z ...
		TileEntity tile = worldIn.getTileEntity(pos);
		if(! (tile instanceof TileEntityDustPlaced)){
			WizardryLogger.logError("The TileEntity attached to the BlockDustPlaced at "+pos+" has bad type: "+tile.getClass());
		}
		TileEntityDustPlaced tileDust = (TileEntityDustPlaced) tile;
		if(!worldIn.isRemote){
			//Raytrace for the hit position
			RayTraceResult hitPos = RayTracer.retraceBlock(worldIn, playerIn, pos);
			Vec3d hit = hitPos.hitVec;//this is null client side
			//WizardryLogger.logInfo("DustPlaced block clicked. pos= "+pos+" lookX: "+look.xCoord+" lookY: "+look.yCoord+" lookZ: "+look.zCoord);
			if(tileDust.isInRune()){
				RuneEntity rune = tileDust.getRune();
				if(rune.handleLeftClick(worldIn, pos, playerIn, hit))return;
			}
			//make it relative to the block hit and find the row/column hit
			double posX = (hit.xCoord - pos.getX() )* TileEntityDustPlaced.COLS;
			double posZ = (hit.zCoord - pos.getZ() )* TileEntityDustPlaced.ROWS;
			int row = (int) posZ;
			int col = (int) posX;

			//WizardryLogger.logInfo("Slot coords is "+row+" "+col);
			//make sure we are within bounds
			if(row<0)row=0;
			if(row>TileEntityDustPlaced.ROWS-1)row=TileEntityDustPlaced.ROWS-1;
			if(col<0)col=0;
			if(col>TileEntityDustPlaced.COLS-1)col=TileEntityDustPlaced.COLS-1;

			int slotID = TileEntityDustPlaced.getSlotIDfromPosition(row, col);

			ItemStack dustStack = tileDust.getStackInSlot(slotID);

			if (dustStack !=null){
				//drop the dust piece
				if(tileDust.isInRune()){
					tileDust.getRune().onPatternBrokenByPlayer(playerIn);
					dustStack = tileDust.getStackInSlot(slotID);
				}
				tileDust.setInventorySlotContents(slotID, null);
				worldIn.playSound(null,pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundType.SAND.getPlaceSound(),SoundCategory.BLOCKS, (SoundType.SAND.getVolume() + 1.0F) / 2.0F, SoundType.GROUND.getPitch() * 0.8F);
				//drop the itemStack
				if(!playerIn.capabilities.isCreativeMode && dustStack!=null && !(dustStack.getItem() instanceof DustPlaceholder))spawnAsEntity(worldIn, pos, dustStack);
				if(tileDust.isEmpty()){//if there is no more dust, break the block
					this.breakBlock(worldIn, pos, worldIn.getBlockState(pos));
					worldIn.setBlockToAir(pos);
				}
			}
			//update the client
			//worldIn.markBlockForUpdate(pos);
			IBlockState state = worldIn.getBlockState(pos);
			worldIn.notifyBlockUpdate(pos, state, state, 3);
		}
	}
	
	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#addDestroyEffects(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.client.particle.EffectRenderer)
	 */
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos,	ParticleManager particleManager) {
		return true;//should remove the break particles
	}
	

	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#addHitEffects(net.minecraft.world.World, net.minecraft.util.RayTraceResult, net.minecraft.client.particle.EffectRenderer)
	 */
	@Override
	public boolean addHitEffects(IBlockState state,World world, RayTraceResult target,ParticleManager particleManager) {
		return true;//should remove "breaking" particles
	}


	@Override
	public boolean canDropFromExplosion(Explosion explosionIn)
	{
		return false;
	}


}
