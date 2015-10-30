/** Runes of Wizardry Mod for Minecraft
 *  Licensed under the GNU GPL version 3
 *  
 *  this file was created by Xilef11 on 2015-10-30
 */
package com.zpig333.runesofwizardry.block;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Xilef11
 *
 */
public class BlockDustStorage extends BlockFalling {
	private final String name = "dust_storage";
	private IIcon[] icons;
	public enum dustTypes{ inert, plant, aqua, flame, glowstone,ender };
	public BlockDustStorage(){
		super(Material.sand);
		this.stepSound = soundTypeSand;
		setCreativeTab(RunesOfWizardry.wizardry_tab);
		setUnlocalizedName(References.modid+"_"+name);
		setHarvestLevel("shovel", 0);
		this.setHardness(0.5F);
		GameRegistry.registerBlock(this, ItemBlockDustStorage.class, name);
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
        for(int i = 0; i < dustTypes.values().length; ++i){
            list.add(new ItemStack(item, 1, i));
        }
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ireg){
        icons = new IIcon[dustTypes.values().length];
        for(int i = 0; i < icons.length; ++i){
            icons[i] = ireg.registerIcon(References.texture_path + "dust_storage_" + dustTypes.values()[i]);
        }
    }
}
