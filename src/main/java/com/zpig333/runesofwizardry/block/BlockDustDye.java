package com.zpig333.runesofwizardry.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.client.gui.GuiDustDye;
import com.zpig333.runesofwizardry.core.References;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustDye;

public class BlockDustDye extends BlockContainer {

    private Random random = new Random();

    public BlockDustDye() {
        super(Material.rock);
        setCreativeTab(RunesOfWizardry.wizardry_tab);
        setHarvestLevel("pickaxe", 0);
        setHardness(2);
    }
    
    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta){
        return true;
    }
    
    public void registerBlockIcons(IIconRegister ireg){
        this.blockIcon=ireg.registerIcon(References.texture_path+"dust_dye_block");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDustDye();
    }

    //drops the items when the block is broken (?)
    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        TileEntityDustDye tileentityDustDye = (TileEntityDustDye) p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tileentityDustDye != null) {
            for (int i1 = 0; i1 < tileentityDustDye.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentityDustDye.getStackInSlot(i1);

                if (itemstack != null) {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = this.random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; p_149749_1_.spawnEntityInWorld(entityitem)) {
                        int j1 = this.random.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(p_149749_1_, p_149749_2_ + f, p_149749_3_ + f1, p_149749_4_ + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) this.random.nextGaussian() * f3;
                        entityitem.motionY = (float) this.random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) this.random.nextGaussian() * f3;

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
        }
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);

    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                        EntityPlayer player, int metadata, float what, float these, float are){
        if (world.isRemote)
        {
            return true;
        }
        else
        { 
            TileEntityDustDye tileentityDD = (TileEntityDustDye)world.getTileEntity(x,y,z);
            
            if (tileentityDD == null || player.isSneaking()) {
                        return false;
            }
            player.openGui(RunesOfWizardry.instance, GuiDustDye.GUI_ID, world, x, y, z);            
            return true;
        }
    }
}

