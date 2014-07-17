package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by zombiepig333 on 17-07-14.
 * <p/>
 * Licensed under the GPLv3
 */
public class ItemPestle extends Item {

    public ItemPestle(){
        super();
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setMaxDamage(63);
        this.setMaxStackSize(1);
        this.setNoRepair();
    }

    @Override
    public boolean hasContainerItem(ItemStack itemStack){
        return true;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack){
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack){
        itemStack.setItemDamage(itemStack.getItemDamage() + 1);
        itemStack.stackSize = 1;
        return itemStack;
    }


    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ireg){
        this.itemIcon = ireg.registerIcon(References.texture_path + "pestle");
    }

}
