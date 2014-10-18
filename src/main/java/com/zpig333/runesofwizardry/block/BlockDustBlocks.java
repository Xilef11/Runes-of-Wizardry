package com.zpig333.runesofwizardry.block;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockDustBlocks extends Block {

    private IIcon[] icons;

    public BlockDustBlocks(Material material) {
        super(material);
        this.setHardness(0.5F);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setStepSound(Block.soundTypeSand);
        this.setHarvestLevel("shovel", 0);
    }

    @Override
    public IIcon getIcon(int side, int meta){
        if(meta < 0 || meta >= icons.length){
            return null;
        }
        else{
            return icons[meta];
        }
    }

    @Override
    public int damageDropped(int i)
    {
        return i;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list){
        for(int i = 0; i < References.dust_types.length; ++i){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ireg){
        icons = new IIcon[6];
        for(int i = 0; i < icons.length; ++i){
            icons[i] = ireg.registerIcon(References.texture_path + "dust_storage_" + References.dust_types[i]);
        }
    }
}
