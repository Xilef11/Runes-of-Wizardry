package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by zombiepig333 on 17-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class ItemPlantBalls extends Item {

    private IIcon[] icons;
    public ItemPlantBalls(){
        super();
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        int meta = itemStack.getItemDamage();
        return super.getUnlocalizedName() + "." + meta;
    }

    @Override
    public IIcon getIconFromDamage(int meta){
        return icons[meta];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ireg){
        icons = new IIcon[2];
        icons[0] = ireg.registerIcon(References.texture_path + "plantball");
        icons[1] = ireg.registerIcon(References.texture_path + "plantball_small");
    }
}
