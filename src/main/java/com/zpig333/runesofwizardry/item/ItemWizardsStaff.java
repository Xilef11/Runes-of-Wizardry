package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWizardsStaff extends Item {

    public ItemWizardsStaff(){
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setMaxDamage(50);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player){
        if(player.isSneaking()){
            player.openGui(RunesOfWizardry.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
        return itemStack;
    }

    @Override
    public void registerIcons(IIconRegister ireg){
        this.itemIcon = ireg.registerIcon(References.texture_path + "wizards_staff");
    }
}
