package com.zpig333.runesofwizardry.block;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;

public abstract class ADustStorageBlock extends BlockFalling implements IDustStorageBlock {

	public ADustStorageBlock(Material mat,String modID){
		super(mat);
		setHardness(0.5F);
		setCreativeTab(getIDust().creativeTab());
		setStepSound(Block.soundTypeSand);
		setHarvestLevel("shovel", 0);
		setUnlocalizedName(modID+"_"+getName());
		GameRegistry.registerBlock(this, DustStorageItemBlock.class, modID+":"+getName());
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDustStorageBlock#getInstance()
	 */
	@Override
	public Block getInstance() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDustStorageBlock#getIDust()
	 */
	@Override
	public abstract IDust getIDust();

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IDustStorageBlock#getName()
	 */
	@Override
	public String getName(){
		return getIDust().getName() + "_storage";
	}
	//this block has 1 property: the meta value
	public static final PropertyInteger PROPERTYMETA = PropertyInteger.create("meta",0,15);
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PROPERTYMETA, meta);
	}
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PROPERTYMETA);

	}
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PROPERTYMETA);
	}
	// create a list of the subBlocks available for this block, i.e. one for each colour
	// ignores facings, because the facing is calculated when we place the item.
	//  - used to populate items for the creative inventory
	// - the "metadata" value of the block is set to the colours metadata
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{	
		List<ItemStack> dusts = new LinkedList<ItemStack>();
		this.getIDust().getSubItems(itemIn, tab, dusts);
		for(ItemStack i:dusts){
			list.add(new ItemStack(itemIn,1,i.getMetadata()));
		}
	}
	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#damageDropped(net.minecraft.block.state.IBlockState)
	 */
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(PROPERTYMETA);
	}
	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#getBlockLayer()
	 */
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos,int tintindex) {
		IBlockState state = worldIn.getBlockState(pos);
		Block b = state.getBlock();
		if(b instanceof IDustStorageBlock){
			IDust dust = ((IDustStorageBlock)b).getIDust();
			int meta = this.getMetaFromState(state);
			ItemStack stack = new ItemStack(dust,1,meta);
			if(tintindex==0){
				return dust.getPrimaryColor(stack);
			}else if(tintindex == 1){
				return dust.getSecondaryColor(stack);
			}
		}
		return super.colorMultiplier(worldIn, pos, tintindex);
	}



}
