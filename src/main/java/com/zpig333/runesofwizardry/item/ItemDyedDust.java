package com.zpig333.runesofwizardry.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.zpig333.runesofwizardry.RunesOfWizardry;
import com.zpig333.runesofwizardry.core.References;

public class ItemDyedDust extends Item{
	private final String name="dust_dyed";
    public ItemDyedDust(){
        super();
        GameRegistry.registerItem(this, name);
        this.setCreativeTab(RunesOfWizardry.wizardry_tab);
        this.setUnlocalizedName(References.modid+"_"+name);
    }
    public String getName(){
    	return name;
    }
    //add tooltip
    @Override
	public void addInformation(ItemStack stack, EntityPlayer player, List data, boolean bool){
        //if the stack has no tag compound, create one and set the color to white
        if(stack.getTagCompound()==null){
        	NBTTagCompound compound = new NBTTagCompound();
        	compound.setInteger("color", 0xffffff);
            stack.setTagCompound(compound);
        }
        String color = String.format("#%06X", stack.getTagCompound().getInteger("color"));
        data.add(color);
            
    }
    //this allows the itemstack to render in the defined color
    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int layer){
        NBTTagCompound tag=stack.getTagCompound();
        if(tag==null){
            return 0xffffff;
        }
        return tag.getInteger("color");
    }

    /* FIXME onItemUse has changed?
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xPos, int yPos, int zPos, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_){
        //TODO place the dust
        return true;
    }
    */
}
