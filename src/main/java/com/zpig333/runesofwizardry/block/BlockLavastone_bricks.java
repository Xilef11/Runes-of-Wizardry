package com.zpig333.runesofwizardry.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

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
		ResourceLocation loc = new ResourceLocation(References.modid,name);
		GameRegistry.register(this, loc);
		GameRegistry.register(new ItemBlock(this), loc);
	}
	public String getName(){
		return name;
	}


}