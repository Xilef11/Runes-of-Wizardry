package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockLavastone_bricks extends Block{
	private final String name="lavastone_bricks";

	public BlockLavastone_bricks(Material p_i45394_1_) {
		super(p_i45394_1_);
		setSoundType(SoundType.STONE);
		setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+name);
		setHardness(2);
		setResistance(12);//slightly better than nether bricks (10)
		setHarvestLevel("pickaxe", 0);
		setLightLevel(0.72F);
		setRegistryName(References.modid,name);
	}
	public String getName(){
		return name;
	}


}