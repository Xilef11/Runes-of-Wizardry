package com.zpig333.runesofwizardry.api;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	// create a list of the subBlocks available for this block, i.e. one for each colour
	// ignores facings, because the facing is calculated when we place the item.
	//  - used to populate items for the creative inventory
	// - the "metadata" value of the block is set to the colours metadata
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{	
		List<ItemStack> dusts = new LinkedList<ItemStack>();
		this.getIDust().getSubItems(itemIn, tab, dusts);
		for(ItemStack i:dusts){
			list.add(new ItemStack(itemIn,1,i.getMetadata()));
		}
	}
	
}
