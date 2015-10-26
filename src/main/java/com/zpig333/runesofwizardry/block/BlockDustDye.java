package com.zpig333.runesofwizardry.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.client.gui.GuiDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockDustDye extends BlockContainer{
	//might want to consider extending Block and implementing ITileEntityProvider
	private Random random = new Random();
	private final String name="dust_dye";
	public BlockDustDye(Material mat) {
		super(mat);
		setCreativeTab(RunesOfWizardry.wizardry_tab);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setUnlocalizedName(References.modid+"_"+name);
		GameRegistry.registerBlock(this, name);
	}
	public String getName(){
		return name;
	}
	@Override
	public boolean canHarvestBlock(IBlockAccess world, net.minecraft.util.BlockPos pos, EntityPlayer player) {
		return true;
	};



	/* (non-Javadoc)
	 * @see net.minecraft.block.BlockContainer#getRenderType()
	 */
	@Override
	public int getRenderType() {
		return 3;
	}
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityDustDye();
	}

	//drops the items when the block is broken (?)
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityDustDye tileentityDustDye = (TileEntityDustDye) world.getTileEntity(pos);

		if (tileentityDustDye != null) {
			for (int i1 = 0; i1 < tileentityDustDye.getSizeInventory(); ++i1) {
				ItemStack itemstack = tileentityDustDye.getStackInSlot(i1);

				if (itemstack != null) {
					float f = this.random.nextFloat() * 0.8F + 0.1F;
					float f1 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem)) {
						int j1 = this.random.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
						float f3 = 0.05F;
						entityitem.motionX = (float) this.random.nextGaussian() * f3;
						entityitem.motionY = (float) this.random.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) this.random.nextGaussian() * f3;

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}
					}
				}
			}
			//world.func_147453_f is updateNeighborsOnBlockChange(x, y, z, block)
			world.notifyBlockOfStateChange(pos, state.getBlock());
		}
		super.breakBlock(world, pos, state);

	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos,IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (world.isRemote)
		{
			return true;
		}
		else
		{ 
			TileEntityDustDye tileentityDD = (TileEntityDustDye)world.getTileEntity(pos);

			if (tileentityDD == null || player.isSneaking()) {
				return false;
			}
			player.openGui(RunesOfWizardry.instance, GuiDustDye.GUI_ID, world, pos.getX(),pos.getY(),pos.getZ());            
			return true;
		}
	}

}

