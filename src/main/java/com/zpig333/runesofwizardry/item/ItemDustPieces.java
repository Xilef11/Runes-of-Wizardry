package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/**
 * the dust "chunks" used to craft the blocks.
 * 
 */
public class ItemDustPieces extends Item{
    private IIcon icon_foreground;
    private IIcon icon_background;

    public ItemDustPieces(){
        super();
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(int meta, int pass){
        if(pass == 0){
            return icon_foreground;
        }else {
            return icon_background;
        }
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack){
        int meta = itemStack.getItemDamage();
        return super.getUnlocalizedName() + "_" + DustRegistry.names[meta];
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xPos, int yPos, int zPos, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_){

        if(world.isRemote){
            return true;
        }
        else {
            Block block = world.getBlock(xPos, yPos, zPos);
            if (block == Blocks.vine || block == Blocks.tallgrass || block == Blocks.deadbush || block == WizardryRegistry.dust_placed || block == Blocks.snow_layer) {
                return false;
            }
            world.setBlock(xPos, yPos + 1, zPos, WizardryRegistry.dust_placed);
            world.playSoundEffect((double)((float)xPos + 0.5F), (double)((float)yPos + 0.5F), (double)((float)zPos + 0.5F), Block.soundTypeSand.func_150496_b(), (Block.soundTypeSand.getVolume() + 1.0F) / 2.0F, Block.soundTypeGrass.getPitch() * 0.8F);
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        for(int i = 0; i < DustRegistry.ids.length; i++){
            if(DustRegistry.colors[i] != null) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        int meta = stack.getItemDamage();
        return pass == 0 ? DustRegistry.getPrimaryColor(meta) : DustRegistry.getSecondaryColor(meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ireg){
        //just the plain one for now
        icon_foreground = ireg.registerIcon(References.texture_path + "dust_item_fore");
        icon_background = ireg.registerIcon(References.texture_path + "dust_item_sub");
    }
}
