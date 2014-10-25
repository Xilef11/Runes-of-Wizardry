package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.api.RunesOfWizardryAPI;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
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
    private Block dust_instance;

    public ItemDustPieces(Block block){
        super();
        this.dust_instance = block;
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
        return super.getUnlocalizedName() + "_" + RunesOfWizardryAPI.dusts.get(meta).getDustName();
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer p, World world, int i, int j, int k, int face, float x, float y, float z)
    {
        if (!world.canMineBlock(p, i, j, k))
            return false;
        Block var11 = world.getBlock(i, j, k);
        if (var11 == WizardryRegistry.dust_placed && world.getTileEntity(i, j, k) != null)
        {
            WizardryRegistry.dust_placed.onBlockActivated(world, i, j, k, p, face, x, y, z);
            return false;
        }
        if (var11 == Blocks.snow)
        {
            face = 1;
        }
        else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush){
            if (face == 0)
            {
                --j;
            }
            if (face == 1)
            {
                ++j;
            }
            if (face == 2)
            {
                --k;
            }
            if (face == 3)
            {
                ++k;
            }
            if (face == 4)
            {
                --i;
            }
            if (face == 5)
            {
                ++i;
            }
        }
        if (!p.canPlayerEdit(i, j, k, 7, item))
        {
            return false;
        } else if (item.stackSize == 0)
        {
            return false;
        } else
        {
            if (world.canPlaceEntityOnSide(this.dust_instance, i, j, k, false, face, (Entity) null, item))
            {
                int var13 = dust_instance.onBlockPlaced(world, i, j, k, face, x, y, z, 0);

                if (world.setBlock(i, j, k, dust_instance, 0, 6))
                {
                    if (world.getBlock(i, j, k) == this.dust_instance)
                    {
                        this.dust_instance.onBlockPlacedBy(world, i, j, k, p, item);
                        this.dust_instance.onPostBlockPlaced(world, i, j, k, var13);
                    }
                    WizardryRegistry.dust_placed.onBlockActivated(world, i, j, k, p, face, x, y, z);
                    world.playSoundEffect((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.dust_instance.stepSound.getStepResourcePath(), (this.dust_instance.stepSound.getVolume() + 1.0F) / 6.0F, this.dust_instance.stepSound.getPitch() * 0.99F);
                    --item.stackSize;
                }
            }
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list){
        for(int i = 0; i < RunesOfWizardryAPI.dusts.size(); i++){
            if(RunesOfWizardryAPI.dusts.get(i) != null) {
                list.add(new ItemStack(item, 1, i));
                item.setCreativeTab(RunesOfWizardry.wizardry_tab);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        int meta = stack.getItemDamage();
        return pass == 0 ? RunesOfWizardryAPI.getPrimaryColor(meta) : RunesOfWizardryAPI.getSecondaryColor(meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ireg){
        //just the plain one for now
        icon_foreground = ireg.registerIcon(References.texture_path + "dust_item_fore");
        icon_background = ireg.registerIcon(References.texture_path + "dust_item_sub");
    }
}
