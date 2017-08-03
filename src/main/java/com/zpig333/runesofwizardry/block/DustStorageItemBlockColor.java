package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.api.IDust;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class DustStorageItemBlockColor implements IItemColor {
	private static DustStorageItemBlockColor instance=null;
	private DustStorageItemBlockColor(){}
	public static DustStorageItemBlockColor instance(){
		if(instance==null)instance=new DustStorageItemBlockColor();
		return instance;
	}
	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		Block block = Block.getBlockFromItem(stack.getItem());
		if(block instanceof ADustStorageBlock){
			IDust dust = ((ADustStorageBlock)block).getIDust();
			return Minecraft.getMinecraft().getItemColors().getColorFromItemstack(new ItemStack(dust, 1,stack.getMetadata()),tintIndex);
		}
		return 0xFFFFFF;
	}

}
