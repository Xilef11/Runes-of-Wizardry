package com.zpig333.runesofwizardry.block;

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

/**
 * Created by zombiepig333 on 15-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class BlockDustBlocks extends Block {

    private IIcon[] icons;

    public BlockDustBlocks(Material material) {
        super(material);
        this.setHardness(1.5F);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setStepSound(Block.soundTypeSand);
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
