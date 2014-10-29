package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class BlockLavastone_bricks extends Block{

	public BlockLavastone_bricks(Material p_i45394_1_) {
		super(p_i45394_1_);
		setStepSound(soundTypeStone);
		setCreativeTab(RunesOfWizardry.wizardry_tab);
		setBlockName("lavastone_bricks");
		setHardness(2);
		setHarvestLevel("pickaxe", 0);
		setLightLevel(0.72F);
	}
	@Override
	public void registerBlockIcons(IIconRegister ireg){
		 this.blockIcon=ireg.registerIcon(References.texture_path+"lavastone_bricks");
	}
	
    @Override
    public int damageDropped(int i)
    {
        return i;
    }
		
}