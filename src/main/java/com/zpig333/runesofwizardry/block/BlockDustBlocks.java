package com.zpig333.runesofwizardry.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class BlockDustBlocks extends Block {

	//TODO BlockDustBlocks for 1.8

    public BlockDustBlocks(Material material) {
        super(material);
        this.setHardness(0.5F);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setStepSound(Block.soundTypeSand);
        this.setHarvestLevel("shovel", 0);
    }


    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list){
        for(int i = 0; i < References.dust_types.length; ++i){
            list.add(new ItemStack(item, 1, i));
        }
    }


}
