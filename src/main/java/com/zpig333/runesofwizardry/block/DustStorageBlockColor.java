package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.api.IDust;
import com.zpig333.runesofwizardry.api.IDustStorageBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class DustStorageBlockColor implements IBlockColor {
	private static DustStorageBlockColor instance=null;
	private DustStorageBlockColor(){}
	public static DustStorageBlockColor instance(){
		if(instance==null)instance=new DustStorageBlockColor();
		return instance;
	}
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn,	BlockPos pos, int tintIndex) {
		Block b = state.getBlock();
		if(b instanceof IDustStorageBlock){
			IDust dust = ((IDustStorageBlock)b).getIDust();
			int meta = b.getMetaFromState(state);
			ItemStack stack = new ItemStack(dust,1,meta);
			if(tintIndex==0){
				return dust.getPrimaryColor(stack);
			}else if(tintIndex == 1){
				return dust.getSecondaryColor(stack);
			}
		}
		return 0xFFFFFF;
	}

}
