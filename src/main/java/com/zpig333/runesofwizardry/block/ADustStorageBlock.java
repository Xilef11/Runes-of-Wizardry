package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ADustStorageBlock extends BlockFalling implements IDustStorageBlock {

	public ADustStorageBlock(Material mat,String modID){
		super(mat);
		setHardness(0.5F);
		setCreativeTab(getIDust().creativeTab());
		setSoundType(SoundType.SAND);
		setHarvestLevel("shovel", 0);
		setUnlocalizedName(modID+"_"+getName());
		setRegistryName(modID,getName());
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
	@Deprecated
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
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{	
		for(int meta:this.getIDust().getMetaValues()){
			list.add(new ItemStack(this,1,meta));
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
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
