package com.zpig333.runesofwizardry.item;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

//Might extend ItemDustPieces instead...
public class ItemDyedDust extends Item{
    public IIcon icon;
    public ItemDyedDust(){
        super();
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName("dust_dyed");
    }
    
    public void addInformation(ItemStack stack, EntityPlayer player, List data, boolean bool){
        //if the stack has no tag compound, create one and set the color to white
        if(stack.getTagCompound()==null){
            stack.setTagCompound(new NBTTagCompound());
            stack.stackTagCompound.setInteger("color", 0xffffff);
        }
        String color = String.format("#%06X", stack.stackTagCompound.getInteger("color"));
        data.add(color);
            
    }
    //this allows the itemstack to render in the defined color
    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int par2){
        return stack.getTagCompound().getInteger("color");
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ireg){
        itemIcon = ireg.registerIcon(References.texture_path + "dust_dyed");
    }
    //DEBUG
    static Random rand = new Random();
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xPos, int yPos, int zPos, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_){
        stack.getTagCompound().setInteger("color", rand.nextInt(0xffffff));
        return true;
    }
}
