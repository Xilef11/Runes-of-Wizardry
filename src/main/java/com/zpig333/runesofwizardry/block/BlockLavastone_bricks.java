package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockLavastone_bricks extends Block{
	private final String name="lavastone_bricks";

	public BlockLavastone_bricks(Material p_i45394_1_) {
		super(p_i45394_1_);
		setStepSound(soundTypeStone);
		setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+name);
		setHardness(2);
		setResistance(12);//slightly better than nether bricks (10)
		setHarvestLevel("pickaxe", 0);
		setLightLevel(0.72F);
		GameRegistry.registerBlock(this, name);
	}
	public String getName(){
		return name;
	}
	/* (non-Javadoc)
	 * @see net.minecraft.block.Block#registerIcons(net.minecraft.client.renderer.texture.IIconRegister)
	 */
	@Override
	public void registerIcons(IIconRegister reg) {
		this.blockIcon=reg.registerIcon(References.texture_path+"lavastone_bricks");
	}



}