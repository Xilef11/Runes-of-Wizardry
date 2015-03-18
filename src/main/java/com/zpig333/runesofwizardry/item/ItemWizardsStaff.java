package com.zpig333.runesofwizardry.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class ItemWizardsStaff extends Item {
	private final String name = "wizards_staff";
    public ItemWizardsStaff(){
    	GameRegistry.registerItem(this, name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setMaxDamage(50);
        this.setUnlocalizedName(References.modid+"_"+name);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player){
        if(player.isSneaking()){
            player.openGui(RunesOfWizardry.instance, 0, world, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
        return itemStack;
    }
    public String getName(){
    	return name;
    }
}
