package com.zpig333.runesofwizardry.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class IDustStorageBlock extends BlockFalling {

	public IDustStorageBlock(Material mat){
		super(mat);
		setHardness(0.5F);
		setCreativeTab(getIDust().creativeTab());
		setStepSound(Block.soundTypeSand);
		setHarvestLevel("shovel", 0);
		setUnlocalizedName(getIDust().getmodid()+"_"+getName());
		GameRegistry.registerBlock(this, getName());
	}
	/** returns the dust that forms this block **/
	public abstract IDust getIDust();

	/** return a name for this block
	 * @return (default) [dust name]_storage
	 */
	public String getName(){
		return getIDust().getName() + "_storage";
	}
	//TODO custom block textures in 1.8
	//TODO handle metadata from the ItemStacks

}
