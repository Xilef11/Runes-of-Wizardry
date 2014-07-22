package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
/**
 * the dust "chunks" used to craft the blocks.
 * @author Xilef11
 */
public class ItemDustPieces extends Item{
    private IIcon[] icons;
    //just the inert dust for now
    public ItemDustPieces(){
        super();
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setHasSubtypes(true);
    }
    @Override
    public IIcon getIconFromDamage(int meta){
        return icons[meta];
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        int meta = itemStack.getItemDamage();
        return super.getUnlocalizedName() + "." + meta;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ireg){
        //just the plain one for now
        icons = new IIcon[1];
        for(int i = 0; i < icons.length; ++i){
            icons[i] = ireg.registerIcon(References.texture_path + "dust_" + References.dust_types[i]);
        }
    }
}
